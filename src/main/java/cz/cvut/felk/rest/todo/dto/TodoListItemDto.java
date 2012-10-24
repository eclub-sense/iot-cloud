package cz.cvut.felk.rest.todo.dto;

public class TodoListItemDto extends TodoItemDto {

	private String url;
	private String lastModified;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
}
