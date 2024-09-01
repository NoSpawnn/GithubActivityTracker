package com.nospawnn.githubactivitytracker.lib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.text.ParseException;

import org.json.JSONArray;

import com.nospawnn.githubactivitytracker.models.Events.Event;

public class GithubClient {
    private static final String EVENTS_URI_FMT = "https://api.github.com/users/%s/events?page=%d&per_page=%d";
    private static final String USER_AGENT_STR = "github.com/nospawnn";
    private static final String ACCEPT_TYPE = "application/vnd.github+json";
    private static final int DEFAULT_PER_PAGE = 20;

    private final HttpClient client = HttpClient.newHttpClient();

    public List<Event> getEventsForUser(String username) throws IllegalArgumentException, ParseException {
        return getEventsForUser(username, 1, DEFAULT_PER_PAGE);
    }

    public List<Event> getEventsForUser(String username, int page) throws IllegalArgumentException, ParseException {
        return getEventsForUser(username, page, DEFAULT_PER_PAGE);

    }

    public List<Event> getEventsForUser(String username, int page, int perPage)
            throws IllegalArgumentException, ParseException {
        var request = buildRequest(String.format(EVENTS_URI_FMT, username, page, perPage));
        var events = client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(JSONArray::new)
                .join();

        return Event.listFromJSONArray(events);
    }

    private static HttpRequest buildRequest(String uri) throws IllegalArgumentException {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("User-Agent", USER_AGENT_STR)
                .header("Accept", ACCEPT_TYPE)
                .build();
    }
}
