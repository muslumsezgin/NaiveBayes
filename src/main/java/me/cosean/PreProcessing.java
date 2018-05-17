package me.cosean;

import me.cosean.model.News;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.WordAnalysis;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    public static Map<String, List<News>> execute(String path, String[] stopWords) throws IOException {
        Map<String, List<News>> result = new HashMap<>();
        File dirs = new File(path);
        if (dirs.isDirectory()) {
            TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
            for (File dir : Objects.requireNonNull(dirs.listFiles())) {
                ArrayList<News> newsList = new ArrayList<>();
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.isFile() && (file.getName().substring(file.getName().lastIndexOf('.') + 1).equals("txt"))) {
                        String text = new String(Files.readAllBytes(file.toPath()), Charset.forName("windows-1254"));
                        String[] split = text.toLowerCase().replaceAll("\\p{P}", " ").trim().split("\\s+");
                        StringBuilder newString = new StringBuilder();
                        for (String word : split) {
                            if (!Arrays.asList(stopWords).contains(word)) {
                                WordAnalysis analyze = morphology.analyze(word);
                                newString.append("_").append(analyze.analysisCount() > 0 ? analyze.getAnalysisResults().get(0).getStem() : word);
                            }
                        }
                        News news = new News(file.getName(), text, newString.toString(), dir.getName());
                        news.getNgramMap().putAll(NGrams.createNgramMap(2, news.getAnalyzeData()));
                        news.getNgramMap().putAll(NGrams.createNgramMap(3, news.getAnalyzeData()));
                        newsList.add(news);

                    }
                }
                result.put(dir.getName(), newsList);
            }
        } else {
            throw new IOException();
        }
        return result;
    }


}
