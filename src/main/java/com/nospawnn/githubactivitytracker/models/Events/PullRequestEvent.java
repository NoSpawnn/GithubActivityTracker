package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class PullRequestEvent extends Event {
    String action;
    int number;

    public PullRequestEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.PullRequestEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.number = payload.getInt("number");
    }

    @Override
    public String formatEventDetailsHtml() {
        var sb = new StringBuilder();

        sb.append("<div class=\"row\"><div class=\"col\"><strong>Action:<br></strong>" + action + "</div>");
        sb.append("<div class=\"col\"><strong>PR:<br></strong>#" + number + "</div></div>");

        return sb.toString();
    }

}
