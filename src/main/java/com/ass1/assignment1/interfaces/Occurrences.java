package com.ass1.assignment1.interfaces;

import java.util.Map;

public interface Occurrences {
    public void  addOccurrence(String word,int n_occurrences);
    public int getNumberWordsProcessed();
    public Map<String, Integer> getOccurrences(int n);
    public Map<String,Integer> getAllWords();
}
