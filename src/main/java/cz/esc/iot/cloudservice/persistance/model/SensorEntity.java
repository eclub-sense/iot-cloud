package cz.esc.iot.cloudservice.persistance.model;

import java.util.Objects;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.mongodb.morphia.annotations.Indexed;

/**
 * Morfia's entity representing sensor. Access variable can be "private" - visible only to owner,
 * "protected" - visible to owner and to users selected by owner to either read or write or
 * "public" - visible to everyone. 
 */
@Entity
public class SensorEntity {

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @Expose private String uuid;
    @Expose private String access;
    @Expose private Integer type;
    @Expose private String description;
    @Reference
    private HubEntity hub;
    @Reference
    @Expose @SerializedName("owner") private UserEntity user;

    public SensorEntity() {
    	super();
    }

    public HubEntity getHub() {
		return hub;
	}

	public void setHub(HubEntity hub) {
		this.hub = hub;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
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

    public Integer getType() {
        return type;
    }

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.uuid);
        hash = 53 * hash + Objects.hashCode(this.type);
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
        final SensorEntity other = (SensorEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return Objects.equals(this.type, other.type);
    }

	@Override
	public String toString() {
		return "SensorEntity [id=" + id + ", uuid=" + uuid + ", type=" + type + "]";
	}
}
