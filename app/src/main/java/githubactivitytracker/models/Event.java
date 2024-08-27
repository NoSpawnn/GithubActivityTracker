package githubactivitytracker.models;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public record Event(
        String id,
        EventType type,
        Actor actor,
        Repo repo,
        JSONObject payload,
        boolean isPublic,
        Date createdAt) {

    private static final Set<String> REQUIRED_JSON_KEYS = Set.of(
            "id", "type", "actor", "repo", "payload", "public", "created_at");

    public static Event fromJSONObject(final JSONObject jo) throws ParseException {
        if (!jo.keySet().containsAll(REQUIRED_JSON_KEYS))
            throw new IllegalArgumentException("Event JSON is missing required keys");

        return new Event(
                jo.getString("id"),
                jo.getEnum(EventType.class, "type"),
                Actor.fromJSONObject(jo.getJSONObject("actor")),
                Repo.fromJSONObject(jo.getJSONObject("repo")),
                jo.getJSONObject("payload"),
                jo.getBoolean("public"),
                Date.from(Instant.parse(jo.getString("created_at")))); // ISO8601 format
    }

    public static List<Event> listFromJSONArray(final JSONArray ja) throws ParseException {
        var events = new ArrayList<Event>(ja.length());

        for (int i = 0; i < ja.length(); i++) {
            events.add(fromJSONObject(ja.getJSONObject(i)));
        }

        return events;
    }

    public String prettyString() {
        return switch (type) {
            case CommitCommentEvent ->
                "Commented on a commit in " + repo.name();
            case CreateEvent ->
                "Created a branch or tag in " + repo.name();
            case DeleteEvent ->
                "Deleted a branch or tag in " + repo.name();
            case ForkEvent ->
                "Forked " + repo.name();
            case GollumEvent ->
                "Created a wiki page for " + repo.name();
            case IssueCommentEvent ->
                "Commented on an issue in " + repo.name();
            case IssuesEvent ->
                titleString(payload.getString("action")) + " an issue in " + repo.name();
            case MemberEvent ->
                "Adder a member to " + repo.name();
            case PublicEvent ->
                "Made " + repo.name() + " public";
            case PullRequestEvent ->
                titleString(payload.getString("action")) + " a pull request in " + repo.name();
            case PullRequestReviewEvent ->
                "Created a PR review in " + repo.name();
            case PullRequestReviewCommentEvent ->
                "Created a comment on a PR in " + repo.name();
            case PullRequestReviewThreadEvent ->
                "Marked a PR thread as " + payload.getString("action") + " in " + repo.name();
            case PushEvent ->
                "Pushed " + payload.getJSONArray("commits").length() + " commits to " + repo.name();
            case ReleaseEvent ->
                "Published a release for " + repo.name();
            case SponsorshipEvent ->
                "Created a sponsorship listing for " + repo.name();
            case WatchEvent ->
                "Starred " + repo.name();
            default -> "";
        };
    }

    private static String titleString(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
