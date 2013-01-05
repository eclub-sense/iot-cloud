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
package cz.cvut.felk.rest.todo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.http.ErrorException;

public interface ServiceAdapter {

	void create(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
	
	void read(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
	
	void delete(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
	
	void update(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
}
