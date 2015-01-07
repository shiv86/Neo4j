package com.packtpub.learningcypher.chapter1;

import java.util.Map;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * This program creates a database, put some data in and executes some query examples.
 * 
 * @author Onofrio Panzarino
 */
public class Program {

    public static void main(String[] args) {
        DatabaseSetup ds = new DatabaseSetup();
        ds.clearAll();

        GraphDatabaseService graphDb = ds.createWithPropertiesFile();
        try {
            DatabaseSetup.setup(graphDb);
            
            final String allEmployees = "MATCH (e:Employee) RETURN e";
            
            ExecutionEngine engine = new ExecutionEngine(graphDb);
            ExecutionResult result = engine.execute(allEmployees);

            String dumped = result.dumpToString();
            System.out.println(allEmployees);
            System.out.println(dumped);

            result = engine.execute(allEmployees);

            ResourceIterator<Node> nodes = result.columnAs("e");
            while (nodes.hasNext()) {
                Node n = nodes.next();
                System.out.println(n.toString());
            }
            
            
            result = engine.execute(allEmployees);
            ResourceIterator<Map<String, Object>> rows = result.iterator();
            for(Map<String,Object> row : IteratorUtil.asIterable(rows)) {
                Node n = (Node) row.get("e");
                System.out.println(n.toString());
            }
            
            QueryExamples examples = new QueryExamples((new QueryDumper(engine)));
            examples.executeQuerys();

        } finally {
            graphDb.shutdown();
        }
    }
}
