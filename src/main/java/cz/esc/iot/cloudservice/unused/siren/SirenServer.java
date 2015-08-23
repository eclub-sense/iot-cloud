package cz.esc.iot.cloudservice.unused.siren;

import java.util.List;

public class SirenServer {  
	private SirenProperties properties;
    private List<SirenDevice> entities;
    
	public String getName() {
		return properties.getName();
	}
	
	public int getSensorCount() {
		return entities.size();
	}
	
	public List<SirenDevice> getEntities() {
		return entities;
	}
	
	@Override
	public String toString() {
		return "Server [properties=" + properties + ", entities=" + entities + "]";
	}
}
