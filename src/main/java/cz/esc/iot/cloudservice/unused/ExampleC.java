package cz.esc.iot.cloudservice.unused;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class ExampleC {
    
    @Id
    ObjectId id;
    long integer = System.currentTimeMillis();

    @Override
    public String toString() {
        return "ExampleC{" + "id=" + id + ", integer=" + integer + '}';
    }
}
