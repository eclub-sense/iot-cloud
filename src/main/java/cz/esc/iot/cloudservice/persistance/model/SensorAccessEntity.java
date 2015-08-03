package cz.esc.iot.cloudservice.persistance.model;

import java.util.Objects;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class SensorAccessEntity {

    @Id
    private ObjectId id;
    @Reference
    private UserEntity owener;
    @Reference
    private UserEntity client;
    @Reference
    private SensorEntity sensor;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public UserEntity getOwener() {
        return owener;
    }

    public void setOwener(UserEntity owener) {
        this.owener = owener;
    }

    public UserEntity getClient() {
        return client;
    }

    public void setClient(UserEntity client) {
        this.client = client;
    }

    public SensorEntity getSensor() {
        return sensor;
    }

    public void setSensor(SensorEntity sensor) {
        this.sensor = sensor;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.owener);
        hash = 29 * hash + Objects.hashCode(this.client);
        hash = 29 * hash + Objects.hashCode(this.sensor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SensorAccessEntity other = (SensorAccessEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.owener, other.owener)) {
            return false;
        }
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        return Objects.equals(this.sensor, other.sensor);
    }

    @Override
    public String toString() {
        return "SensorAccessEntity{" + "id=" + id + ", owener=" + owener + ", client=" + client + ", sensor=" + sensor + '}';
    }
}
