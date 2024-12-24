Clear the index:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "{\"delete\":{\"query\":\"*:*\"}}"
```

Add a class field of type string:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/schema" -d "{\"add-field\":{\"name\":\"class\",\"type\":\"string\",\"stored\":true}}"
```

Create a document:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"parent1\",\"title\":\"Parent\",\"documents\":[{\"id\":\"document1\",\"title\":\"Document 1\",\"class\":\"Document\"},{\"id\":\"document2\",\"title\":\"Document 2\",\"class\":\"Document\"}]}]"
```

Add a nested document to existing document:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"parent1\",\"documents\":{\"add\":[{\"id\":\"document3\",\"title\":\"Document 3\",\"class\":\"Document\"}]}}]"
```

Get document with nested documents:
```
curl "http://localhost:8983/solr/scholars-discovery/select?q=id:parent1&fl=*,%5Bchild%5D&wt=json"
```

Facet on nested document property class:
```
curl "http://localhost:8983/solr/scholars-discovery/select?q=*:*&json.facet=%7Bcategories:%7Btype:terms,field:class%7D%7D&wt=json"
```


Create the nested document independently:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"document4\",\"title\":\"Document 4\",\"class\":\"Document\"}]"
```

Add the child document to the parent document by ID reference:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"parent1\",\"documents\":{\"add\":[{\"id\":\"document4\"}]}}]"
```

***This only adds the id! Requires entire child document or specific fields desired in response and/or filtering, faceting, or sorting.***

Add a different nested document to existing document:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"parent1\",\"works\":{\"add\":[{\"id\":\"work1\",\"title\":\"Work 1\",\"class\":\"Teaching\"}]}}]"
```

Create a parent document with another nested document:
```
curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/scholars-discovery/update?commit=true" -d "[{\"id\":\"parent2\",\"title\":\"Parent 2\",\"work\":[{\"id\":\"work2\",\"title\":\"Work 2\",\"class\":\"Programming\"},{\"id\":\"work3\",\"title\":\"Work 3\",\"class\":\"Nurturing\"}]}]"
```

Get document with nested documents:
```
curl "http://localhost:8983/solr/scholars-discovery/select?q=id:parent2&fl=*,%5Bchild%5D&wt=json"
```

Facet on nested document property class:
```
curl "http://localhost:8983/solr/scholars-discovery/select?q=*:*&json.facet=%7Bcategories:%7Btype:terms,field:class%7D%7D&wt=json"
```

