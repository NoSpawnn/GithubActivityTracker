package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class DeleteEvent extends Event {
    String refType;

    public DeleteEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.DeleteEvent, actor, repo, isPublic, createdAt);
        this.refType = payload.getString("ref_type");
    }

    @Override
    public String formatEventDetailsHtml() {
        var sb = new StringBuilder();

        sb.append("<div class=\"row\"><div class=\"col\"><strong>Ref Type:<br><strong>" + refType + "</div></div>");

        return sb.toString();
    }
}