package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Issue;
import com.nospawnn.githubactivitytracker.models.Repo;

public class IssueCommentEvent extends Event {
    String action;
    Issue issue;

    public IssueCommentEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt,
            JSONObject payload) {
        super(id, EventType.IssueCommentEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.issue = Issue.fromJSONObject(payload.getJSONObject("issue"));
    }

    public String formatEventDetailsHtml() {
        var sb = new StringBuilder();

        sb.append("<div class=\"row mt-3\">");
        sb.append("<div class=\"col align\"><strong>Title:<br></strong>" + issue.title() + "</div>");
        sb.append("<div class=\"col\"><strong>Issue:<br></strong><a href=\"" + issue.url() + "\">#" + issue.number()
                + "</a></div>");
        sb.append("</div>");
        sb.append("<div class=\"row mt-3\">");
        sb.append("<div class=\"col\"><strong>Total Comments:<br></strong>" + issue.commentCount() + "</div>");
        sb.append("</div>");

        return sb.toString();
    }
}
