
import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb2.TDB2Factory;

public class FusekiTDB2Example {

    public static void main(String[] args) {

        // 1. Create a TDB2 dataset
        String tdb2Location = "KG"; // Replace with your TDB2 directory
        Dataset dataset = TDB2Factory.connectDataset(tdb2Location);

        // 2. Preload the pizza.ttl file into the dataset
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

        // 3. Create a Fuseki server using the TDB2 dataset
        FusekiServer server = FusekiServer.create()
                .add("/dataset", dataset) // Expose the dataset at the endpoint /dataset
                .port(3030)  // Set the port (default is 3030)
                .build();

        // 4. Start the server
        server.start();

        System.out.println("Fuseki server started at http://localhost:3030/dataset");

        // Add a shutdown hook to stop the server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Fuseki server stopped.");
        }));
    }
}
