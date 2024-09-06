package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;
import java.util.Map;

import com.renomad.minum.templating.TemplateProcessor;
import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;
import com.nospawnn.githubactivitytracker.models.PullRequest;

import static com.nospawnn.githubactivitytracker.web.PathRegister.TEMPLATE_DIR;
import static com.nospawnn.githubactivitytracker.web.Page.fileUtils;

public class PullRequestEvent extends Event {
    String action;
    PullRequest pullRequest;

    public PullRequestEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.PullRequestEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.pullRequest = PullRequest.fromJSONObject(payload.getJSONObject("pull_request"));
        this.htmlTemplate = TemplateProcessor.buildProcessor(fileUtils.readTextFile(TEMPLATE_DIR + "/events/PullRequestEvent.html"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return htmlTemplate.renderTemplate(Map.of(
                "action", action,
                "url", pullRequest.url(),
                "number", Integer.toString(pullRequest.number())
        ));
    }

}
