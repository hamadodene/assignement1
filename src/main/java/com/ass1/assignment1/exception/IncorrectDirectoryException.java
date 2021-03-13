/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ass1.assignment1.exception;

/**
 *
 * @author hamado
 */
public class IncorrectDirectoryException extends Exception{
    
    public IncorrectDirectoryException(String errorMessage) {
        super(errorMessage);
    }
}
