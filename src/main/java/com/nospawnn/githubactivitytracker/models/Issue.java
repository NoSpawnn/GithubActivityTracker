package com.nospawnn.githubactivitytracker.models;

import java.util.Set;

import org.json.JSONObject;

public record Issue(String url, String repoUrl, String title, int number, int commentCount) {

    private static final Set<String> REQUIRED_JSON_KEYS = Set.of(
            "html_url", "repository_url", "title", "number", "comments");

    public static Issue fromJSONObject(JSONObject jo) {
        if (!jo.keySet().containsAll(REQUIRED_JSON_KEYS))
            throw new IllegalArgumentException("Issue JSON is missing required keys");

        return new Issue(jo.getString("html_url"), jo.getString("repository_url"), jo.getString("title"),
                jo.getInt("number"), jo.getInt("comments"));
    }
}
