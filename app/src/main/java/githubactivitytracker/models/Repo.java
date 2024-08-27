package githubactivitytracker.models;

import org.json.JSONObject;
import java.util.Set;

public record Repo(long id, String name, String url) {

    private static final Set<String> REQUIRED_JSON_KEYS = Set.of("id", "name", "url");

    public static Repo fromJSONObject(final JSONObject jo) {
        if (!jo.keySet().containsAll(REQUIRED_JSON_KEYS))
            throw new IllegalArgumentException("Repo JSON is missing required keys");

        return new Repo(jo.getLong("id"), jo.getString("name"), jo.getString("url"));
    }
}
