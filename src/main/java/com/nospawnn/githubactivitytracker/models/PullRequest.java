package com.nospawnn.githubactivitytracker.models;

import org.json.JSONObject;

public record PullRequest(String url, int number) {
    public static PullRequest fromJSONObject(JSONObject jo) {
        return new PullRequest(jo.getString("html_url"), jo.getInt("number"));
    }
}
