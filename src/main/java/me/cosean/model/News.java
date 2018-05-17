package me.cosean.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class News {
    private String name;
    private String data;
    private String analyzeData;
    private String type;
    private String predictedType;
    private Map<String, Integer> ngramMap;

    public News(String name, String data, String analyzeData, String type) {
        this.name = name;
        this.data = data;
        this.analyzeData = analyzeData;
        this.type = type;
        this.ngramMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAnalyzeData() {
        return analyzeData;
    }

    public void setAnalyzeData(String analyzeData) {
        this.analyzeData = analyzeData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Map<String, Integer> getNgramMap() {
        return ngramMap;
    }

    public void setNgramMap(Map<String, Integer> ngramMap) {
        this.ngramMap = ngramMap;
    }

    public String getPredictedType() {
        return predictedType;
    }

    public void setPredictedType(String predictedType) {
        this.predictedType = predictedType;
    }


    @Override
    public String toString() {
        return "News{" +"\n" +
                "name===>'" + name + '\'' + "\n" +
//                "data===>'" + data + '\'' + "\n" +
                "analyzeData===>'" + analyzeData + '\'' + "\n" +
                "type===>'" + type + '\'' + "\n" +
                "predictedType===>'" + predictedType + '\'' + "\n" +
                "ngramMap===>" + ngramMap.size() + "\n" +
                '}';
    }
}
