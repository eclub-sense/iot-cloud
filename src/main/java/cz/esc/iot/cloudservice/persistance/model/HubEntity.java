package cz.esc.iot.cloudservice.persistance.model;

import java.util.Objects;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;
import org.mongodb.morphia.annotations.Indexed;

/**
 * Morfia's entity representing hub.
 */
@Entity
public class HubEntity {

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @Expose private String uuid;
    @Expose private String status;
    @Reference
    private UserEntity user;

    public HubEntity() {
    	super();
    }

    public ObjectId getId() {
        return id;
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public void setId(ObjectId id) {
        this.id = id;
    }

	public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
	@Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        hash = 11 * hash + Objects.hashCode(this.uuid);
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
        final HubEntity other = (HubEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.uuid, other.uuid);
    }

    @Override
    public String toString() {
        return "HubEntity{" + "id=" + id + ", uuid=" + uuid + '}';
    }
}
