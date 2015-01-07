package com.packtpub.learningcypher.chapter2.model;

/**
 * A publication
 * 
 * @author Onofrio Panzarino
 */
public interface Publication {
    int getYear();
    
    Publisher getPublisher();
}
