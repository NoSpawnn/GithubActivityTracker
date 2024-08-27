package gat.models;

import java.util.Set;

import org.json.JSONObject;

public record Actor(
        long id,
        String login,
        String displayLogin,
        String gravatarId,
        String url,
        String avatarUrl) {

    private static final Set<String> REQUIRED_JSON_KEYS = Set.of(
            "id", "login", "display_login", "gravatar_id", "url", "avatar_url");

    public static Actor fromJSONObject(final JSONObject jo) {
        if (!jo.keySet().containsAll(REQUIRED_JSON_KEYS))
            throw new IllegalArgumentException("Actor JSON is missing required keys");

        return new Actor(
                jo.getLong("id"),
                jo.getString("login"),
                jo.getString("display_login"),
                jo.getString("gravatar_id"),
                jo.getString("url"),
                jo.getString("avatar_url"));
    }
}
