package com.packtpub.learningcypher.chapter2;

import com.packtpub.learningcypher.chapter2.model.Person;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * A person node
 * 
 * @author Onofrio Panzarino
 */
public class PersonNode implements Person {
    final Node node;
    
    public static final String Surname = "surname";
    public static final String Name = "name";

    public PersonNode(Node node) {
        this.node = node;
    }

    public static PersonNode copy(Person src, GraphDatabaseService graphdb) {
        Node node = graphdb.createNode();
        PersonNode pn = new PersonNode(node);
        pn.setName(src.getName());
        pn.setSurname(src.getSurname());
        return pn;
    }
    
    @Override
    public String getSurname() {
        return (String) node.getProperty(Surname);
    }
    
    @Override
    public void setSurname(String surname) {
        node.setProperty(Surname, surname);
    }
    
    @Override
    public String getName() {
        return (String) node.getProperty(Name);
    }
    
    @Override
    public void setName(String name) {
        node.setProperty(Name, name);
    }
}
