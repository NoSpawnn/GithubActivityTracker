package com.nospawnn.githubactivitytracker.models.Events;

import java.util.Date;
import java.util.Map;

import com.nospawnn.githubactivitytracker.models.RepoType;
import com.renomad.minum.templating.TemplateProcessor;
import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

import static com.nospawnn.githubactivitytracker.web.Page.fileUtils;
import static com.nospawnn.githubactivitytracker.web.PathRegister.TEMPLATE_DIR;

public class ForkEvent extends Event {
    Repo newRepo;

    public ForkEvent(String id, Actor actor, Repo repo, boolean isPublic, Date createdAt, JSONObject payload) {
        super(id, EventType.ForkEvent, actor, repo, isPublic, createdAt);
        this.newRepo = Repo.fromJSONObject(payload, RepoType.FORKEE);
        this.htmlTemplate = TemplateProcessor.buildProcessor(fileUtils.readTextFile(TEMPLATE_DIR + "/events/ForkEvent.html"));
    }

    @Override
    public String formatEventDetailsHtml() {
        return htmlTemplate.renderTemplate(Map.of(
                "oldUrl", repo.url(),
                "oldName", repo.name(),
                "newUrl", newRepo.url(),
                "newName", actor.displayLogin().toLowerCase() + "/" + newRepo.name()));
    }
}
