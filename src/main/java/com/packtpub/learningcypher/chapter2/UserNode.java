/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.packtpub.learningcypher.chapter2;

import com.packtpub.learningcypher.chapter2.model.Book;
import com.packtpub.learningcypher.chapter2.model.User;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * A user node
 *
 * @author Onofrio Panzarino
 */
public class UserNode implements User {
    private Node node;

    @Override
    public String getUsername() {
        return (String) node.getProperty("username");
    }

    @Override
    public boolean passwordMatch(String password) {
        return password.equals(node.getProperty("password"));
    }

    @Override
    public void vote(Book book, int score) {
        if(score <0 || score > 5) 
            throw new IllegalArgumentException("Score must be between 0 and 5");
        
        BookNode bn = (BookNode) book;
        Relationship r = node.createRelationshipTo(bn.node, BooksRelationship.Votes);
        r.setProperty("score", score);
    }
}
