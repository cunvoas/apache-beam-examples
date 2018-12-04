package firestore.example.function;

import firestore.example.model.Person;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.regex.Pattern;

public class ParsePersonFromCSV extends DoFn<String, Person> {
    private String delimiter;

    public ParsePersonFromCSV(String delimiter) {
        this.delimiter = delimiter;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        String csvLine = c.element();
        String[] fields = csvLine.split(Pattern.quote(delimiter));

        Person p = new Person(fields[0].trim(), fields[1].trim(), Integer.valueOf(fields[2].trim()));
        c.output(p);
    }
}
