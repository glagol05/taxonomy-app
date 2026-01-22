package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class ImageFetcher {

    public static String fetchStandardImage(String speciesName) {
        String title = speciesName.replace(" ", "_");
        String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=pageimages&piprop=original&titles=" + title;

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonText = new StringBuilder();
            while(scanner.hasNext()) jsonText.append(scanner.nextLine());
            scanner.close();

            JSONObject obj = new JSONObject(jsonText.toString());
            JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");
            for (String key : pages.keySet()) {
                JSONObject page = pages.getJSONObject(key);
                if (page.has("original")) {
                    return page.getJSONObject("original").getString("source");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
