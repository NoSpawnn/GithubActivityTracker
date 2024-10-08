package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;
import org.json.JSONObject;

public class MemberEvent extends Event {

    public MemberEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.MemberEvent, actor, repo, isPublic, createdAt);
    }

    @Override
    public String formatEventDetailsHtml() {
        return "";
    }

}
