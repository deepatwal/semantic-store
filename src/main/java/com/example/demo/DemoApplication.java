package com.example.demo;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;
import org.apache.jena.tdb2.TDB2Factory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        String tdb2Location = "KG";

        // Connect to the dataset
        var dataset = TDB2Factory.connectDataset(tdb2Location);

        // Preload the pizza.ttl file into the dataset
        dataset.begin(org.apache.jena.query.ReadWrite.WRITE);
        try {
            RDFDataMgr.read(dataset, "./pizza.ttl", Lang.TTL);
            dataset.commit();
        } catch (Exception e) {
            dataset.abort();
            e.printStackTrace();
        } finally {
            dataset.end();
        }

        // Create and start the Fuseki server
        FusekiServer server = FusekiServer.create()
                .add("/dataset", dataset)
                .port(3030) // Default port for Fuseki
                // Removed as the Web UI is enabled by default in recent versions of Fuseki
                .build();
        server.start();
    }

}
