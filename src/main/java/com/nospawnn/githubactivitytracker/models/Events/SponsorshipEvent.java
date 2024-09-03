package com.nospawnn.githubactivitytracker.models.Events;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;
import org.json.JSONObject;

import java.util.Date;

public class SponsorshipEvent extends Event {
    String action;

    public SponsorshipEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.SponsorshipEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
    }

    @Override
    public String formatEventDetailsHtml() {
        return "";
    }
}
