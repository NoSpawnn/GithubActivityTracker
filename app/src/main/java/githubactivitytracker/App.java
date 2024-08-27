package githubactivitytracker;

import java.util.List;

import githubactivitytracker.lib.GithubClient;
import githubactivitytracker.models.Event;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: " +
                    "\n    ./gradlew run --args <username>");
            return;
        }

        var s = new GithubClient();
        List<Event> events;

        try {
            events = Event.listFromJSONArray(s.getEventsForUser(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (Event event : events) {
            System.out.println("- " + event.prettyString());
        }
    }
}
