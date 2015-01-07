package com.packtpub.learningcypher.chapter1;

import org.neo4j.graphdb.Label;

/**
 * Labels used in our HR application
 * 
 * @author Onofrio Panzarino
 */
public enum HrLabels implements Label {
    
    /**
     * Employee
     */
    Employee,
    
    /**
     * Cost center
     */
    CostCenter
}
