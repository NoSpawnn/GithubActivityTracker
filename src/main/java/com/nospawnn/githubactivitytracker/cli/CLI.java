package com.nospawnn.githubactivitytracker.cli;

import java.util.List;

import com.nospawnn.githubactivitytracker.lib.GithubClient;
import com.nospawnn.githubactivitytracker.models.Event;

public class CLI {
    public void runForUsername(String username) {
        var github = new GithubClient();
        List<Event> events;

        try {
            events = github.getEventsForUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (events.isEmpty())
            System.out.println("User '" + username + "' has no events (or they don't exist!)");
        else
            for (Event event : events)
                System.out.println("- " + event.prettyString());

    }
}
