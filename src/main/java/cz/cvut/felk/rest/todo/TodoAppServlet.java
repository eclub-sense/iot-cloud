/*
 *  Copyright 2012 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cz.cvut.felk.rest.todo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.sprintapi.api.ErrorException;
import org.sprintapi.api.ResourceDescriptor;
import org.sprintapi.api.http.HttpServlet;

import cz.cvut.felk.rest.todo.dao.TodoListDao;
import cz.cvut.felk.rest.todo.dao.TodoListInMemoryDao;
import cz.cvut.felk.rest.todo.resource.TodoListResource;
import cz.cvut.felk.rest.todo.resource.TodoResource;

public class TodoAppServlet extends HttpServlet {

	private static final long serialVersionUID = -5803664054963878143L;

	protected static final String URL_ITEM = "/item/";
	
	private TodoListDao todoListDao;
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.todoListDao = new TodoListInMemoryDao();
	}
	
	@Override
	protected ResourceDescriptor resolve(String uri) {
		if (uri == null) {
			return null;
		}
		
		if (URL_ITEM.equals(uri)) {
			return new TodoListResource(todoListDao);	
		}
		 
		if (todoListDao.read(uri) != null) {
			return new TodoResource(todoListDao);
		}
		return null;
	}

	@Override
	protected void handleError(ErrorException ex, HttpServletResponse httpResponse) {
		httpResponse.setStatus(ex.getStatusCode());
		if (ex.getStatusCode() >= 500) { 
			log(ex.getMessage(), ex.getCause());
		}
	}
}
