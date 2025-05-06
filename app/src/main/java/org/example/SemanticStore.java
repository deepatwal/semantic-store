package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.tdb2.TDB2Factory;

public class SemanticStore {

    private static final Logger logger = Logger.getLogger(SemanticStore.class.getName());

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream fis = SemanticStore.class.getResourceAsStream("/config.properties")) {
            if (fis == null) {
                throw new IOException("config.properties not found in the classpath");
            }
            properties.load(fis);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration file: ", e);
            return;
        }

        String tdbDirectory = properties.getProperty("tdb.directory");
        String datasetName = properties.getProperty("dataset.name");

        Dataset dataset = TDB2Factory.connectDataset(tdbDirectory + "/" + datasetName);

        try {
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();

            Resource resource = model.createResource("http://example.org/person/JohnDoe");
            resource.addProperty(model.createProperty("http://example.org/person/name"), "John Doe");
            resource.addProperty(model.createProperty("http://example.org/person/age"), "30");
            resource.addProperty(model.createProperty("http://example.org/person/email"), "john.doe@example.org");
            resource.addProperty(model.createProperty("http://example.org/person/phone"), "123-456-7890");

            dataset.commit();
            logger.log(Level.INFO, "Triples successfully written to the dataset.");
        } catch (Exception e) {
            dataset.abort();
            logger.log(Level.SEVERE, "Error writing triples: ", e);
        } finally {
            dataset.end();
        }

        try {
            dataset.begin(ReadWrite.READ);
            Model model = dataset.getDefaultModel();
            logger.log(Level.INFO, "Reading triples from the dataset...");
            for (Statement stmt : model.listStatements().toList()) {
                logger.log(Level.INFO, "Statement: {0}", stmt);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reading triples: ", e);
        } finally {
            dataset.end();
        }
    }
}
