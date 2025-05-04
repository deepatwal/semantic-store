package org.example;

import java.nio.file.Paths;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb2.TDB2Factory;

public class FusekiServerStarter {

    public static void main(String[] args) {
        String tdbDirectory = Paths.get(".").toAbsolutePath().normalize().toString() + "/tdb2-data";
        Dataset dataset = TDB2Factory.connectDataset(tdbDirectory);

        // Add some initial data (optional) - Use try-finally for transaction safety
        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            Resource resource = model.createResource("http://example.org/person/JohnDoe");
            resource.addProperty(model.createProperty("http://example.org/ontology/name"), "John Doe");
            dataset.commit();
        } finally {
            if (dataset.isInTransaction()) {
                dataset.abort();
            }
            // DO NOT close the dataset here. It must remain open for Fuseki.
        }


        // Create and start the Fuseki server
        FusekiServer server = FusekiServer.create()
                .add("/my_dataset", dataset)
                .port(3030)
                .build();
        server.start();

        System.out.println("Fuseki server started at http://localhost:3030/ with TDB2 backend.");
        System.out.println("Dataset available at: http://localhost:3030/my_dataset");
        System.out.println("Fuseki UI available at: http://localhost:3030/");

        // Keep the server running (Ctrl+C to stop in the terminal)
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        // Server shutdown
        server.stop();
        System.out.println("Fuseki server stopped.");

        // Close the dataset *after* the server has stopped
        dataset.close(); 
    }
}