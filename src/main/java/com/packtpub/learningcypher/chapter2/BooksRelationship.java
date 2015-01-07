package com.packtpub.learningcypher.chapter2;

import org.neo4j.graphdb.RelationshipType;

/**
 * Relationships with books
 * 
 * @author Onofrio Panzarino
 */
public enum BooksRelationship implements RelationshipType {
    /**
     * Authorship
     */
    AuthorOf,
    
    /**
     * A person is the translator of a book
     */
    TranslatorOf,
    
    /**
     * A book is referenced in another book
     */
    ReferencedIn,

    /**
     * A book was published
     */
    PublishedBy,
    
    /**
     * A user voted a book
     */
    Votes
}
