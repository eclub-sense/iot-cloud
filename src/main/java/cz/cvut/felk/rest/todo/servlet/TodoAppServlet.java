package cz.cvut.felk.rest.todo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.errors.ErrorException;
import cz.cvut.felk.rest.todo.service.ServiceAdapter;
import cz.cvut.felk.rest.todo.service.impl.ServiceAdapterImpl;

public class TodoAppServlet extends HttpServlet {

	private static final long serialVersionUID = 100538579687937474L;
	
	protected ServiceAdapter adapter;
	
	@Override
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		
		this.adapter = new ServiceAdapterImpl();
	}
	
	@Override
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		try {
			adapter.create(httpRequest, httpResponse);
			
		} catch (ErrorException ex) {
			writeError(ex, httpResponse);
			
		} catch (Exception ex) {
			writeError(new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex), httpResponse);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		
		try {
			adapter.read(httpRequest, httpResponse);
			
		} catch (ErrorException ex) {
			writeError(ex, httpResponse);
			
		} catch (Exception ex) {
			writeError(new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex), httpResponse);
		}
	}

	@Override
	protected void doPut(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		try {
			adapter.update(httpRequest, httpResponse);
			
		} catch (ErrorException ex) {
			writeError(ex, httpResponse);
			
		} catch (Exception ex) {
			writeError(new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex), httpResponse);
		}

	}
	
	@Override
	protected void doDelete(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		
		try {
			adapter.delete(httpRequest, httpResponse);
			
		} catch (ErrorException ex) {
			writeError(ex, httpResponse);
			
		} catch (Exception ex) {
			writeError(new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex), httpResponse);
		}
	}	
	
	protected void writeError(ErrorException ex, HttpServletResponse httpResponse) {
		
		httpResponse.setStatus(ex.getStatusCode());
		
		Writer writer = null;
		try {
			httpResponse.setContentType("text/plain");
			
			writer = httpResponse.getWriter();
			ex.printStackTrace(new PrintWriter(writer));
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
