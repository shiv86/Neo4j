package com.packtpub.learningcypher.chapter2;

import com.packtpub.learningcypher.chapter2.model.Publisher;
import org.neo4j.graphdb.Node;

/**
 * A publisher node
 * 
 * @author Onofrio Panzarino
 */
public class PublisherNode implements Publisher {

    final Node node;
    
    public static final String Name = "name";

    public PublisherNode(Node node) {
        this.node = node;
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
