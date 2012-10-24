package cz.cvut.felk.rest.todo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.errors.ErrorException;

public interface ServiceAdapter {

	void create(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
	
	void read(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
	
	void delete(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
	
	void update(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException;
}
