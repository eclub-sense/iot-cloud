package cz.esc.iot.cloudservice.siren;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.Action;

public class Actions {

	@Expose private List<Action> actions = new ArrayList<>();

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}
