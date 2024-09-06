package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;
import java.util.Map;

import com.renomad.minum.templating.TemplateProcessor;
import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

import static com.nospawnn.githubactivitytracker.web.Page.fileUtils;
import static com.nospawnn.githubactivitytracker.web.PathRegister.TEMPLATE_DIR;

public class CreateEvent extends Event {
    String refType;

    public CreateEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.CreateEvent, actor, repo, isPublic, createdAt);
        this.refType = payload.getString("ref_type");
        this.htmlTemplate = TemplateProcessor.buildProcessor(fileUtils.readTextFile(TEMPLATE_DIR + "/events/CreateEvent.html"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return htmlTemplate.renderTemplate(Map.of());
    }
}
