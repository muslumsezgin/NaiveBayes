package me.cosean;

import me.cosean.model.News;
import me.cosean.model.SuccessModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        //String path = "C:\\Users\\anil\\Desktop\\Deneme";
        String path = "C:\\Users\\anil\\Desktop\\1150haber\\raw_texts";

        String[] stopWords = PreProcessing.getStopWord();
        try {
            Map<String, List<News>> stringArrayListHashMap = PreProcessing.execute(path, stopWords);
            Map<String, List<News>> trainMap = new HashMap<>();
            List<News> testList = new ArrayList<>();

            stringArrayListHashMap.forEach((category, newsList) -> {
                Collections.shuffle(newsList);
                int v = (int) (newsList.size() * 0.75);
                trainMap.put(category, newsList.subList(0, v));
                List<News> filterList = newsList.subList(v, newsList.size()).stream()
                        .filter(k -> k.getNgramMap().size() > 0).collect(Collectors.toList());
                testList.addAll(filterList);
            });

            NaiveBayes naiveBayes = new NaiveBayes(trainMap);
            naiveBayes.learn();
            Map<String, SuccessModel> suggestSuccessMap = naiveBayes.suggest(testList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}




