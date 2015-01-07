package com.packtpub.learningcypher.chapter2;

import com.packtpub.learningcypher.chapter2.model.*;
import java.util.LinkedList;
import org.neo4j.graphdb.*;

/**
 * A book node
 * 
 * @author Onofrio Panzarino
 */
public class BookNode implements Book {

    final Node node;
    public static final String Title = "title";
    public static final String Pages = "pages";
    public static final String Tags = "tags";

    public BookNode(Node node) {
        this.node = node;
    }

    public long getId() {
        return node.getId();
    }
    
    @Override
    public String getTitle() {
        return (String) node.getProperty(Title);
    }

    @Override
    public void setTitle(String title) {
        node.setProperty(Title, title);
    }

    private PersonNode asNode(Person person) {
        try {
            return (PersonNode) person;
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Expected PersonNode instance", cce);
        }
    }

    @Override
    public void addAuthor(Person author) {
        PersonNode p = asNode(author);
        p.node.createRelationshipTo(node, BooksRelationship.AuthorOf);
    }

    @Override
    public Person[] getAuthors() {
        Iterable<Relationship> iterables = node.getRelationships(BooksRelationship.AuthorOf);
        LinkedList<PersonNode> authors = new LinkedList<>();

        for (Relationship r : iterables) {
            Node n = r.getEndNode();
            PersonNode pn = new PersonNode(n);
            authors.add(pn);
        }
        return authors.toArray(new Person[authors.size()]);
    }

    @Override
    public void setPages(int pages) {
        node.setProperty(Pages, pages);
    }

    @Override
    public int getPages() {
        return (int) node.getProperty(Pages);
    }

    private PublisherNode asNode(Publisher publisher) {
        try {
            return (PublisherNode) publisher;
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Expected Publisher node", cce);
        }
    }

    @Override
    public void setPublisher(Publisher publisher, int year) {
        PublisherNode pn = asNode(publisher);

        Iterable<Relationship> published = node.getRelationships(BooksRelationship.PublishedBy);
        for (Relationship r : published) {
            r.delete();
        }

        Relationship newRel = node.createRelationshipTo(pn.node, BooksRelationship.PublishedBy);
        newRel.setProperty(PublicationRelation.Year, year);
    }

    @Override
    public Publication getPublication() {
        Iterable<Relationship> published = node.getRelationships(BooksRelationship.PublishedBy);
        for (Relationship r : published) {
            return new PublicationRelation(r);
        }
        return null;
    }

    @Override
    public String[] getTags() {
        return (String[]) node.getProperty(Tags);
    }

    @Override
    public void setTags(String[] tags) {
        node.setProperty(Tags, tags);
    }

    public static class PublicationRelation implements Publication {
        public static final String Year = "year";
        
        private final Relationship relationShip;

        public PublicationRelation(Relationship r) {
            relationShip = r;
        }
        
        @Override
        public int getYear() {
            return (int) relationShip.getProperty(Year);
        }
        
        @Override
        public Publisher getPublisher() {
            return new PublisherNode(relationShip.getEndNode());
        }
    }
}
