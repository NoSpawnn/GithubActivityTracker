package com.nospawnn.githubactivitytracker.models.Events;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nospawnn.githubactivitytracker.models.Actor;
import com.nospawnn.githubactivitytracker.models.EventType;
import com.nospawnn.githubactivitytracker.models.Repo;

public abstract class Event {
    String id;
    EventType type;
    Actor actor;
    Repo repo;
    boolean isPublic;
    Date createdAt;

    private static final Set<String> REQUIRED_JSON_KEYS = Set.of(
            "id", "type", "actor", "repo", "payload", "public", "created_at");

    public Event(String id, EventType type, Actor actor, Repo repo, boolean isPublic, Date createdAt) {
        this.id = id;
        this.type = type;
        this.actor = actor;
        this.repo = repo;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

    public static Event fromJSONObject(final JSONObject jo) throws ParseException {
        if (!jo.keySet().containsAll(REQUIRED_JSON_KEYS))
            throw new IllegalArgumentException("Event JSON is missing required keys");

        var id = jo.getString("id");
        var actor = Actor.fromJSONObject(jo.getJSONObject("actor"));
        var repo = Repo.fromJSONObject(jo.getJSONObject("repo"));
        var isPublic = jo.getBoolean("public");
        var createdAt = Date.from(Instant.parse(jo.getString("created_at"))); // ISO8601 format
        var payload = jo.getJSONObject("payload");
        var type = jo.getEnum(EventType.class, "type");

        return switch (type) {
            case CommitCommentEvent ->
                new CommitCommentEvent(id, actor, repo, isPublic, createdAt, payload);
            case IssueCommentEvent ->
                new IssueCommentEvent(id, actor, repo, isPublic, createdAt, payload);
            case CreateEvent -> new CreateEvent(id, actor, repo, isPublic, createdAt, payload);
            case DeleteEvent -> new DeleteEvent(id, actor, repo, isPublic, createdAt, payload);
            case ForkEvent -> new ForkEvent(id, actor, repo, isPublic, createdAt, payload.getJSONObject("forkee"));
            case GollumEvent -> new GollumEvent(id, actor, repo, isPublic, createdAt, payload);
            case IssuesEvent -> new IssuesEvent(id, actor, repo, isPublic, createdAt, payload);
            // case MemberEvent -> new MemberEvent(id, actor, repo, isPublic, createdAt, payload);
            case PublicEvent -> new PublicEvent(id, actor, repo, isPublic, createdAt);
            case PullRequestEvent -> new PullRequestEvent(id, actor, repo, isPublic, createdAt, payload);
            // case PullRequestReviewCommentEvent ->
            // new PullRequestReviewCommentEvent(id, actor, repo, isPublic, createdAt, payload);
            // case PullRequestReviewEvent -> new PullRequestReviewEvent(id, actor, repo, isPublic, createdAt, payload);
            // case PullRequestReviewThreadEvent -> new PullRequestReviewThreadEvent(id, actor, repo, isPublic, createdAt, payload);
            case PushEvent -> new PushEvent(id, actor, repo, isPublic, createdAt, payload);
            // case ReleaseEvent -> new ReleaseEvent(id, actor, repo, isPublic, createdAt, payload);
            // case SponsorshipEvent -> new SponsorshipEvent(id, actor, repo, isPublic, createdAt, payload);
            // case WatchEvent -> new WatchEvent(id, actor, repo, isPublic, createdAt, payload);
            default -> null;
        };
    }

    public static List<Event> listFromJSONArray(final JSONArray ja) throws ParseException {
        var events = new ArrayList<Event>(ja.length());

        for (int i = 0; i < ja.length(); i++)
            events.add(fromJSONObject(ja.getJSONObject(i)));

        return events;
    }

    public abstract String formatEventDetailsHtml();

    public String getId() {
        return id;
    }

    public EventType getType() {
        return type;
    }

    public Actor getActor() {
        return actor;
    }

    public Repo getRepo() {
        return repo;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public static Set<String> getRequiredJsonKeys() {
        return REQUIRED_JSON_KEYS;
    }

    // public String prettyString() {
    // return switch (type) {
    // case CommitCommentEvent ->
    // "Commented on a commit in " + repo.name();
    // case CreateEvent ->
    // "Created a branch or tag in " + repo.name();
    // case DeleteEvent ->
    // "Deleted a branch or tag in " + repo.name();
    // case ForkEvent ->
    // "Forked " + repo.name();
    // case GollumEvent ->
    // "Created a wiki page for " + repo.name();
    // case IssueCommentEvent ->
    // "Commented on an issue in " + repo.name();
    // case IssuesEvent ->
    // titleString(payload.getString("action")) + " an issue in " + repo.name();
    // case MemberEvent ->
    // "Adder a member to " + repo.name();
    // case PublicEvent ->
    // "Made " + repo.name() + " public";
    // case PullRequestEvent ->
    // titleString(payload.getString("action")) + " a pull request in " +
    // repo.name();
    // case PullRequestReviewEvent ->
    // "Created a PR review in " + repo.name();
    // case PullRequestReviewCommentEvent ->
    // "Created a comment on a PR in " + repo.name();
    // case PullRequestReviewThreadEvent ->
    // "Marked a PR thread as " + payload.getString("action") + " in " +
    // repo.name();
    // case PushEvent ->
    // "Pushed " + payload.getJSONArray("commits").length() + " commits to " +
    // repo.name();
    // case ReleaseEvent ->
    // "Published a release for " + repo.name();
    // case SponsorshipEvent ->
    // "Created a sponsorship listing for " + repo.name();
    // case WatchEvent ->
    // "Starred " + repo.name();
    // default -> "";
    // };
    // }
}
