package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;

public class ImageFetcher {

    private static final String IMAGE_DIR = "species_images";

    public static String fetchAndSaveImage(String fullSpeciesName) {
        try {
            File folder = new File(IMAGE_DIR);
            if (!folder.exists()) folder.mkdirs();

            String title = fullSpeciesName.trim().replace(" ", "_");

            String apiUrl =
                "https://en.wikipedia.org/api/rest_v1/page/summary/" + title;

            ProcessBuilder pb = new ProcessBuilder(
                    "curl", "-s",
                    "-A", "SpeciesApp/1.0",
                    apiUrl
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            StringBuilder jsonText = new StringBuilder();
            try (Scanner sc = new Scanner(process.getInputStream())) {
                while (sc.hasNextLine()) jsonText.append(sc.nextLine());
            }

            JSONObject obj = new JSONObject(jsonText.toString());
            if (!obj.has("thumbnail")) return null;

            String imageUrl = obj
                    .getJSONObject("thumbnail")
                    .getString("source");

            String safeName =
                fullSpeciesName.replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".jpg";
            File outFile = new File(folder, safeName);

            ProcessBuilder downloadPb = new ProcessBuilder(
                    "curl", "-L", "-s",
                    "-A", "SpeciesApp/1.0",
                    "-o", outFile.getAbsolutePath(),
                    imageUrl
            );
            downloadPb.redirectErrorStream(true);
            Process download = downloadPb.start();
            download.waitFor();

            if (!outFile.exists() || outFile.length() < 10_000) {
                outFile.delete();
                return null;
            }

            return outFile.getAbsolutePath();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
