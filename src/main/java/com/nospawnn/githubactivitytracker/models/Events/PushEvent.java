package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;
import com.renomad.minum.templating.TemplateProcessor;

import static com.nospawnn.githubactivitytracker.web.Page.fileUtils;
import static com.nospawnn.githubactivitytracker.web.PathRegister.TEMPLATE_DIR;

public class PushEvent extends Event {
    int commitCount;
    int distinctCommitCount;
    String head;

    public PushEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.PushEvent, actor, repo, isPublic, createdAt);
        this.commitCount = payload.getInt("size");
        this.distinctCommitCount = payload.getInt("distinct_size");
        this.head = payload.getString("head");
        this.htmlTemplate = TemplateProcessor.buildProcessor(fileUtils.readTextFile(TEMPLATE_DIR + "/events/PushEvent.html"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return htmlTemplate.renderTemplate(Map.of(
            "commitCount", Integer.toString(commitCount),
            "distinctCommitCount", Integer.toString(distinctCommitCount),
            "head", head
        ));
    }

}
