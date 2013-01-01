package cz.cvut.felk.rest.todo.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import cz.cvut.felk.rest.todo.core.content.ContentAdapter;
import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;

public class JsonDeserializer<T> implements ContentAdapter<InputStream, T>{

	private final Class<? extends T> clazz;
	
	public JsonDeserializer(Class<? extends T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	@Override
	public T transform(InputStream in) throws ErrorException {
		try {
		    return (new Gson()).fromJson(new InputStreamReader(in), clazz);
		    			
		} catch (JsonSyntaxException e) {
			throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST, e);
			
		} catch (JsonIOException e) {
			throw new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);			
		}
	}
//	
//	public static final void write(int code, ContentDescriptor<?> content, HttpServletResponse httpResponse) throws ErrorException {
//		
//		httpResponse.setStatus(code);
//
//		if ((HttpServletResponse.SC_CREATED == code)) {
//			httpResponse.setHeader(ContentDescriptor.META_LOCATION, content.getContext() + content.getUri());
//		} 
//		
//		for (String metaName : content.getMetaNames()) {
//			Object metaValue = content.getMeta(metaName);
//			if (metaValue != null) {
//				if (metaValue instanceof Date) {
//					httpResponse.setHeader(metaName, ((Date)metaValue).toGMTString());	
//				} else {
//					httpResponse.setHeader(metaName, metaValue.toString());
//				}
//			}
//		}
//		
//		if ((content.getBody() == null) || (HttpServletResponse.SC_NOT_MODIFIED == code)) {
//			return;
//		}
//		
//		if (content.getBody() instanceof TodoItemDto) {
//			httpResponse.setContentType(ContentDescriptor.CT_ITEM);
//			
//		} else if (content.getBody() instanceof TodoItemDto[]) {
//			httpResponse.setContentType(ContentDescriptor.CT_LIST);
//		}
//		
//		Writer writer  = null;
//		try {
//			final Gson gson = new Gson();
//			final String json = gson.toJson(content.getBody()); 
//			
//			httpResponse.setContentLength(json.getBytes().length);
//			
//			writer = httpResponse.getWriter();
//			writer.write(json);
//			
//		} catch (IOException e) {
//			throw new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
//			
//		} finally {
//			if (writer != null) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					
//				}
//			}
//		}
//	}

}
