package me.cosean;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NGrams {

    public static HashMap<String, Integer> createNgramMap(int n, String str) {
        HashMap<String, Integer> ngrams = new HashMap<>();
        for (int i = 0; i < str.length() - n + 1; i++) {
            String nGramData = str.substring(i, i + n);
            if (Objects.nonNull(ngrams.get(nGramData)))
                ngrams.put(nGramData, ngrams.get(nGramData) + 1);
            else{
                ngrams.put(nGramData, 1);
            }

        }

        return ngrams;
    }


}
