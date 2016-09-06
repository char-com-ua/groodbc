# groodbc
jdbc driver with groovy script instead of sql

## idea
Provide jdbc wrapper with scripting language to make metadata available in reporting tools like BIRT or iReport/Jasper
for the formats like Json and XML.

Did you tried scripting datasource in BIRT to work with json?
For simple cases that's enough, but when you have to describe 100+ columns in 10 datasets - it becomes painful.

## jdbc connection
```groovy
groodbc:org:<here any groovy code that returns data>
```


### for example parse json file:
```groovy
groodbc:org: new groovy.json.JsonSlurper().parse( new File("./test/data/test.json").newReader("UTF-8") )
```

### or you can create connection at runtime and pass it into report engine:
```java
import org.groodbc.driver.GDBConnection;
import groovy.json.JsonSlurper;

Object data = JsonSlurper().parse( new File("./test/data/test.json"), "UTF-8"  );
GDBConnection connection = new GDBConnection(data);
```

## dataset/resultset
###sample data
```json
{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}
```
### select for all menu items
```groovy
select = {-> data.menu.popup.menuitem }
```
* `data` is a reference to the object evaluated/passed on the level of connection.
* `{ ->` means there is no input parameters in this query
* `.menu.popup.menuitem` is an accessor for json object. the select closure must return `List<Map<String,Object>>`

### select with param
```groovy
select = {String p_value-> data.menu.popup.menuitem.findAll{ it.value == p_value } }
```

###  aggregated data
```groovy
select = {-> 
  [
    //wrap one row into a list
    [
      //this is one row definition
      'COUNT'     :data.menu.popup.menuitem.size(),
      'MAX_VALUE' :data.menu.popup.menuitem.collect{it.value}.max()
    ] 
  ]
}
```

## dependencies
the groovy-all jar library
https://mvnrepository.com/artifact/org.codehaus.groovy/groovy-all/2.4.6


