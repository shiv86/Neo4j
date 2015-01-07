package com.packtpub.learningcypher.chapter2.model;

/**
 * A user
 * @author Onofrio Panzarino
 */
public interface User {
    
    String getUsername();
    
    boolean passwordMatch(String password);
    
    void vote(Book book, int score);
}
