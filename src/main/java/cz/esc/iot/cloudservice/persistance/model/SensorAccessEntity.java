package cz.esc.iot.cloudservice.persistance.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;

/**
 * Morfia's entity where info about sharing is stored. Permission can by
 * either "read" or "write".
 */
@Entity
public class SensorAccessEntity {

    @Id
    private ObjectId id;
    @Reference
    @Expose private UserEntity owner;
    @Reference
    private UserEntity user;
    @Expose private String permission;
    @Reference
    @Expose private SensorEntity sensor;

    public SensorAccessEntity() {
    	
    }
    
    public SensorAccessEntity(UserEntity owner, UserEntity user, String permission, SensorEntity sensor) {
    	this.sensor = sensor;
    	this.permission = permission;
    	this.owner = owner;
    	this.user = user;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public UserEntity getOwener() {
        return owner;
    }

    public void setOwener(UserEntity owner) {
        this.owner = owner;
    }

    public SensorEntity getSensor() {
        return sensor;
    }

    public void setSensor(SensorEntity sensor) {
        this.sensor = sensor;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((sensor == null) ? 0 : sensor.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorAccessEntity other = (SensorAccessEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (sensor == null) {
			if (other.sensor != null)
				return false;
		} else if (!sensor.equals(other.sensor))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SensorAccessEntity [id=" + id + ", owner=" + owner + ", user=" + user + ", permission=" + permission
				+ ", sensor=" + sensor + "]";
	}
}
