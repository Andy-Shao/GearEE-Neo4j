# GearEE-Neo4j
## What & Why ?
The core reason of build this projection is that I am not used to Spring-Data with JPA or Hibernate. For ORM framework or Neo4j GOM framework which are not agility enought. And then, I want to build someone which look like, work similer with Mybatis. You can use Cypher hand by hand & do not need care about any of Object-Model. 

## Testing environment prepare
### docker running neo4j
```shell
docker run --name neo4j-test -p 7474:7474 -p 7687:7687 --env NEO4J_AUTH=neo4j/password neo4j
```
### Testing stage
When you are running this project firstly, you should run CreatePersonTest.java, MatchPersonTest.java, 
AsyncCreatePersonTest.java, and MatchPersonTest2.java testing cases in neo4j-core module.