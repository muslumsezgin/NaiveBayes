package me.cosean;

import me.cosean.model.News;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.WordAnalysis;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class PreProcessing {

    public static String[] getStopWord() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path path = Paths.get(Objects.requireNonNull(classLoader.getResource("stopwords.txt")).toURI());
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).toLowerCase().trim().split("\r\n");
        } catch (IOException | URISyntaxException e) {
            System.out.println("Stop Words Dosyası Okunamadı!");
            return new String[0];
        }
    }

    public static HashMap<String, ArrayList<News>> readDirFiles(String path, String[] stopWords) throws IOException {
        HashMap<String, ArrayList<News>> result = new HashMap<>();
        File dirs = new File(path);
        if (dirs.isDirectory()) {
            TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
            for (File dir : Objects.requireNonNull(dirs.listFiles())) {
                ArrayList<News> news = new ArrayList<>();
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.isFile() && (file.getName().substring(file.getName().lastIndexOf('.') + 1).equals("txt"))) {
                        String text = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
                        String[] split = text.toLowerCase().replaceAll("\\p{P}", " ").trim().split("\\s+");
                        StringBuilder newString = new StringBuilder();
                        for (String word : split) {
                            if (!Arrays.asList(stopWords).contains(word)) {
                                WordAnalysis analyze = morphology.analyze(word);
                                newString.append(" ").append(analyze.analysisCount() > 0 ? analyze.getAnalysisResults().get(0).getStem() : word);
                            }
                        }
                        news.add(new News(file.getName(), text, newString.toString(), dir.getName()));
                    }
                }
                result.put(dir.getName(), news);
            }
        } else {
            throw new IOException();
        }
        return result;
    }


}
