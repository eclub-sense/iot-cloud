package cz.esc.iot.cloudservice.persistance.dao;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorfiaSetUp {

    private static final Morphia morphia = new Morphia();

    static {
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("cz.esc.iot.cloudservice.persistance.model");
    }

    // create the Datastore connecting to the default port on the local host
    private static final Datastore datastore = morphia.createDatastore(new MongoClient( "localhost" , 27017 ), "CloudDB");

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
