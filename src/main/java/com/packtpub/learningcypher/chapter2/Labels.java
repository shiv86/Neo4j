package com.packtpub.learningcypher.chapter2;

import org.neo4j.graphdb.Label;

/**
 * Labels used in this database
 * 
 * @author Onofrio Panzarino
 */
public enum Labels implements Label {
    Book,
    
    Person,
    
    Publisher;
}
