package com.nospawnn.githubactivitytracker.models;

import org.json.JSONObject;

import java.util.Set;

public record Repo(long id, String name, String url) {
    private static final Set<String> REPO_REQUIRED_JSON_KEYS = Set.of("id", "name", "url");
    private static final Set<String> FORKEE_REQUIRED_JSON_KEYS = Set.of("id", "name", "html_url");


    public static Repo fromJSONObject(final JSONObject jo, RepoType type) {
        switch (type) {
            case REPO -> {
                if (!jo.keySet().containsAll(REPO_REQUIRED_JSON_KEYS))
                    throw new IllegalArgumentException("Repo JSON is missing required keys");

                return new Repo(jo.getLong("id"), jo.getString("name"), jo.getString("url"));
            }
            case FORKEE -> {
                if (!jo.keySet().containsAll(FORKEE_REQUIRED_JSON_KEYS))
                    throw new IllegalArgumentException("Repo JSON is missing required keys");

                return new Repo(jo.getLong("id"), jo.getString("name"), jo.getString("html_url"));
            }
            default -> {
                return null;
            }
        }
    }
}
