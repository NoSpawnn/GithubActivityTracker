package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class CommitCommentEvent extends Event {
    String url;
    String commitId;

    public CommitCommentEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.CommitCommentEvent, actor, repo, isPublic, createdAt);
        this.url = payload.getString("html_url");
        this.commitId = payload.getString("commit_id");
    }

    @Override
    public String formatEventDetailsHtml() {
        var sb = new StringBuilder();

        sb.append("<div class=\"row mt-3\">");
        sb.append(
                "<div class=\"col align\"><strong>Commit:<br></strong><a href=\"" + url + "\">" + commitId
                        + "</a></div>");
        sb.append("</div>");

        return sb.toString();
    }

}
