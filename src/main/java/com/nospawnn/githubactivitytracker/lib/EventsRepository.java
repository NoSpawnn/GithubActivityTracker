package com.nospawnn.githubactivitytracker.lib;

import com.nospawnn.githubactivitytracker.models.Events.Event;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EventsRepository {
    private static final ConcurrentHashMap<String, List<Event>> events = new ConcurrentHashMap<>();

    public static Optional<Event> getSingleEvent(String user, String eventId) {
        if (!events.containsKey(user))
            return Optional.empty();

        var thisEvents = events.get(user);

        for (var event : thisEvents)
            if (event.getId().equals(eventId))
                return Optional.of(event);

        return Optional.empty();
    }

    public static void addEventsForUser(String username, List<Event> newEvents) {
        if (events.containsKey(username))
            events.get(username).addAll(newEvents);
        else
            events.put(username, newEvents);
    }

    public static List<Event> getEventsForUser(String username, int page, int perPage) {
        var thisEvents = events.get(username);

        if (thisEvents == null)
            return null;

        var startIdx = page * perPage;
        if (startIdx >= thisEvents.size())
            return null;

        return thisEvents.subList(startIdx, thisEvents.size());
    }
}