package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;
import java.util.Map;

import com.renomad.minum.templating.TemplateProcessor;
import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Issue;
import com.nospawnn.githubactivitytracker.models.Repo;

import static com.nospawnn.githubactivitytracker.web.Page.fileUtils;
import static com.nospawnn.githubactivitytracker.web.PathRegister.TEMPLATE_DIR;

public class IssuesEvent extends Event {
    String action;
    Issue issue;

    public IssuesEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.IssuesEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.issue = Issue.fromJSONObject(payload.getJSONObject("issue"));
        this.htmlTemplate = TemplateProcessor.buildProcessor(fileUtils.readTextFile(TEMPLATE_DIR + "/events/IssuesEvent.html"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return htmlTemplate.renderTemplate(Map.of("action", action, "url", issue.url(), "title", issue.title()));
    }
}
