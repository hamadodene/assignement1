/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public Monitor(){
        lock = new ReentrantLock();
        words = new HashMap<>();
    }
    
    //
    public void update(String word){
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
}
