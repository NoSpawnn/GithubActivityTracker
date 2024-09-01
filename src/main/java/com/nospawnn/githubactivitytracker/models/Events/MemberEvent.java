package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class MemberEvent extends Event {

    public MemberEvent(String id, EventType type, Actor actor, Repo repo, boolean isPublic, Date createdAt) {
        super(id, type, actor, repo, isPublic, createdAt);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String formatEventDetailsHtml() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'formatEventDetailsHtml'");
    }

}
