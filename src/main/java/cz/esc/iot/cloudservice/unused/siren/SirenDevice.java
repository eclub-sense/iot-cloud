package cz.esc.iot.cloudservice.unused.siren;

import java.util.List;

public class SirenDevice {
	private SirenProperties properties;
	private List<SirenLink> links;
	
	public String getId() {
		return properties.getId();
	}
	
	public String getUuid() {
		return properties.getUuid();
	}
	
	public String getName() {
		return properties.getName();
	}

	public String getHref() {
		return links.get(0).getHref();
	}

	@Override
	public String toString() {
		return "Entity [properties=" + properties + ", links=" + links + "]";
	}
}
