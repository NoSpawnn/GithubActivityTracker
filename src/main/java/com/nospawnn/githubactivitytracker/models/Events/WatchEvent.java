package com.nospawnn.githubactivitytracker.models.Events;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;
import com.renomad.minum.templating.TemplateProcessor;

import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import static com.nospawnn.githubactivitytracker.web.Page.fileUtils;
import static com.nospawnn.githubactivitytracker.web.PathRegister.TEMPLATE_DIR;

public class WatchEvent extends Event {
    String action;

    public WatchEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.WatchEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.htmlTemplate = TemplateProcessor.buildProcessor(fileUtils.readTextFile(TEMPLATE_DIR + "/events/WatchEvent.html"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return htmlTemplate.renderTemplate(Map.of("action", action));
    }
}
