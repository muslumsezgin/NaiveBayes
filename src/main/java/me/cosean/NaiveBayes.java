package me.cosean;

import me.cosean.model.News;
import me.cosean.model.SuccessModel;

import java.util.*;
import java.util.stream.Collectors;


public class NaiveBayes {

    private Map<String, Double> probability = new HashMap<>();
    private Map<String, List<News>> dataMap;
    private Map<String, Map<String, Integer>> featureMap;
    private Map<String, Integer> totalFeatureMap;
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
            featureMap.forEach((cat, features) -> featureMap.put(cat,
                    features.entrySet().stream().filter(x -> x.getValue() > 50)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
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

    public Map<String, SuccessModel> suggest(List<News> dataList) {
        dataList.forEach(news -> {
            Map<String, Double> possibilityMap = new HashMap<>();
            featureMap.forEach((category, featureList) -> {
                possibilityMap.put(category, Math.log(probability.get(category)));
                news.getNgramMap().forEach((featureName, featureSize) -> {
                    if (Objects.nonNull(featureList.get(featureName))) {
                        double featureSizeOfCategory = featureList.get(featureName) + 1;
                        double allFeatureSize = totalFeatureMap.get(category) + this.featureSize;
                        double possibilityOfBeginInCategory = featureSizeOfCategory / allFeatureSize;
                        double multiplier = possibilityMap.get(category) + Math.log(Math.pow(possibilityOfBeginInCategory, featureSize));
                        possibilityMap.put(category, multiplier);
                    }
                });
            });

            String predictedCat = Collections.max(possibilityMap.entrySet(),
                    Comparator.comparingDouble(Map.Entry::getValue)).getKey();
            news.setPredictedType(predictedCat);
        });
        Map<String, SuccessModel> successData = new HashMap<>();
        probability.forEach((category, v) -> successData.put(category, new SuccessModel(
                dataList.stream().filter(news -> news.getType().equals(category)
                        && news.getPredictedType().equals(category)).count(),
                dataList.stream().filter(news -> news.getType().equals(category)).count(),
                dataList.stream().filter(news -> news.getPredictedType().equals(category)).count()
        )));
        return successData;
    }
}
