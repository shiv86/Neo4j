package com.packtpub.learningcypher.chapter1;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

/**
 * This class executes a query and dumps the result on a stream (default is
 * standard output stream)
 *
 * @author Onofrio Panzarino
 */
public class QueryDumper implements Closeable {

    private ExecutionEngine engine;
    private PrintStream stream;

    /**
     * Initializes the dumper with the engine. The stream is set to
     * {@code System.out}
     *
     * @param engine the engine used to execute the query. This reference will
     * be hold during all the life of the object
     *
     * @see System
     */
    public QueryDumper(ExecutionEngine engine) {
        this(engine, System.out);
    }

    /**
     * Initializes the dumper with the engine and the stream
     *
     * @param engine the engine used to execute the query. This reference will
     * be hold during all the life of the object
     * @param stream the stream to use to print out.
     */
    public QueryDumper(ExecutionEngine engine, PrintStream stream) {
        this.engine = engine;
        this.stream = stream;
    }

    /**
     * Executes the query and dumps the result
     *
     * @param query
     */
    public void dump(String query) {
        
        long startTime = System.nanoTime();
        ExecutionResult result = engine.execute(query);
        long stopTime = System.nanoTime();
        stream.println(query);
        stream.println(result.dumpToString());
        
        stream.printf("Executed in %d nanoseconds", stopTime - startTime).println();
    }

    @Override
    public void close() throws IOException {
        if (stream != null) {
            try {
                stream.close();
            } catch(Throwable t) {
            }
        }
    }
}
