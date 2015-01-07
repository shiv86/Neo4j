package com.packtpub.learningcypher.chapter2;

import com.packtpub.learningcypher.chapter2.model.Book;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import junit.framework.TestCase;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * Repository tests
 * 
 * @author Onofrio Panzarino
 */
public class BookRepositoryTest extends TestCase {

    public BookRepositoryTest(String testName) {
        super(testName);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        try (Transaction tx = graphDb.beginTx()) {
            final URL url = App.class.getResource("books_create.txt");
            final Path resPath = Paths.get(url.toURI());

            final ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);

            final String script = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
            engine.execute(script);
            engine.execute("MATCH (b:Book) WITH b "
                    + "CREATE (b) <-[:Votes{score: 4 }]- (:User)");
            
            tx.success();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (graphDb != null) {
            graphDb.shutdown();
            graphDb = null;
        }
    }

    private GraphDatabaseService graphDb;

    public void testFindShouldReturnCorrectNumberWhenInvoked() {
        final ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);
        try (final Transaction t = graphDb.beginTx()) {
            final BookRepository repo = new BookRepository(engine);
            final List<Book> books = repo.find("tales", 10, 0);

            assertNotNull(books);
            assertEquals(3, books.size());
            
            t.success();
        }
    }

    public void testFindByYearShouldReturnCorrectNumberWhenInvoked() {
        final ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);

        try (final Transaction t = graphDb.beginTx()) {
            final BookRepository repo = new BookRepository(engine);
            final List<Book> books = repo.findByTitleAfterYear("Memcached", 2013);

            
            assertNotNull(books);
            assertEquals(1, books.size());
            
            t.success();
        }
    }

    public void testFindByTagShouldReturnCorrectNumberWhenInvoked() {
        final ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);

        try (final Transaction t = graphDb.beginTx()) {
            final BookRepository repo = new BookRepository(engine);
            final List<Book> books = repo.findByTag("drama");

            assertNotNull(books);
            assertEquals(44, books.size());
            t.success();
        }
    }

    public void testFindByTagsShouldReturnCorrectNumberWhenInvoked() {
        final ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);

        try (final Transaction t = graphDb.beginTx()) {
            final BookRepository repo = new BookRepository(engine);
            final List<Book> books = repo.findByTags("neo4j", "nosql");

            assertNotNull(books);
            assertEquals(2, books.size());
            t.success();
        }
    }

    public void testFindExactTitle() {
        final ExecutionEngine engine = new ExecutionEngine(graphDb);

        try (final Transaction t = graphDb.beginTx()) {
            final BookRepository repo = new BookRepository(engine);
            final List<Book> books = repo.findExactTitle("War and Peace");

            assertNotNull(books);
            assertEquals(1, books.size());
            t.success();
        }
    }
    
    public void testScoreValues() {
        final ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);

        try (final Transaction t = graphDb.beginTx()) {
            final BookRepository repo = new BookRepository(engine);
            final List<Book> books = repo.find("Learning", 1, 0);
            assertNotNull(books);

            for (Book b : books) {
                Double score = repo.getScore(b);
                assertEquals(4.0, score);
            }

            t.success();
        }
    }
}
