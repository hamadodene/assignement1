package com.ass1.assignment1.interfaces;

import java.util.Map;

public interface Occurrences {
    public int  addOccurrence(String word);
    public int getNumberWordsProcessed();
    public void flushOccurrences();
    public Map<String, Integer> getOccurrences(int n);
    public Map<String,Integer> getAllWords();
}
