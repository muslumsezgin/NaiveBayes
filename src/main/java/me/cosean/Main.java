package me.cosean;

import me.cosean.model.News;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        String path = "C:\\Users\\Muslum\\Desktop\\1150haber\\raw_texts";

        String[] stopWords = PreProcessing.getStopWord();
        try {
            HashMap<String, ArrayList<News>> stringArrayListHashMap = PreProcessing.readDirFiles(path, stopWords);
            System.out.println(stringArrayListHashMap.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
