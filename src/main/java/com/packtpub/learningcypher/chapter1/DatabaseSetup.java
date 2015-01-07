package com.packtpub.learningcypher.chapter1;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.GregorianCalendar;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

/**
 * Database setup: creates the database and put some data in.
 *
 * @author Onofrio Panzarino
 */
public class DatabaseSetup {

    private static final String DB_PATH = "database/hr";

    /**
     * Creates an empty database
     *
     * @return an empty database
     */
    public GraphDatabaseService create() {
        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = graphDbFactory.newEmbeddedDatabase(DB_PATH);
        return db;
    }

    public GraphDatabaseService createWithMoreMemory() {
        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = graphDbFactory
                .newEmbeddedDatabaseBuilder(DB_PATH)
                .setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "20M")
                .newGraphDatabase();
        return db;
    }

    public GraphDatabaseService createWithPropertiesFile() {
        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
        try {
            URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
            String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
            URL propertyFileUrl = new URL("file:////" + jarPath + "neo4j.properties");

            GraphDatabaseService db = graphDbFactory
                    .newEmbeddedDatabaseBuilder(DB_PATH)
                    .loadPropertiesFromURL(propertyFileUrl)
                    .newGraphDatabase();
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes the existing database files
     *
     * @return {true} if the operation succedees
     */
    public boolean clearAll() {
        File f = new File(DB_PATH);
        return delete(f);
    }

    /**
     * Delete the file specified and all children
     *
     * @param f
     * @return {true} if the operation succedees
     */
    private boolean delete(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        return f.delete();
    }

    /**
     * Setup the database provided with some data
     *
     * @param graphDb the database to fill
     */
    public static void setup(GraphDatabaseService graphDb) {
        try (Transaction tx = graphDb.beginTx()) {
            // set up of center costs
            Node cc1 = graphDb.createNode(HrLabels.CostCenter);
            cc1.setProperty(CostCenter.CODE, "CC1");

            Node cc2 = graphDb.createNode(HrLabels.CostCenter);
            cc2.setProperty(CostCenter.CODE, "CC2");

            Node davies = graphDb.createNode(HrLabels.Employee);
            davies.setProperty(Employee.NAME, "Nathan");
            davies.setProperty(Employee.SURNAME, "Davies");

            Node taylor = graphDb.createNode(HrLabels.Employee);
            taylor.setProperty(Employee.NAME, "Rose");
            taylor.setProperty(Employee.SURNAME, "Taylor");

            Node underwood = graphDb.createNode(HrLabels.Employee);
            underwood.setProperty(Employee.NAME, "Heather");
            underwood.setProperty(Employee.MIDDLE_NAME, "Mary");
            underwood.setProperty(Employee.SURNAME, "Underwood");

            Node smith = graphDb.createNode(HrLabels.Employee);
            smith.setProperty(Employee.NAME, "John");
            smith.setProperty(Employee.SURNAME, "Smith");

            // There is a vacant post in the company
            Node vacantPost = graphDb.createNode();

            // davies belongs to CC1
            davies.createRelationshipTo(cc1, EmployeeRelationship.BELONGS_TO)
                    .setProperty(EmployeeRelationship.FROM,
                            new GregorianCalendar(2011, 1, 10).getTimeInMillis());

            // .. and reports to Taylor
            davies.createRelationshipTo(taylor, EmployeeRelationship.REPORTS_TO);

            // Taylor is the manager of CC1
            taylor.createRelationshipTo(cc1, EmployeeRelationship.MANAGER_OF)
                    .setProperty(EmployeeRelationship.FROM,
                            new GregorianCalendar(2010, 2, 8).getTimeInMillis());

            // Smith belongs to CC2 from 2008
            smith.createRelationshipTo(cc2, EmployeeRelationship.BELONGS_TO)
                    .setProperty(EmployeeRelationship.FROM,
                            new GregorianCalendar(2008, 9, 20).getTimeInMillis());

            // Smith reports to underwood
            smith.createRelationshipTo(underwood, EmployeeRelationship.REPORTS_TO);

            // Underwood belongs to CC2
            underwood.createRelationshipTo(cc2, EmployeeRelationship.BELONGS_TO);

            // Underwood will report to an employee not yet hired
            underwood.createRelationshipTo(vacantPost, EmployeeRelationship.REPORTS_TO);

            // But the vacant post will belong to CC2
            vacantPost.createRelationshipTo(cc2, EmployeeRelationship.BELONGS_TO);

            tx.success();
        }
    }
}
