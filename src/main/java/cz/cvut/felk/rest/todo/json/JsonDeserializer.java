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
package cz.cvut.felk.rest.todo.json;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import cz.cvut.felk.rest.todo.http.ErrorException;
import cz.cvut.felk.rest.todo.http.content.ContentAdapter;

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
