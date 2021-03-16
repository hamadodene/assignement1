package com.ass1.assignment1;

import com.ass1.assignment1.interfaces.Occurrences;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 *
 * @author Hamado Dene
 */
public class OccurrencesImpl implements Occurrences {
    
    private final Map<String, Integer> words;
    private final int DEFAULT_WORD_COUNT = 1;
    private int numberOfWordProcessed = 0;
    private final OccurrencesImpl instance = new OccurrencesImpl();

    public OccurrencesImpl() {
        words = new HashMap<>();
    }

    @Override
    public int  addOccurrence(String word) {
        if(words.containsKey(word)){
            int value = words.get(word);
            words.put(word, value + 1);
        } else {
            words.put(word, DEFAULT_WORD_COUNT);
            numberOfWordProcessed++;
        }
        return numberOfWordProcessed;
    }

    @Override
    public int getNumberWordsProcessed() {
        return numberOfWordProcessed;
    }

    @Override
    public Map<String, Integer> getOccurrences(int n) {
        if(words.isEmpty()) {
            return null;
        }
        //Sorted map from value
        final Map<String, Integer> sortedByValue = sortByValue(words);
        
        return sortedByValue.entrySet().stream()
               .limit(n)
               .collect(Collectors.toMap(Entry::getKey, Entry::getValue));        
    }
    
    private  static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public Map<String,Integer> getAllWords() {
        return words;
    }

}
