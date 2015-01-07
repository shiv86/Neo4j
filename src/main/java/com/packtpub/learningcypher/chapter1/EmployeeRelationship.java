package com.packtpub.learningcypher.chapter1;

import org.neo4j.graphdb.RelationshipType;

/**
 * Relationships in our sample application
 *
 * @author Onofrio Panzarino
 */
public enum EmployeeRelationship implements RelationshipType {

    /**
     * An employee reports to his manager
     */
    REPORTS_TO,
    
    /**
     * An empoyee belongs to a center cost
     */
    BELONGS_TO,

    /**
     * An employee is the manager of a center cost
     */
    MANAGER_OF;
    
    /**
     * Property From
     */
    public static final String FROM = "from";
}
