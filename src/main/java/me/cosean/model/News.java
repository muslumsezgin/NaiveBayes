package me.cosean.model;

import java.util.Objects;

public class News {
    private String name;
    private String data;
    private String analyzeData;
    private String type;

    public News(String name, String data, String analyzeData, String type) {
        this.name = name;
        this.data = data;
        this.analyzeData = analyzeData;
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(name, news.name) &&
                Objects.equals(data, news.data) &&
                Objects.equals(analyzeData, news.analyzeData) &&
                Objects.equals(type, news.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data, analyzeData, type);
    }
}
