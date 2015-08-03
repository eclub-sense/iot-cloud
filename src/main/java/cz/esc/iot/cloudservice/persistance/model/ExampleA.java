package cz.esc.iot.cloudservice.persistance.model;

import java.io.Serializable;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class ExampleA implements Serializable {

    @Id
    public ObjectId id;

    @Override
    public String toString() {
        return "ExampleA{" + "id=" + id + '}';
    }
}
