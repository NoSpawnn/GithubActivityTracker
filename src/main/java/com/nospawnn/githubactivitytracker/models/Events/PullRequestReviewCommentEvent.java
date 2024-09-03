package com.nospawnn.githubactivitytracker.models.Events;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.PullRequest;
import com.nospawnn.githubactivitytracker.models.Repo;
import org.json.JSONObject;

import java.util.Date;

public class PullRequestReviewCommentEvent extends Event {
    String action;
    PullRequest pullRequest;

    public PullRequestReviewCommentEvent(String id, Actor actor, Repo repo, boolean isPublic,  Date createdAt, JSONObject payload) {
        super(id, EventType.PullRequestReviewCommentEvent, actor, repo, isPublic, createdAt);
        this.action = payload.getString("action");
        this.pullRequest = PullRequest.fromJSONObject(payload.getJSONObject("pull_request"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return "<div class=\"row\"><div class=\"col\"><strong>Action:<br></strong>" + action + "</div>" +
                "<div class=\"col\"><strong>PR:<br></strong><a href=\"" + pullRequest.url() + "\">#" + pullRequest.number() + "</a></div></div>";
    }
}
