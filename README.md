### Apache Beam Examples

##### To run the Firestore example locally

PersonCSVToFirestore is a simple pipeline taking a [Person.csv](https://github.com/vdolez/apache-beam-examples/blob/master/src/main/java/firestore/example/input/person.csv) and then sending it to Firestore using Apache Beam.

Note that the CSV structure is known before executing the pipeline 
```
mvn compile exec:java \
	-Dexec.mainClass=firestore.example.PersonCSVToFirestore \
	-Dexec.args="--project=<YOUR_PROJECT_ID> \
	--CSVPath=<PATH_TO_CSV_FILE> \
	--datastoreKindDestination=<ENTITY_KIND> \
	--projectId=<DATASTORE_PROJECT_ID> \
	--runner=DirectRunner"
``` 

