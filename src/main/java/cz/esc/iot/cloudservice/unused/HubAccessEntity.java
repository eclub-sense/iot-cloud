package cz.esc.iot.cloudservice.unused;

import java.util.Objects;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

@Entity
public class HubAccessEntity {
    
    @Id
    private ObjectId id;
    @Reference
    private UserEntity owener;
    @Reference
    private UserEntity client;
    @Reference
    private HubEntity hub;

    public HubAccessEntity() {
    }

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

    public HubEntity getHub() {
        return hub;
    }

    public void setHub(HubEntity hub) {
        this.hub = hub;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.owener);
        hash = 47 * hash + Objects.hashCode(this.client);
        hash = 47 * hash + Objects.hashCode(this.hub);
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
        final HubAccessEntity other = (HubAccessEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.owener, other.owener)) {
            return false;
        }
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        return Objects.equals(this.hub, other.hub);
    }

    @Override
    public String toString() {
        return "HubAccessEntity{" + "id=" + id + ", owener=" + owener + ", client=" + client + ", sensor=" + hub + '}';
    }
}
