package cz.esc.iot.cloudservice.persistance.model;

import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class UserEntity {

    @Id
    private ObjectId id;
    private String username;
    private String hash;
    @Reference
    private Collection<SensorEntity> sensorEntities;
    @Reference
    private Collection<HubEntity> hubEntities;

    public UserEntity() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Collection<SensorEntity> getSensorEntities() {
        return sensorEntities;
    }

    public void setSensorEntities(Collection<SensorEntity> sensorEntities) {
        this.sensorEntities = sensorEntities;
    }

    public Collection<HubEntity> getHubEntities() {
        return hubEntities;
    }

    public void setHubEntities(Collection<HubEntity> hubEntities) {
        this.hubEntities = hubEntities;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.username);
        hash = 31 * hash + Objects.hashCode(this.hash);
        hash = 31 * hash + Objects.hashCode(this.sensorEntities);
        hash = 31 * hash + Objects.hashCode(this.hubEntities);
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
        final UserEntity other = (UserEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        if (!Objects.equals(this.sensorEntities, other.sensorEntities)) {
            return false;
        }
        return Objects.equals(this.hubEntities, other.hubEntities);
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", username=" + username + ", hash=" + hash + ", sensorEntities=" + sensorEntities + ", hubEntities=" + hubEntities + '}';
    }
}
