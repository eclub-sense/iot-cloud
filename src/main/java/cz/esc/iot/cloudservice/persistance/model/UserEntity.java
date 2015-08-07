package cz.esc.iot.cloudservice.persistance.model;

import java.util.List;
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
    private String password;
    @Reference
    private List<SensorEntity> sensorEntities;
    @Reference
    private List<HubEntity> hubEntities;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SensorEntity> getSensorEntities() {
        return sensorEntities;
    }

    public void setSensorEntities(List<SensorEntity> sensorEntities) {
        this.sensorEntities = sensorEntities;
    }

    public List<HubEntity> getHubEntities() {
        return hubEntities;
    }

    public void setHubEntities(List<HubEntity> hubEntities) {
        this.hubEntities = hubEntities;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.username);
        hash = 89 * hash + Objects.hashCode(this.password);
        hash = 89 * hash + Objects.hashCode(this.sensorEntities);
        hash = 89 * hash + Objects.hashCode(this.hubEntities);
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
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.sensorEntities, other.sensorEntities)) {
            return false;
        }
        if (!Objects.equals(this.hubEntities, other.hubEntities)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", username=" + username + ", password=" + password + ", sensorEntities=" + sensorEntities + ", hubEntities=" + hubEntities + '}';
    }
}
