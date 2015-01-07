package com.packtpub.learningcypher.chapter1;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * An employee in our simple HR management tool
 * 
 * @author Onofrio Panzarino
 */
public class Employee {

    public static final String NAME = "name";
    public static final String MIDDLE_NAME = "middleName";
    public static final String SURNAME = "surname";
    
    private final long id;
    private final String name, surname;

    /**
     * Creates an {@code Employee} from a {@code Node}
     * @param node Employee node
     * @return the created {@code Employee}
     */
    public static Employee fromNode(Node node) {
        try(Transaction t = node.getGraphDatabase().beginTx()) {
            Employee e = new Employee(
                node.getId(), 
                (String) node.getProperty("name"), 
                (String) node.getProperty("surname"));
            t.success();
            return e;
        }
    }

    /**
     * Initializes a new {@code Employee}
     * @param id ID of the employee
     * @param name Name of the employee
     * @param surname Surname of the employee
     */
    public Employee(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    /**
     * The ID of the employee
     * @return the ID of the employee
     */
    public long getId() {
        return id;
    }

    /**
     * The name of the employee
     * @return the name of the employee
     */
    public String getName() {
        return name;
    }

    /**
     * The surname of the employee
     * @return the surname of the employee
     */
    public String getSurname() {
        return surname;
    }
}
