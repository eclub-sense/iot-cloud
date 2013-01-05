/*
 *  Copyright 2012 sprintapi.org
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

import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.core.ResourceDescriptor;
import cz.cvut.felk.rest.todo.http.ErrorException;
import cz.cvut.felk.rest.todo.http.servlet.HttpServlet;
import cz.cvut.felk.rest.todo.resource.TodoListResource;

public class TodoAppServlet extends HttpServlet {

	private static final long serialVersionUID = -5803664054963878143L;

	@Override
	protected ResourceDescriptor resolve(String uri) {
		return new TodoListResource();
	}

	@Override
	protected void handleError(ErrorException ex, HttpServletResponse httpResponse) {
		httpResponse.setStatus(ex.getStatusCode());
		
		log("error", ex.getCause());
		
//		Writer writer = null;
//		try {
//			httpResponse.setContentType("text/plain");
//			
//			writer = httpResponse.getWriter();
//			ex.printStackTrace(new PrintWriter(writer));
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//			
//		} finally {
//			if (writer != null) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

}
