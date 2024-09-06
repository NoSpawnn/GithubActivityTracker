package com.nospawnn.githubactivitytracker.models.Events;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;
import org.json.JSONObject;
import com.renomad.minum.templating.TemplateProcessor;

import java.util.Date;
import java.util.Map;

import static com.nospawnn.githubactivitytracker.web.Page.fileUtils;
import static com.nospawnn.githubactivitytracker.web.PathRegister.TEMPLATE_DIR;

public class ReleaseEvent extends Event {
    String action;

    public ReleaseEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.ReleaseEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.htmlTemplate = TemplateProcessor.buildProcessor(fileUtils.readTextFile(TEMPLATE_DIR + "/events/ReleaseEvent.html"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return htmlTemplate.renderTemplate(Map.of(
            "url", repo.url(),
            "name", repo.name()));
    }
}
