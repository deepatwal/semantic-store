package org.example;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

public class FusekiServerStarter {

    public static void main(String[] args) {
        // Create an in-memory dataset (for demonstration)
        Model model = ModelFactory.createDefaultModel();
        Resource resource = model.createResource("http://example.org/person/JohnDoe");
        resource.addProperty(model.createProperty("http://example.org/ontology/name"), "John Doe");
        Dataset dataset = DatasetFactory.create(model);

        // Create and start the Fuseki server
        FusekiServer server = FusekiServer.create()
                .add("/my_dataset", dataset)
                .port(3030)
                .build();
        server.start();

        System.out.println("Fuseki server started at http://localhost:3030/my_dataset");

        // Keep the server running (Ctrl+C to stop in the terminal)
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle interruption (e.g., Ctrl+C)
                break; // Exit the loop
            }
        }

        // Server shutdown (this part will be reached after Ctrl+C)
        server.stop();
        System.out.println("Fuseki server stopped.");
    }
}