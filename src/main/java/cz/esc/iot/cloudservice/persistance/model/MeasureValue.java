package cz.esc.iot.cloudservice.persistance.model;

public class MeasureValue {

	private String name;
	private String unit;
	
	public MeasureValue() {
		super();
	}
	
	public MeasureValue(String name, String unit) {
		super();
		this.name = name;
		this.unit = unit;
	}

	public String getName() {
		return name;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "MeasureValue [name=" + name + ", unit=" + unit + "]";
	}
}
