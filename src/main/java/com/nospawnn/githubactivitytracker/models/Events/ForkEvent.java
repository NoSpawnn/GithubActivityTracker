package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;

import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public class ForkEvent extends Event {
    Repo newRepo;

    public ForkEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.ForkEvent, actor, repo, isPublic, createdAt);
        this.newRepo = Repo.fromJSONObject(payload);
    }

    @Override
    public String formatEventDetailsHtml() {
        var sb = new StringBuilder();

        sb.append("<div class=\"row\"><div class=\"col\"><strong>Original Repo:<br></strong><a href=\"" + repo.url()
                + "\">" + repo.name() + "</a></div>");
        sb.append("<div class=\"col\"><strong>New Repo:<br></strong><a href=\"" + newRepo.url()
                + "\">" + actor.displayLogin().toLowerCase() + '/' + newRepo.name() + "</a></div></div>");

        return sb.toString();
    }
}
