package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class InfoFetcher {
    public static final String fetchInfo(String fullSpeciesName) {
        try {
            String speciesName = fullSpeciesName.trim();

            String matchUrl =
                "https://api.gbif.org/v1/species/match?name=" +
                URLEncoder.encode(speciesName, StandardCharsets.UTF_8);

            ProcessBuilder pb = new ProcessBuilder(
                    "curl", "-s",
                    "-A", "SpeciesApp/1.0",
                    matchUrl
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            StringBuilder json = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
            }

            process.waitFor();

            JSONObject obj = new JSONObject(json.toString());
            if (!obj.has("usageKey")) {
                return null;
            }
            int usageKey = obj.getInt("usageKey");

            String taxInfoUrl =
                "https://api.gbif.org/v1/species/" + usageKey;

            ProcessBuilder tpb = new ProcessBuilder(
                    "curl", "-s",
                    "-A", "SpeciesApp/1.0",
                    taxInfoUrl
            );
            tpb.redirectErrorStream(true);
            Process taxProcess = tpb.start();

            StringBuilder taxStringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(taxProcess.getInputStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    taxStringBuilder.append(line);
                }
            }

            taxProcess.waitFor();

            JSONObject taxObj = new JSONObject(taxStringBuilder.toString());
            if (!taxObj.has("scientificName")) {
                return null;
            }

            String commonNameUrl =
                "https://api.gbif.org/v1/species/" + usageKey + "/vernacularNames";

            ProcessBuilder vpb = new ProcessBuilder(
                    "curl", "-s",
                    "-A", "SpeciesApp/1.0",
                    commonNameUrl
            );
            vpb.redirectErrorStream(true);
            Process verProcess = vpb.start();


            StringBuilder verStringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(verProcess.getInputStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    verStringBuilder.append(line);
                }
            }

            verProcess.waitFor();

            String verString = verStringBuilder.toString().trim();

            JSONArray verArray;
            if (verString.startsWith("[")) {
                verArray = new JSONArray(verString);
            } else {
                verArray = new JSONArray();
            }

            String commonName = null;
            if (!verArray.isEmpty()) {
                JSONObject first = verArray.getJSONObject(0);
                commonName = first.optString("vernacularName", null);
            }

            taxObj.put("commonName", commonName);

            return taxObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fetchDescription(String speciesName) {
        try {
            String searchUrl = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" +
                                URLEncoder.encode(speciesName.trim(), StandardCharsets.UTF_8) +
                                "&format=json";

            ProcessBuilder pb = new ProcessBuilder("curl", "-s", "-A", "SpeciesApp/1.0", searchUrl);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            StringBuilder searchJson = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) searchJson.append(line);
            }
            process.waitFor();

            JSONObject searchObj = new JSONObject(searchJson.toString());
            JSONArray results = searchObj.getJSONObject("query").getJSONArray("search");
            if (results.isEmpty()) return null;

            String firstTitle = results.getJSONObject(0).getString("title");

            String wikiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/" +
                            URLEncoder.encode(firstTitle, StandardCharsets.UTF_8);

            ProcessBuilder summaryPb = new ProcessBuilder("curl", "-s", "-A", "SpeciesApp/1.0", wikiUrl);
            summaryPb.redirectErrorStream(true);
            Process summaryProcess = summaryPb.start();

            StringBuilder summaryJson = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(summaryProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) summaryJson.append(line);
            }
            summaryProcess.waitFor();

            JSONObject wikiObj = new JSONObject(summaryJson.toString());
            return wikiObj.optString("extract", null);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
