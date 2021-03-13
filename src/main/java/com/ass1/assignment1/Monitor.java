package com.ass1.assignment1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Hamado Dene
 */
public class Monitor {
    private final Lock lock;
    Map<String,Integer> words;
    private final int DEFAULT_WORD_COUNT = 1;
    private int numberWordProcessed = 0;
    
    public Monitor(){
        lock = new ReentrantLock();
        words = new HashMap<>();
    }
    
    //Update word occurences
    public void updateOccurences(String word){
        lock.lock();
        try {       
            //Update occurences counter
            if(words.containsKey(word)){
                int value = words.get(word);
                words.put(word, value++);
            } else {
                words.put(word, DEFAULT_WORD_COUNT);
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    //Update number of word processed
    public void updataNumberWordProcessed(){
        lock.lock();
        try {
            numberWordProcessed ++;
        } finally {
            lock.unlock();
        }       
    }
    
    //get number of word processed
    public int getNumberWordProcessed(){
        return this.numberWordProcessed;
    }
    
}
