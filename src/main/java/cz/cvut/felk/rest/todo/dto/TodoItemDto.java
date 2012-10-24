package cz.cvut.felk.rest.todo.dto;

public class TodoItemDto implements Validator {

	public enum State { New, WorkInProgress, Closed};
	
	private String description;
	private State state;
	
	public boolean validate() {
		return (state != null) && (description != null);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
