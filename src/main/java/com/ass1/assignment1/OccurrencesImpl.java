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
    
    public OccurrencesImpl() {
        words = new HashMap<>();
    }

    /**
     *
     * @param word
     * @return number of words processed
     *
     * Update/Add occurrence into Map
     */
    @Override
    public void addOccurrence(String word, int n_occurrences) {
        if(words.containsKey(word)){
            int value = words.get(word);
            int newValue = value + n_occurrences;
            words.put(word, newValue);
        } else {
            words.put(word, n_occurrences);
        }
    }

    @Override
    public int getNumberWordsProcessed() {
        return words.size();
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

    /**
     *
     * @param wordCounts
     * @return an ordered map
     */
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

    //flush words Map
    @Override
    public void flushOccurrences() {
        words.clear();
    }
}
