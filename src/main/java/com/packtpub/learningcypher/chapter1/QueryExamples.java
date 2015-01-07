package com.packtpub.learningcypher.chapter1;

/**
 * Queries explained in the first chapter
 * 
 * @author Onofrio Panzarino
 */
public class QueryExamples {

    private final QueryDumper dumper;

    public QueryExamples(QueryDumper dumper) {
        this.dumper = dumper;
    }

    public void executeQuerys() {
        
        // Finding nodes by relations
        dumper.dump("MATCH (n:Employee) --> (cc:CostCenter) RETURN cc,n");
        
        dumper.dump("MATCH (n:Employee) --> (cc:CostCenter) RETURN *");

        // Filtering on properties
        dumper.dump("MATCH (n:Employee) --> (:CostCenter { code: 'CC1' }) RETURN n");

        // Filtering on relationships
        dumper.dump("MATCH (n:Employee) -[r]- (:CostCenter { code: 'CC1' }) RETURN n.surname,n.name,r");

        dumper.dump("MATCH (n) -[:BELONGS_TO]-> (:CostCenter { code: 'CC1' } ) RETURN n");

        dumper.dump("MATCH (n) -[r:BELONGS_TO|MANAGER_OF]-> (:CostCenter { code: 'CC1' } ) RETURN n.name,n.surname,r");

        dumper.dump("MATCH (n) -[:BELONGS_TO]-> (cc:CostCenter) <-[:MANAGER_OF]- (m) RETURN n.surname,m.surname,cc.code");

        dumper.dump("MATCH (n) -[:BELONGS_TO]-> (cc:CostCenter) <-[:MANAGER_OF]- (m) <-[:REPORTS_TO]- (k) RETURN n.surname,m.surname,cc.code, k.surname");

        dumper.dump("MATCH (:Employee{surname: 'Smith'}) -[*2]- (neighborhood) RETURN neighborhood");
  
        dumper.dump("MATCH (:Employee{surname: 'Smith'}) -[*2]- (neighborhood) RETURN DISTINCT neighborhood");
        
        dumper.dump("MATCH (:Employee{surname: 'Smith'}) -[r*2]- (neighborhood) RETURN neighborhood,r");
  
        dumper.dump("MATCH (:Employee{surname: 'Smith'}) -[r*2..3]- (neighborhood) RETURN neighborhood,r");
        
        dumper.dump("MATCH (:Employee{surname: 'Smith'}) -[r*0..2]- (neighborhood) RETURN neighborhood,r");   
        
        dumper.dump("MATCH (e:Employee) <-[:REPORTS_TO]- (m:Employee) RETURN e.surname,m.surname");

        // Dealing with missing parts
        dumper.dump("MATCH (e:Employee) OPTIONAL MATCH  (e) <-[:REPORTS_TO]- (m:Employee) RETURN e.surname,m.surname");
        
        dumper.dump("MATCH (e:Employee) OPTIONAL MATCH  (c:CostCenter) <-[:MANAGER_OF]- (e) <-[:REPORTS_TO]- (m:Employee) RETURN e.surname,m.surname,c.code");
        
        dumper.dump("MATCH (e:Employee) OPTIONAL MATCH (e) <-[:REPORTS_TO]- (m:Employee) OPTIONAL MATCH (e) -[:MANAGER_OF]-> (c:CostCenter) RETURN e.surname,m.surname,c.code");
        
        // Working with PATHS
        dumper.dump("MATCH path = (a{surname:'Davies'}) -[*]- (b{surname:'Taylor'}) RETURN path"); 
        dumper.dump("MATCH (a{surname:'Davies'}), (b{surname:'Taylor'}) RETURN allShortestPaths((a)-[*]-(b)) AS path"); 
        
        // Starting points with node IDs
        dumper.dump("START a=node(2), b=node(3) RETURN allShortestPaths((a)-[*]-(b)) AS path"); 
    }
}
