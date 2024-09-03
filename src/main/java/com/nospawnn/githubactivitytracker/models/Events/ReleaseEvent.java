package com.nospawnn.githubactivitytracker.models.Events;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;
import org.json.JSONObject;

import java.util.Date;

public class ReleaseEvent extends Event {
    String action;

    public ReleaseEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.ReleaseEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
    }

    @Override
    public String formatEventDetailsHtml() {
        return "<div class=\"row\"><div class=\"col\"><strong>Repo:<br></strong><a href=\"" + repo.url() + "\">" + repo.name() + "</div></div>";
    }
}
