package com.packtpub.learningcypher.chapter2;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.kernel.impl.util.StringLogger;
import scala.Console;

/**
 * Queries from the book dumped to Console
 * 
 * @author Onofrio Panzarino
 */
public class App {

    private static ExecutionEngine engine;

    public static void main(String[] args) throws Exception {
        final String path = "db/books";

        final File dbDirectory = new File(path);
        final boolean exists = dbDirectory.exists();

        if (exists) {
            FileUtils.deleteRecursively(dbDirectory);
        }
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path);

        engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);

        if (!false) {
            Console.out().println("Creating nodes...");

            URL url = App.class.getResource("books_create.txt");
            Path resPath = Paths.get(url.toURI());

            String script = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
            engine.execute(script);
            
            final String randomVotes
                    = "MATCH (b:Book) WITH b "
                    + "CREATE (b) <-[:Votes{score: round(rand()*3) + 2 }]- (u1:User)"
                    + "CREATE (b) <-[:Votes{score: round(rand()*3) + 2 }]- (u2:User)"
                    + "CREATE (b) <-[:Votes{score: round(rand()*4) + 1 }]- (u3:User)"
                    + "CREATE (b) <-[:Votes{score: round(rand()*3) + 1 }]- (u4:User)"
                    + "CREATE (b) <-[:Votes{score: round(rand()*4) + 1 }]- (u5:User)"; 
           engine.execute(randomVotes);
           
           final String bookWithoutVotes =
                   "CREATE (:Book {title:\"The Art of Prolog\",tags:[\"prolog\"]})";
           engine.execute(bookWithoutVotes);
        }

        dump("MATCH (b:Book { title: 'In Search of Lost Time' }) RETURN b");
        dump("MATCH (b:Book) WHERE b.title = 'In Search of Lost Time' RETURN b");
        dump("MATCH (b:Book) WHERE b.title =~ '.*Lost.*' RETURN b");
        dump("MATCH (b:Book) WHERE b.title =~ '^Henry.*' RETURN b");
        dump("MATCH (b:Book) WHERE b.title =~ '.*[Tt]ale(s)?.*' RETURN b");
        dump("MATCH (b:Book) WHERE b.title =~ '(?i).*tale(s)?.*' RETURN b");
        dump("MATCH (book:Book) -[r:PublishedBy]-> (:Publisher) WHERE r.year >= 2012 RETURN book.title, r.year");

        dump("MATCH (b:Book) -[r:PublishedBy]-> (:Publisher) WHERE r.year IN [2012, 2013] RETURN b.title, r.year");
        
        dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag IN ['nosql','neo4j'] ) RETURN b.title,b.tags");
        dump("MATCH (b:Book) WHERE ALL ( tag IN b.tags WHERE tag IN ['nosql','neo4j'] ) RETURN b.title,b.tags");
        dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag = 'nosql' ) "
                + "AND NONE (tag in b.tags WHERE tag = 'neo4j') RETURN b.title,b.tags");
        dump("MATCH (b:Book) WHERE SINGLE (tag IN b.tags WHERE tag IN ['ruby', 'mongodb'] ) RETURN b.title,b.tags");

        // LIMIT and SKIP
        dump("MATCH (b:Book) WHERE b.title =~ '(?i).*drama.*' RETURN b LIMIT 20");
        dump("MATCH (b:Book) WHERE b.title =~ '(?i).*drama.*' RETURN b SKIP 20 LIMIT 20");
        
        // SORTING
        dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag IN ['drama'] ) RETURN b.title ORDER BY b.title LIMIT 5");
        dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag IN ['drama'] ) RETURN b.title ORDER BY b.title DESC LIMIT 5");
        
        dump("MATCH (b:Book) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN b.title, p.year ORDER BY p.year DESC LIMIT 5");
        dump("MATCH (b:Book) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN b.title, p.year ORDER BY COALESCE(p.year,-5000) DESC LIMIT 5");
        
        dump("START b=node(5) MATCH (b:Book) <-[r:Votes]- (:User) RETURN COUNT(*) as votes");
        dump("MATCH (b:Book {title: \"The Art of Prolog\"}) OPTIONAL MATCH (b) <-[r:Votes]- (:User) RETURN b, COUNT(r.score) as votes");
        dump("MATCH (b:Book {title: \"The Art of Prolog\"}) OPTIONAL MATCH (b) <-[r:Votes]- (:User) RETURN b, COUNT(*) as votes");
        
        dump("START b=node(5) MATCH (b:Book) <-[r:Votes]- (:User) RETURN COUNT(*) as votes, SUM(r.score) as total");
        dump("START b=node(5) MATCH (b:Book) <-[r:Votes]- (:User) RETURN AVG(r.score) as avgScore");
        dump("START b=node(5) MATCH (b:Book) <-[r:Votes]- (:User) RETURN MAX(r.score), MIN(r.score)");
        dump("START b=node(5) MATCH (b:Book) <-[r:Votes]- (:User) RETURN AVG(r.score) as avgScore, STDEV(r.score) as stdDevScore");
        
        dump("START b=node(5,6) MATCH (b:Book) <-[r:Votes]- (:User) RETURN b.title, COLLECT(r.score)");
        dump("START b=node(5,6) MATCH (b:Book) <-[r:Votes]- (:User) RETURN COLLECT(r.score)");
        dump("START b=node(5) MATCH (b:Book) <-[r:Votes]- (:User) RETURN AVG(r.score) as avgScore, COLLECT(r.score)");        
        dump("MATCH (b:Book) <-[r:Votes]- (:User) RETURN b, AVG(r.score) as avgScore ORDER BY avgScore DESC");
        
        // Conditional expressions
        dump("MATCH (b:Book)<-[r:Votes]-(:User) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN p.year, r.score");
        dump("MATCH (b:Book)<-[r:Votes]-(:User) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN CASE WHEN p.year > 2010 THEN 'Recent' ELSE 'Old' END as category, AVG(r.score)");
        dump("MATCH (b:Book)<-[r:Votes]-(:User) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN CASE p.year % 2 WHEN 0 THEN 'Even' WHEN 1 THEN 'Odd' ELSE 'Unknown' END as parity, AVG(r.score)");
        dump("MATCH (b:Book)<-[r:Votes]-(:User) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN CASE COALESCE(p.year % 2, -1) WHEN 0 THEN 'Even' WHEN 1 THEN 'Odd' ELSE 'Unknown' END as parity, AVG(r.score)");
        
        // Separating query parts using WITH
        dump("MATCH (b:Book) <-[r:Votes]- (:User) WITH b, AVG(r.score) as avgScore WHERE avgScore >=4 RETURN b, avgScore ORDER BY avgScore DESC");
        dump("MATCH (b:Book) <-[r:Votes]- (:User) WITH b, AVG(r.score) as avgScore "
                + "ORDER BY avgScore DESC LIMIT 1 OPTIONAL MATCH (b) -[p:PublishedBy]-> () "
                + "RETURN b.title, p.year");
        
        
        // UNION
        dump("MATCH (b:Book) RETURN 'Books' as type, COUNT(b) as cnt UNION ALL "
                + "MATCH (a:Person) RETURN 'Authors' as type, COUNT(a) as cnt");
    }

    private static void dump(String query) {
        System.out.println(query);
        System.out.println(engine.execute(query).dumpToString());
    }
}
