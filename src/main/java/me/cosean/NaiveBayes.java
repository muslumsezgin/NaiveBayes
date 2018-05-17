package me.cosean;

import me.cosean.model.News;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NaiveBayes {

    private Map<String, Double> probability = new HashMap<>();
    private Map<String, List<News>> dataMap;
    private Map<String, Map<String, Integer>> featureMap;
    private Map<String, Integer> totalFeatureMap;
    //TODO: bunu neden yaptık sor
    private int featureSize;

    public NaiveBayes(Map<String, List<News>> dataMap) {
        this.dataMap = dataMap;
        featureMap = new HashMap<>();
        totalFeatureMap = new HashMap<>();
    }

    public void learn() {
        dataMap.forEach((category, newsList) -> {
            featureMap.put(category, new HashMap<>());
            newsList.forEach(news -> news.getNgramMap().forEach((k, v) -> featureMap.get(category)
                    .merge(k, v, (v1, v2) -> v1 + v2)));
            totalFeatureMap.put(category, featureMap.get(category).values().stream().mapToInt(Number::intValue).sum());
        });

        HashSet<String> feature = new HashSet<>();
        featureMap.forEach((k, v) -> feature.addAll(v.keySet()));
        featureSize = feature.size();

        double tempDataSize = 0;
        for (List<News> value : dataMap.values()) tempDataSize += value.size();
        if (tempDataSize > 0) {
            double finalTempDataSize = tempDataSize;
            dataMap.forEach((key, value) -> probability.put(key, value.size() / finalTempDataSize));
        }
    }

    public void suggest(List<News> dataList) {
        dataList.forEach(news -> {
            Map<String, Double> possibilityMap = new HashMap<>();
            featureMap.forEach((category, featureList) -> {
                possibilityMap.put(category, probability.get(category));
                news.getNgramMap().forEach((featureName, featureSize) -> {
                    if (Objects.nonNull(featureList.get(featureName))) {
                        double featureSizeOfCategory = featureList.get(featureName) + 1;
                        double allFeatureSize = totalFeatureMap.get(category) + this.featureSize;
                        double possibilityOfBeginInCategory = featureSizeOfCategory / allFeatureSize;
                        double multiplier = possibilityMap.get(category) * possibilityOfBeginInCategory;
                        possibilityMap.put(category, multiplier);
                    }
                });
            });

//            Double max = Collections.max(possibilityMap.values());
//            String key = possibilityMap.entrySet().stream().filter(x -> x.getValue().equals(max)).collect(Collectors.toList()).get(0).getKey();
            String key = Collections.max(possibilityMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
            //String key = possibilityMap.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            news.setPredictedType(key);
        });
    }
}
