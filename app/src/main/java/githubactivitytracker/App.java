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

        var userName = args[0];
        var github = new GithubClient();
        List<Event> events;

        try {
            events = Event.listFromJSONArray(github.getEventsForUser(userName));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (events.isEmpty())
            System.out.println("User '" + userName + "' has no events (or they don't exist!)");
        else
            for (Event event : events)
                System.out.println("- " + event.prettyString());

    }
}
