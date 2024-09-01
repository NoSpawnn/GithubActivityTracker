package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class PushEvent extends Event {
    int commitCount;
    int distinctCommitCount;
    String head;

    public PushEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.PushEvent, actor, repo, isPublic, createdAt);
        this.commitCount = payload.getInt("size");
        this.distinctCommitCount = payload.getInt("distinct_size");
        this.head = payload.getString("head");
    }

    @Override
    public String formatEventDetailsHtml() {
        var sb = new StringBuilder();

        sb.append("<div class=\"row\"><div class=\"col\"><strong>Commits:<br></strong>" + commitCount + " ("
                + distinctCommitCount + " distinct)</div>");
        sb.append("<div class=\"col\"><strong>Head:<br></strong>" + head + "</div></div>");

        return sb.toString();
    }

}
