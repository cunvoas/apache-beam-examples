package firestore.example;

import firestore.example.function.ParsePersonFromCSV;
import firestore.example.function.PersonneToEntity;
import firestore.example.model.Person;
import com.google.datastore.v1.Entity;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.datastore.DatastoreIO;
import org.apache.beam.sdk.options.*;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

public class PersonCSVToFirestore {

    private interface CSVToFirestoreOptions extends PipelineOptions {
        @Description("GCP Project ID")
        @Validation.Required
        ValueProvider<String> getProjectId();
        void setProjectId(ValueProvider<String> id);

        @Description("Which CSV file to read")
        @Validation.Required
        ValueProvider<String> getCSVPath();
        void setCSVPath(ValueProvider<String> path);

        @Description("On which Datastore kind to store data")
        @Validation.Required
        ValueProvider<String> getDatastoreKindDestination();
        void setDatastoreKindDestination(ValueProvider<String> path);
    }

    public static void main(String[] args) {
        CSVToFirestoreOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(CSVToFirestoreOptions.class);
        Pipeline p = Pipeline.create(options);

        PCollection<String> csvLines = p.apply("Read CSV lines", TextIO.read().from(options.getCSVPath()));
        PCollection<Person> personnes = csvLines.apply("Parse CSV to in-memory object", ParDo.of(new ParsePersonFromCSV(",")));
        PCollection<Entity> entities = personnes.apply("In-memory object to Firestore entity", ParDo.of(new PersonneToEntity(options.getDatastoreKindDestination())));

        entities.apply("Save entities to Datastore", DatastoreIO.v1().write()
                .withProjectId(options.getProjectId()));

        p.run().waitUntilFinish();
    }
}
