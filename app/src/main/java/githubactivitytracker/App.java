package githubactivitytracker;

import java.util.List;

import githubactivitytracker.lib.GithubClient;
import githubactivitytracker.models.Event;

public class App {
    public static void main(String[] args) {
        var s = new GithubClient();
        List<Event> events;

        try {
            events = Event.listFromJSONArray(s.getEventsForUser("nospawnn"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (Event event : events) {
            System.out.println("- " + event.prettyString());
        }
    }
}
