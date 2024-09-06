package com.nospawnn.githubactivitytracker.lib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;

import org.json.JSONArray;

import com.nospawnn.githubactivitytracker.models.Events.Event;

public class GithubClient {
    private static final String EVENTS_URI_FMT = "https://api.github.com/users/%s/events?page=%d&per_page=%d";
    private static final String USER_AGENT_STR = "github.com/nospawnn";
    private static final String ACCEPT_TYPE = "application/vnd.github+json";
    private static final int DEFAULT_PER_PAGE = 20;
    private static final int MAX_TO_SEARCH = 1; // No. of pages to search for a given event


    private final HttpClient client = HttpClient.newHttpClient();

    public Event getSingleEvent(String user, String eventId) {
        var cachedEvent = EventsRepository.getSingleEvent(user, eventId);
        if (cachedEvent.isPresent())
            return cachedEvent.get();

        for (int i = 0; i < MAX_TO_SEARCH; i++) {
            List<Event> fetchedEvents = new ArrayList<>();

            try {
                fetchedEvents = getEventsForUser(user, i, DEFAULT_PER_PAGE);
            } catch (ParseException e) {
            }

            for (var event : fetchedEvents) {
                if (event.getId().equals(eventId))
                    return event;
            }
        }

        return null;
    }

    public List<Event> getEventsForUser(String username, int page) throws IllegalArgumentException, ParseException {
        return getEventsForUser(username, page, DEFAULT_PER_PAGE);
    }

    public List<Event> getEventsForUser(String username, int page, int perPage)
            throws IllegalArgumentException, ParseException {
        var eventsList = EventsRepository.getEventsForUser(username, page, perPage);
        if (eventsList != null)
            return eventsList;

        var request = buildRequest(String.format(EVENTS_URI_FMT, username, page, perPage));
        var eventsJson = client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(JSONArray::new)
                .join();

        eventsList = Event.listFromJSONArray(eventsJson);
        EventsRepository.addEventsForUser(username, eventsList);

        return eventsList;
    }

    private static HttpRequest buildRequest(String uri) throws IllegalArgumentException {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("User-Agent", USER_AGENT_STR)
                .header("Accept", ACCEPT_TYPE)
                .build();
    }
}
