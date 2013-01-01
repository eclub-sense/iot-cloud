package cz.cvut.felk.rest.todo.dto;

public class TodoListItemDto extends TodoItemDto {

	private String uri;
	private String lastModified;

	public String getUri() {
		return uri;
	}

	public void setUri(String url) {
		this.uri = url;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
}