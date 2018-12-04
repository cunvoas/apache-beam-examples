package firestore.example.function;

import firestore.example.model.Person;
import com.google.datastore.v1.Entity;
import com.google.datastore.v1.Key;
import com.google.datastore.v1.client.DatastoreHelper;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;

public class PersonneToEntity extends DoFn<Person, Entity> {

    private ValueProvider<String> entityKind;

    public PersonneToEntity(ValueProvider<String> kind) {
        entityKind = kind;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        c.output(personneToEntity(entityKind.get(), c.element()));
    }

    public static Entity personneToEntity(String kind, Person p) {
        Key.Builder keyBuilder = DatastoreHelper.makeKey(kind, new String(p.getFirstName()));
        Entity.Builder entityBuilder = Entity.newBuilder();

        entityBuilder.setKey(keyBuilder);

        entityBuilder.putProperties("firstName", DatastoreHelper.makeValue(p.getFirstName()).build());
        entityBuilder.putProperties("lastName", DatastoreHelper.makeValue(p.getLastName()).build());
        entityBuilder.putProperties("age", DatastoreHelper.makeValue(p.getAge()).build());

        return entityBuilder.build();
    }
}
