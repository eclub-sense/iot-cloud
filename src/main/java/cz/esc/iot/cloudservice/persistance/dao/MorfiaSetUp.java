package cz.esc.iot.cloudservice.persistance.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import static java.util.Arrays.asList;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Connects to zettor's MongoDB database. Creating Morfia's datastore.
 */
public class MorfiaSetUp {

    private static final Morphia morphia = new Morphia();

    static {
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("cz.esc.iot.cloudservice.persistance.model");
    }

    //*
    static MongoClient mongoClient = new MongoClient(asList(new ServerAddress("147.32.110.50:27017")),
            Arrays.asList(MongoCredential.createCredential("pauliada",
                            "zettor",
                            "j7dpxrIfYjbZIfFtdS1Y".toCharArray())),
            MongoClientOptions.builder().serverSelectionTimeout(1000).build());

    static {
        try {
            mongoClient.getDB("zettor").command("ping");
        } catch (MongoTimeoutException e) {
            System.exit(512);
        }
    }
    private static final Datastore datastore = morphia.createDatastore(mongoClient, "zettor");
     /*/
     // create the Datastore connecting to the default port on the local host
     private static final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 27017), "CloudDB");
    //*/

    static {
        datastore.ensureIndexes();
    }

    public static Morphia getMorphia() {
        return morphia;
    }

    public static Datastore getDatastore() {
        return datastore;
    }

}
