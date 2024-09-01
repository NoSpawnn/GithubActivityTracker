package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class PublicEvent extends Event {

    public PublicEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt) {
        super(id, EventType.PublicEvent, actor, repo, isPublic, createdAt);
    }

    @Override
    public String formatEventDetailsHtml() {
        return ""; // This event has no associated payload
    }

}
