package me.cosean;

import me.cosean.model.News;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NaiveBayes {

    private Map<String, Double> probability = new HashMap<>();
    private NGrams nGrams;
    private Double dataSize;

    public NaiveBayes(NGrams nGrams, Map<String, List<Object>> data) {
        this.nGrams = nGrams;
        double dataSize = 0;
        for (List<Object> value : data.values()) {
            dataSize += value.size();
        }
        this.dataSize = dataSize;
    }

    public Double getDataSize() {
        return dataSize;
    }

    public Map<String, Double> getProbability() {
        return probability;
    }

    public NGrams getnGrams() {
        return nGrams;
    }

    public void learn(Map<String, List<News>> data) {
        data.forEach((key, value) -> {
            probability.put(key, value.size() / dataSize);
        });
    }

    public void suggest(String filename, String context) {
        NGrams suggest_N_Grams = new NGrams();

    }
}
