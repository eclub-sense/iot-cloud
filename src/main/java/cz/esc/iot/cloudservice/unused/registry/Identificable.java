package cz.esc.iot.cloudservice.unused.registry;

public interface Identificable {

	/**
	 * @return Returns sensor's/hub's UUID
	 */
	public String getStringUuid();
	
	public int getIntUuid();
}
