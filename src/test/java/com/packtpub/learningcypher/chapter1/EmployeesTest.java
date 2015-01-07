package com.packtpub.learningcypher.chapter1;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Path;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * Unit test for Employees
 */
public class EmployeesTest
        extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EmployeesTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(EmployeesTest.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        ds = new DatabaseSetup();
        ds.clearAll();
        graphDb = ds.create();
        ds.setup(graphDb);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        graphDb.shutdown();
    }
    
    DatabaseSetup ds;
    GraphDatabaseService graphDb;

    /**
     * Rigourous Test :-)
     */
    public void testBySurname() {
        ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);
        EmployeeRepository employees = new EmployeeRepository(engine);
        
        List<Employee> bySurname = employees.bySurname("Davies");
        assertNotNull("returned not null", bySurname);
        assertEquals(1, bySurname.size());
        assertEquals("Nathan", bySurname.get(0).getName());
        
        bySurname = employees.bySurname("Taylor");
        assertNotNull("2nd returned not null", bySurname);
        assertEquals(1, bySurname.size());
        assertEquals("Rose", bySurname.get(0).getName());
    }
    
    public void testBelonging() {
        ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);
        EmployeeRepository employees = new EmployeeRepository(engine);
        
        List<Employee> belongingToCc1 = employees.belongingTo("CC1");
        assertNotNull(belongingToCc1);
        assertEquals(1, belongingToCc1.size());
    }
    
    public void testBelongingFrom2010() {
        ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);
        EmployeeRepository employees = new EmployeeRepository(engine);
        
        List<Employee> belongingToCc1 = employees.belongingTo("CC1",
                new GregorianCalendar(2010, 1, 1).getTime());
        assertNotNull(belongingToCc1);
        assertEquals(1, belongingToCc1.size());
    }
    
    public void testAllEmployees() {
        ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);
        EmployeeRepository employees = new EmployeeRepository(engine);
        
        List<Employee> all = employees.all();
        assertNotNull(all);
        assertEquals(4, all.size());
    }
    
    public void testShortestPath() {
        ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);
        EmployeeRepository employees = new EmployeeRepository(engine);
        
        Object p = employees.shortestPath("Taylor", "Smith");
        assertNotNull(p);
        // assertEquals(1, p.length());
        
    }
}
