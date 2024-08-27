package githubactivitytracker.lib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONArray;

public class GithubClient {
    private static final String EVENTS_URI_FMT = "https://api.github.com/users/%s/events";
    private static final String USER_AGENT_STR = "github.com/nospawnn";
    private static final String ACCEPT_TYPE = "application/vnd.github+json";

    private HttpClient client = HttpClient.newHttpClient();

    public JSONArray getEventsForUser(String username) throws IllegalArgumentException {
        var request = buildRequest(String.format(EVENTS_URI_FMT, username));
        return client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(JSONArray::new)
                .join();
    }

    private static HttpRequest buildRequest(String uri) throws IllegalArgumentException {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("User-Agent", USER_AGENT_STR)
                .header("Accept", ACCEPT_TYPE)
                .build();
    }
}
