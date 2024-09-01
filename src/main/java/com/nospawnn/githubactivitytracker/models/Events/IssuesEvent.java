package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Issue;
import com.nospawnn.githubactivitytracker.models.Repo;

public class IssuesEvent extends Event {
    String action;
    Issue issue;

    public IssuesEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.IssuesEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.issue = Issue.fromJSONObject(payload);
    }

    @Override
    public String formatEventDetailsHtml() {
        return "<div class=\"row\"><div class=\"col\"><strong>Action:<br></strong>"
                + action
                + "</div><div class=\"col\"><strong>Issue:<br></strong><a href=\"" + issue.url()
                + "\">"
                + issue.title() + "</a></div></div>";
    }
}
