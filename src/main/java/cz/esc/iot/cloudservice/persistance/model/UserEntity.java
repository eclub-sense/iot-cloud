package cz.esc.iot.cloudservice.persistance.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;

@Entity
public class UserEntity {

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @Expose private String identifier;
    @Expose private List<String> emails = new LinkedList<>();
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String username) {
        this.identifier = username;
    }

    public List<SensorEntity> getSensorEntities() {
        return sensorEntities;
    }

    public void addEmail(String mail) {
    	emails.add(mail);
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
        hash = 89 * hash + Objects.hashCode(this.identifier);
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
        if (!Objects.equals(this.identifier, other.identifier)) {
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
        return "UserEntity{" + "id=" + id + ", identifier=" + identifier + ", sensorEntities=" + sensorEntities + ", hubEntities=" + hubEntities + '}';
    }
}
