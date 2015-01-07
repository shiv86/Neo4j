# Neo4J Quick Reference

The following is a quick guide to Neo4j.
######Neo4j is a property graph database. It consist of:
* Nodes - Which can have a number of labels
* Relationship - can have one direction and type.

Both nodes and relationships can have a number of properties.

######Neo4j has two running modes:
* A standalone server via REST ( Obviously a lot safer and a crash would not affect the server)
* An embedded database in Java Application (use when application is a standalone and no requirement for a separate Database Server.

######POM Dependency and Repo:
```sh
<dependencies>
		<dependency>
			<groupId>org.neo4j</groupId>
			<artifactId>neo4j</artifactId>
			<version>2.0.0</version>
		</dependency>
	</dependencies>
	<repositories>
		<repository>
			<id>neo4j</id>
			<url>http://m2.neo4j.org/content/repositories/releases/</url>
			<releases>
				<enabled>true</enabled>
			</releases>
		</repository>
	</repositories>
```

###### Creating an embedded database:
```java
        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = graphDbFactory.newEmbeddedDatabase(DB_PATH);
        return db;
```
* With the **GraphDatabaseService** you can fully interact with the database: Create Nodes, Relationships, set properties and indexes.


###### Configuration:
Neo4j allows you to pass a set of configuration options for **performance tuning** , **caching**,**logging**,**file system use** and other low-level behaviours.

* **GraphDatabaseSettings.java** contains a list of properties which can be customized

```java
private static final String DB_PATH = "database/hr";

//Create with more memory
GraphDatabaseService db = graphDbFactory
                .newEmbeddedDatabaseBuilder(DB_PATH)
                .setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "20M")
                .newGraphDatabase();
                
//Creating using props file
GraphDatabaseService db = graphDbFactory
                    .newEmbeddedDatabaseBuilder(DB_PATH)
                    .loadPropertiesFromURL(propertyFileUrl)
                    .newGraphDatabase();
                    
//neo4j.properties
neostore.nodestore.db.mapped_memory=20M
```


#### Chapter 1 - Querying with Pattern Matching
Chapter One contains a HR Management Tool consisting of 
* Nodes: Employee, CostCenter
* Relationships: (1) Employees that belong to cost centers, (2) Employees that report to other employees and (3) Employees that can be managers of cost centers

###### Defining Nodes
```java
public enum HrLables implements Label {
    Employee,CostCenter
}
```

###### Defining Relationships

```java
public enum EmployeeRelationship implements RelationshipType {
    REPORTS_TO, BELONGS_TO, MANAGER_OF;
    // from specifies the dates of validity
    public static final String PROPERTY_FROM = "from";
}
```
###### Transactions
Neo4J is a fully ACID compliant database. Similar to JPA and with **FlushMode.COMMIT** , the entity is persisted when the entity transaction is committed, a graph record is commited once the graph transaction is closed.

```java  
org.neo4j.graphdb.Transaction:
public interface Transaction extends AutoCloseable
```

Since Java SE7:

>The new syntax allows you to **declare resources** that are part of the **try** block. What this means is that you define the resources ahead of time and the runtime automatically closes those resources (if they are not already closed) after the execution of the try block. **Note: This only occurs fore resources that extend AutoCloseable** as shown above.

```java
// Java SE7 and Greater
 try (Transaction tx = graphDb.beginTx()) {
 //perform some work with graph ...
 tx.success();
 }
```

```java
// Java SE6 
Transaction tx = graphDb.beginTx();
 try {
 //perform some work with graph ...
  tx.success();
 } finally {
  tx.close();
 }
```

**tx.sucess()** - marks the transaction as sucessfull. Every change will be committed once the transaction is closed. If an exception is thrown from inside the try statement, the transaction automatically ends with a roll back.

###### Creating Nodes and relationships using Java API

Note in **com.packtpub.learningcypher.chapter1.DatabaseSetup.setup(GraphDatabaseService graphDb)**

Demonstrates creating Nodes with Labels and properties. And how to create Relationships from Nodes to Other Nodes E.g.:

```java
 Node cc1 = graphDb.createNode(HrLabels.CostCenter);
            cc1.setProperty(CostCenter.CODE, "CC1");
            
Node davies = graphDb.createNode(HrLabels.Employee);
            davies.setProperty(Employee.NAME, "Nathan");
            davies.setProperty(Employee.SURNAME, "Davies");     
            
 // davies belongs to CC1
 davies.createRelationshipTo(cc1, EmployeeRelationship.BELONGS_TO)
 .setProperty(EmployeeRelationship.FROM, new GregorianCalendar(2011, 1, 10).getTimeInMillis());

// .. and reports to Taylor
davies.createRelationshipTo(taylor, EmployeeRelationship.REPORTS_TO);            
```

* **createNode**:  This creates a node and then returns it as a result. The node will be created with a long, unique ID. **Note: Unlike relational databases, node IDs in Neo4j are not guaranteed to remain fixed forever. Infact IDs are recomputed upon node deletion, so don't trust IDs**

*  **createRelationshipTo**: This creates a relationship between two nodes and returns that relationship in a relationship instance. This one too will have a lon. unique ID
*  **setProperty**: This sets the value of a property of a node or relationship.

We put time in milliseconds in the property because Neo4j supports only the following types/array of types: **boolean,byte,short,int,long,float,double,String**









