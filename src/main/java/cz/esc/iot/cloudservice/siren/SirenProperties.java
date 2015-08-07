package cz.esc.iot.cloudservice.siren;

public class SirenProperties {
	private String id;
	private String uuid;
	private String name;
	
	public String getUuid() {
		return id;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "SirenProperties [id=" + id + ", uuid=" + uuid + ", name=" + name + "]";
	}
}
