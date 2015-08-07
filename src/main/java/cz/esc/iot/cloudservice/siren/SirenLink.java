package cz.esc.iot.cloudservice.siren;

import java.util.List;

public class SirenLink {
	
	private String title;
	private List<String> rel;
	private String href;
	
	public String getHref() {
		return href;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public String toString() {
		return "Link [rel=" + rel + ", href=" + href + "]";
	}
}
