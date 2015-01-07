package com.packtpub.learningcypher.chapter1;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;

/**
 * Employee queries
 * 
 * @author Onofrio Panzarino
 */
public class EmployeeRepository {
    private final ExecutionEngine engine;

    /**
     * Initializes the class with an execution engine
     * @param engine the execution engine
     */
    public EmployeeRepository(ExecutionEngine engine) {
        this.engine = engine;
    }
    
    /**
     * All employees in the database
     * @return all employees
     */
    public LinkedList<Employee> all() {
        ExecutionResult result = engine.execute("MATCH (e:Employee) RETURN e");
        Iterator<Node> nodes = result.columnAs("e");
        return fromNodes(nodes);
    }
    
    /**
     * All employees belonging to a center cost
     * @param centerCost the center cost 
     * @return employees belonging to the center cost
     */
    public LinkedList<Employee> belongingTo(String centerCost) {
        Map<String, Object> params = new HashMap<>();
        params.put("centerCost", centerCost);
   
        ExecutionResult result = engine.execute("MATCH (n) -[:BELONGS_TO]->( :CostCenter{ code: {centerCost} } ) RETURN n", params);
        Iterator<Node> nodes = result.columnAs("n");
        return fromNodes(nodes);
    }
    
    /**
     * All employees belonging to a center cost
     * @param centerCost the center cost 
     * @return employees belonging to the center cost
     */
    public LinkedList<Employee> belongingTo(String centerCost, Date after) {
        Map<String, Object> params = new HashMap<>();
        params.put("centerCost", centerCost);
        params.put("after", after.getTime());
        
        ExecutionResult result = engine.execute("MATCH (n) -[r:BELONGS_TO]->( :CostCenter{ code: {centerCost} } ) WHERE r.from > {after} RETURN n", params);
        Iterator<Node> nodes = result.columnAs("n");
        return fromNodes(nodes);
    }
    
    /**
     * All employees with a given surname
     * @param surname the surname to search
     * @return employees with the given surname
     */
    public LinkedList<Employee> bySurname(String surname) {
        Map<String, Object> params = new HashMap<>();
        params.put("surname", surname);
        ExecutionResult result = engine.execute("MATCH (n:Employee {surname: {surname}}) "
                + "RETURN n", 
                params);
        Iterator<Node> nodes = result.columnAs("n");
        return fromNodes(nodes);
    }
    
     /**
     * The shortest path between two employees
     * 
     * @param surname1 the surname of the first employee
     * @param surname2 the surname of the second employee
     * 
     * @return list of paths
     */
    public Object shortestPath(String surname1, String surname2) {
        Map<String, Object> params = new HashMap<>();
        params.put("surname1", surname1);
        params.put("surname2", surname2);
        
        ExecutionResult result = engine
                .execute("MATCH (a{surname:{surname1} }), (b{surname:{surname2} }) "
                        + "RETURN allShortestPaths((a)-[*]-(b)) AS path", params);
        Iterator<Object> paths = result.columnAs("path");        
        for(Object p : IteratorUtil.asIterable(paths)) 
            return p;
        return null;
    }
    
    /**
     * Creates employees from a node iterator
     * 
     * @param nodes node iterator
     * @return employees found in the iterator
     */
    public LinkedList<Employee> fromNodes(Iterator<Node> nodes) {
        LinkedList<Employee> returnEmployees = new LinkedList<>();
        for (Node node : IteratorUtil.asIterable(nodes)) {
            returnEmployees.add(Employee.fromNode(node));
        }
        return returnEmployees;
    }
}
