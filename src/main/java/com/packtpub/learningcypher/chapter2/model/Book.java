package com.packtpub.learningcypher.chapter2.model;

/**
 * A book
 * 
 * @author Onofrio Panzarino
 */
public interface Book {

    void addAuthor(Person author);

    Person[] getAuthors();

    String getTitle();

    void setTitle(String title);
    
    void setPages(int pages);
    
    int getPages();
    
    String[] getTags();
    
    void setTags(String[] tags);
    
    void setPublisher(Publisher publisher, int year);
    
    Publication getPublication();
}
