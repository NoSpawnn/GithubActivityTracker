package com.nospawnn.githubactivitytracker.web;

import com.nospawnn.githubactivitytracker.lib.GithubClient;
import com.renomad.minum.utils.FileUtils;
import com.renomad.minum.web.IRequest;
import com.renomad.minum.web.IResponse;
import com.renomad.minum.web.Response;
import com.renomad.minum.templating.TemplateProcessor;
import java.util.Map;
import java.text.ParseException;

public class Page {
    private final GithubClient github = new GithubClient();
    private String initialHtml;
    private TemplateProcessor eventTableTemplate;
    private TemplateProcessor eventTableRowTemplate;

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

    public IResponse eventsForUser(IRequest request) {
        // This request was not sent by HTMX
        if (request.getHeaders().valueByKey("HX-Request") == null)
            return Response.redirectTo("/");

        StringBuilder renderedEvents = new StringBuilder();
        Map<String, String> queryString = request.getRequestLine().queryString();
        String user = queryString.get("user");

        if (user.isBlank())
            return noEventsFound(request);

        int page = queryString.get("page") == null ? 1 : Integer.parseInt(queryString.get("page"));

        try {
            github.getEventsForUser(user, page)
                    .stream()
                    .map(e -> eventTableRowTemplate.renderTemplate(Map.of(
                            "id", e.id(),
                            "repo", e.repo().name(),
                            "date", e.createdAt().toString())))
                    .forEach(renderedEvents::append);
        } catch (ParseException e) {
        }

        if (renderedEvents.isEmpty())
            return noEventsFound(request);

        return Response.htmlOk(eventTableTemplate.renderTemplate(
                Map.of(
                        "events", renderedEvents.toString(),
                        "user", user,
                        "currentPage", Integer.toString(page),
                        "prevPage", Integer.toString(page <= 1 ? 1 : page - 1),
                        "nextPage", Integer.toString(page + 1),
                        "firstPageBtnDisabled", page <= 1 ? "disabled" : "")));
    }

}
