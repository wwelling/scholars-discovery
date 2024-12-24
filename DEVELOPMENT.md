Clear the index.
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "{\"delete\":{\"query\":\"*:*\"}}"
```

Add a type.
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/schema" -d "{\"add-field\":{\"name\":\"type\",\"type\":\"string\",\"stored\":true}}"
```

Create a parent document.
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"parent1\",\"title\":\"Parent Document\",\"children\":[{\"id\":\"child1\",\"title\":\"Child Document 1\",\"type\":\"book\"},{\"id\":\"child2\",\"title\":\"Child Document 2\",\"type\":\"postcard\"}]}]"
```

Add a child document to existing parent document.
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"parent1\",\"children\":{\"add\":[{\"id\":\"child3\",\"title\":\"Child Document 3\",\"type\":\"newspaper\"}]}}]"
```

Get parent document.
```
curl "http://localhost:8983/solr/scholars-discovery/select?q=id:parent1&fl=*,%5Bchild%5D&wt=json"
```

Facet on child document property.
```
curl "http://localhost:8983/solr/scholars-discovery/select?q=*:*&json.facet=%7Bcategories:%7Btype:terms,field:type,domain:%7BblockChildren:%27id:parent1%27%7D%7D%7D&wt=json"
```
