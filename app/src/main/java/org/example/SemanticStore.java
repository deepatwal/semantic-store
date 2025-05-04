package org.example;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.tdb2.TDB2Factory;

public class SemanticStore {

    public static void main(String[] args) {

        String tdbDirectory = "C:/Users/deepa/data/workspace/github/TBD-storage";
        String datasetName = "Dataset_04-05-2025";

        Dataset dataset = TDB2Factory.connectDataset(tdbDirectory + "/" + datasetName);

        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();

            Resource resource = model.createResource("http://example.org/person/JohnDoe");
            resource.addProperty(model.createProperty("http://example.org/person/name"), "John Doe");

            dataset.commit();
            System.out.println("Triples successfully written to the dataset.");
        } catch (Exception e) {
            dataset.abort();
            e.printStackTrace();
        } finally {
            dataset.end();
        }

        // --- READ TRIPLES ---
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            System.out.println("\nReading all triples from the dataset:");
            for (Statement stmt : model.listStatements().toList()) {
                System.out.println(stmt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataset.end();
        }
    }
}
