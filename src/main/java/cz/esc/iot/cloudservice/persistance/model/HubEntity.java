package cz.esc.iot.cloudservice.persistance.model;

import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class HubEntity {

    @Id
    private ObjectId id;
    private String uuid;
    @Reference
    private Collection<SensorEntity> sensorEntities;

    public HubEntity() {
    }

    public ObjectId getId() {
        return id;
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

    public Collection<SensorEntity> getSensorEntities() {
		return sensorEntities;
	}

	public void setSensorEntities(Collection<SensorEntity> sensorEntities) {
		this.sensorEntities = sensorEntities;
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
