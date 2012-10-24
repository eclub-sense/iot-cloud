package cz.cvut.felk.rest.todo.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cz.cvut.felk.rest.todo.ContentDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;

public class HttpResponse<T> {


	public static final void write(int code, ContentDescriptor<?> content, HttpServletResponse httpResponse) throws ErrorException {
		
		httpResponse.setStatus(code);

		if ((HttpServletResponse.SC_CREATED == code)) {
			httpResponse.setHeader(ContentDescriptor.META_LOCATION, content.getContextUrl() + content.getUrl());
		} 
		
		for (String metaName : content.getMetaNames()) {
			Object metaValue = content.getMeta(metaName);
			if (metaValue != null) {
				if (metaValue instanceof Date) {
					httpResponse.setHeader(metaName, ((Date)metaValue).toGMTString());	
				} else {
					httpResponse.setHeader(metaName, metaValue.toString());
				}
			}
		}
		
		if ((content.getBody() == null) || (HttpServletResponse.SC_NOT_MODIFIED == code)) {
			return;
		}
		
		if (content.getBody() instanceof TodoItemDto) {
			httpResponse.setContentType(ContentDescriptor.CT_ITEM);
			
		} else if (content.getBody() instanceof TodoItemDto[]) {
			httpResponse.setContentType(ContentDescriptor.CT_LIST);
		}
		
		Writer writer  = null;
		try {
			final Gson gson = new Gson();
			final String json = gson.toJson(content.getBody()); 
			
			httpResponse.setContentLength(json.getBytes().length);
			
			writer = httpResponse.getWriter();
			writer.write(json);
			
		} catch (IOException e) {
			throw new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
			
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					
				}
			}
		}
	}    
}
