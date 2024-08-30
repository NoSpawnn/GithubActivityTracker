package com.nospawnn.githubactivitytracker.web;

import com.nospawnn.githubactivitytracker.lib.GithubClient;
import com.nospawnn.githubactivitytracker.models.Event;
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
    private String initialHtml;
    private TemplateProcessor eventTableTemplate;
    private TemplateProcessor eventTableRowTemplate;
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, List<Event>>> eventsCache = new ConcurrentHashMap<>();

    public Page(FileUtils fileUtils) {
        initialHtml = fileUtils.readTextFile("src/main/resources/templates/index.html");
        eventTableTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/eventTable.html"));
        eventTableRowTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/eventTableRow.html"));
    }

    public IResponse initialHtml(IRequest request) {
        return Response.htmlOk(initialHtml);
    }

    public IResponse noEventsFound(IRequest request) {
        return Response.htmlOk("<div id=\"events\"><p>That user has no events (or they don't exist!)</p></div>");
    }

    public String renderEventsTableRowsWith(List<Event> events) {
        var sb = new StringBuilder();

        events.stream()
                .map(e -> eventTableRowTemplate.renderTemplate(Map.of(
                        "id", e.id(),
                        "repo", e.repo().name(),
                        "date", e.createdAt().toString())))
                .forEach(sb::append);

        return sb.toString();
    }

    public IResponse eventsForUser(IRequest request) {
        // This request was not sent by HTMX
        if (request.getHeaders().valueByKey("HX-Request") == null)
            return Response.redirectTo("/");

        String renderedEvents = "";
        Map<String, String> queryString = request.getRequestLine().queryString();
        String user = queryString.get("user").toLowerCase();
        List<Event> events;

        if (user.isBlank())
            return noEventsFound(request);

        int page = queryString.get("page") == null ? 1 : Integer.parseInt(queryString.get("page"));

        if (eventsCache.containsKey(user)) {
            var cachedEvents = eventsCache.get(user);
            if (cachedEvents.containsKey(page)) {
                // System.out.println("Found existing events: '" + user + "' - page " + page);
                renderedEvents = renderEventsTableRowsWith(cachedEvents.get(page));
            } else {
                // System.out.println("Existing user '" + user + "', no existing events for page " + page);
                try {
                    events = github.getEventsForUser(user, page);
                    cachedEvents.put(page, events);
                    renderedEvents = renderEventsTableRowsWith(events);
                } catch (ParseException e) {
                }
            }
        } else {
            // System.out.println("New user '" + user + "', fetching page " + page + " events");
            try {
                events = github.getEventsForUser(user, page);
                renderedEvents = renderEventsTableRowsWith(events);
                var newMap = new ConcurrentHashMap<Integer, List<Event>>();
                newMap.put(page, events);
                eventsCache.put(user, newMap);
            } catch (ParseException e) {
            }
        }

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

}
