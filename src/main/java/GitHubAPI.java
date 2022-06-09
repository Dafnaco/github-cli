import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GitHubAPI {

    public static HttpResponse<String> verifyValidResponse(HttpResponse<String> r) throws Exception {
        if (r.statusCode() == 404) {
            throw new Exception("Could not find the wanted repository");
        }

        if (r.statusCode() >= 500) {
            throw new Exception("Github had an internal error");
        }

        if (r.statusCode() >= 300) {
            throw new Exception("Unknown error");
        }

        return r;
    }
    public static JsonArray getAggregatedDownloads(String repo) throws Exception {

        URI uri = URI.create(String.format("https://api.github.com/repos/%s/releases", repo));
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        GitHubAPI.verifyValidResponse(response);
        return new Gson().fromJson(response.body(), JsonArray.class);
    }


    public static JsonObject getRepoStats(String repo) throws Exception {

        URI uri = URI.create((String.format("https://api.github.com/repos/%s", repo)));
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .build();
        var response =  HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        GitHubAPI.verifyValidResponse(response);
        return new Gson().fromJson(response.body(), JsonObject.class);
    }

    public static JsonArray getRepoContributors(String repo) throws Exception {
        URI uri = URI.create((String.format("https://api.github.com/repos/%s/contributors", repo)));
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        GitHubAPI.verifyValidResponse(response);
        return new Gson().fromJson(response.body(), JsonArray.class);
    }
}
