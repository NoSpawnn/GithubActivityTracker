package com.nospawnn.githubactivitytracker.web;

import com.nospawnn.githubactivitytracker.lib.GithubClient;
import com.nospawnn.githubactivitytracker.models.Events.Event;
import com.renomad.minum.utils.FileUtils;
import com.renomad.minum.web.IRequest;
import com.renomad.minum.web.IResponse;
import com.renomad.minum.web.Response;
import com.renomad.minum.templating.TemplateProcessor;
import java.util.Map;
import java.text.ParseException;
import java.util.List;

public class Page {
    private final GithubClient github = new GithubClient();
    private final String initialHtml;
    private final TemplateProcessor eventTableTemplate;
    private final TemplateProcessor eventTableRowTemplate;
    private final TemplateProcessor eventSingleTemplate;
    public static FileUtils fileUtils;

    public Page() {
        initialHtml = fileUtils.readTextFile("src/main/resources/templates/index.html");
        eventTableTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/events/eventTable.html"));
        eventTableRowTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/events/eventTableRow.html"));
        eventSingleTemplate = TemplateProcessor
                .buildProcessor(fileUtils.readTextFile("src/main/resources/templates/events/eventSingle.html"));
    }

    public IResponse initialHtml(IRequest request) {
        return Response.htmlOk(initialHtml);
    }

    public IResponse noEventsFound(IRequest request) {
        return Response.htmlOk("<p>That user has no events (or they don't exist!)</p>");
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
                        "user", e.getActor().displayLogin().toLowerCase(),
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

        try {
            renderedEvents = renderEventsTableRowsWith(github.getEventsForUser(user, page), page);
        } catch (ParseException e) {
            renderedEvents = "";
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

    public IResponse singleEventView(IRequest request) {
        if (!isHxRequest(request))
            return Response.redirectTo("/");

        Map<String, String> queryString = request.getRequestLine().queryString();
        String user = queryString.get("user");
        String pageNo = queryString.get("page");
        String id = queryString.get("id");

        if (user == null || pageNo == null || id == null)
            return Response.redirectTo("/");

        var event = github.getSingleEvent(user, id);

        return Response.htmlOk(eventSingleTemplate.renderTemplate(Map.of(
                "page", pageNo,
                "id", event.getId(),
                "type", event.getType().toString(),
                "timestamp", event.getCreatedAt().toString(),
                "user", event.getActor().displayLogin(),
                "eventDetails", event.formatEventDetailsHtml())));
    }

}
