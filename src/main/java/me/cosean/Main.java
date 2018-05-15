package me.cosean;

import me.cosean.model.News;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        String path = "C:\\Users\\anil\\Desktop\\1150haber\\raw_texts";

        String[] stopWords = PreProcessing.getStopWord();
        try {
            HashMap<String, ArrayList<News>> stringArrayListHashMap = PreProcessing.execute(path, stopWords);
            System.out.println(stringArrayListHashMap.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}




