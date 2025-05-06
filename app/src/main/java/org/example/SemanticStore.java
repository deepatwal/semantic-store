package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.tdb2.TDB2Factory;

public class SemanticStore {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        String tdbDirectory = properties.getProperty("tdb.directory", "C:/Users/deepa/data/workspace/github/TBD-storage");
        String datasetName = properties.getProperty("dataset.name", "Dataset_04-05-2025");

        Dataset dataset = TDB2Factory.connectDataset(tdbDirectory + "/" + datasetName);

        try {
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();

            Resource resource = model.createResource("http://example.org/person/JohnDoe");
            resource.addProperty(model.createProperty("http://example.org/person/name"), "John Doe");

            dataset.commit();
            System.out.println("Triples successfully written to the dataset.");
        } catch (Exception e) {
            dataset.abort();
            System.err.println("Error writing triples: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dataset.end();
        }

        try {
            dataset.begin(ReadWrite.READ);
            Model model = dataset.getDefaultModel();
            System.out.println("\nReading all triples from the dataset:");
            for (Statement stmt : model.listStatements().toList()) {
                System.out.println(stmt);
            }
        } catch (Exception e) {
            System.err.println("Error reading triples: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dataset.end();
        }
    }
}