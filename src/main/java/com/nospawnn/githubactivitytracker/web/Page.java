package com.nospawnn.githubactivitytracker.web;

import com.nospawnn.githubactivitytracker.lib.GithubClient;
import com.nospawnn.githubactivitytracker.models.Events.Event;
import com.renomad.minum.utils.FileUtils;
import com.renomad.minum.web.IRequest;
import com.renomad.minum.web.IResponse;
import com.renomad.minum.web.Response;
import com.renomad.minum.templating.TemplateProcessor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.text.ParseException;
import java.util.List;

public class Page {
    private final GithubClient github = new GithubClient();
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, List<Event>>> eventsCache = new ConcurrentHashMap<>();
    private String initialHtml;
    private TemplateProcessor eventTableTemplate;
    private TemplateProcessor eventTableRowTemplate;
    private TemplateProcessor eventSingleTemplate;

    public Page(FileUtils fileUtils) {
        initialHtml = fileUtils.readTextFile("src/main/resources/templates/index.html");
        eventTableTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/eventTable.html"));
        eventTableRowTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/eventTableRow.html"));
        eventSingleTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/eventSingle.html"));
    }

    public IResponse initialHtml(IRequest request) {
        return Response.htmlOk(initialHtml);
    }

    public IResponse noEventsFound(IRequest request) {
        return Response.htmlOk("<p>That user has no events (or they don't exist!)</p>");
    }

    public List<Event> tryGetCachedEvents(String user, int page) {
        user = user.toLowerCase();

        if (!eventsCache.containsKey(user)) {
            // Brand new username
            try {
                var events = github.getEventsForUser(user, page);
                var newMap = new ConcurrentHashMap<Integer, List<Event>>();
                newMap.put(page, events);
                eventsCache.put(user, newMap);
                return events;
            } catch (ParseException e) {
            }
        }

        var cachedEvents = eventsCache.get(user);
        if (!cachedEvents.containsKey(page)) {
            // Existing user, new page
            try {
                var events = github.getEventsForUser(user, page);
                cachedEvents.put(page, events);
                return events;
            } catch (ParseException e) {
            }
        } else {
            // Existing user, existing page
            return cachedEvents.get(page);
        }

        // No events found
        return List.of();
    }

    public boolean isHxRequest(IRequest request) {
        return request.getHeaders().valueByKey("HX-Request") != null;
    }

    public String renderEventsTableRowsWith(List<Event> events, int page) {
        var sb = new StringBuilder();

        events.stream()
                .map(e -> eventTableRowTemplate.renderTemplate(Map.of(
                        "id", e.getId(),
                        "type", e.getType().toString(),
                        "page", Integer.toString(page),
                        "user", e.getActor().displayLogin(),
                        "repo", e.getRepo().name(),
                        "date", e.getCreatedAt().toString())))
                .forEach(sb::append);

        return sb.toString();
    }

    public IResponse eventsForUser(IRequest request) {
        // This request was not sent by HTMX
        if (!isHxRequest(request))
            return Response.redirectTo("/");

        String renderedEvents = "";
        Map<String, String> queryString = request.getRequestLine().queryString();
        String user = queryString.get("user").toLowerCase();

        if (user.isBlank())
            return noEventsFound(request);

        int page = queryString.get("page") == null ? 1 : Integer.parseInt(queryString.get("page"));

        renderedEvents = renderEventsTableRowsWith(tryGetCachedEvents(user, page), page);

        if (renderedEvents.isEmpty())
            return noEventsFound(request);

        return Response.htmlOk(eventTableTemplate.renderTemplate(
                Map.of(
                        "events", renderedEvents,
                        "user", user,
                        "currentPage", Integer.toString(page),
                        "prevPage", Integer.toString(page <= 1 ? 1 : page - 1),
                        "nextPage", Integer.toString(page + 1),
                        "firstPageBtnDisabled", page <= 1 ? "disabled" : "")));
    }

    public IResponse singleEventView(IRequest request) {
        if (!isHxRequest(request))
            return Response.redirectTo("/");

        Map<String, String> queryString = request.getRequestLine().queryString();
        String user = queryString.get("user");
        String pageNo = queryString.get("page");
        String id = queryString.get("id");

        if (user == null || pageNo == null || id == null)
            return Response.redirectTo("/");

        var event = eventsCache
                .get(user.toLowerCase())
                .get(Integer.parseInt(pageNo))
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .get();

        return Response.htmlOk(eventSingleTemplate.renderTemplate(Map.of(
                "page", pageNo,
                "id", event.getId(),
                "type", event.getType().toString(),
                "timestamp", event.getCreatedAt().toString(),
                "user", event.getActor().displayLogin(),
                "eventDetails", event.formatEventDetailsHtml())));
    }

}
