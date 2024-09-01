package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class GollumEvent extends Event {
    public GollumEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.GollumEvent, actor, repo, isPublic, createdAt);
    }

    @Override
    public String formatEventDetailsHtml() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'formatEventDetailsHtml'");
    }

}
