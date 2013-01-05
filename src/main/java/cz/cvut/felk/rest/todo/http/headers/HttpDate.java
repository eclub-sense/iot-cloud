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
package cz.cvut.felk.rest.todo.http.headers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * HTTP applications have historically allowed three different formats for the representation of date/time stamps:
 * <ul>
 *  <li>Sun, 06 Nov 1994 08:49:37 GMT  ; RFC 822, updated by RFC 1123</li>
 *  <li>Sunday, 06-Nov-94 08:49:37 GMT ; RFC 850, obsoleted by RFC 1036</li>
 *  <li>Sun Nov  6 08:49:37 1994       ; ANSI C's asctime() format</li>
 * <p>
 *   The first format is preferred as an Internet standard and represents a fixed-length 
 *   subset of that defined by RFC 1123 [8] (an update to RFC 822 [9]). The second format
 *   is in common use, but is based on the obsolete RFC 850 [12] date format and lacks a four-digit year. 
 *   HTTP/1.1 clients and servers that parse the date value MUST accept all three formats 
 *   (for compatibility with HTTP/1.0), though they MUST only generate 
 *   the RFC 1123 format for representing HTTP-date values in header fields. 
 * </p>
 *
 */
public class HttpDate {

	/**
     * US locale - all HTTP dates use English
     */
    public final static Locale LOCALE = Locale.US;

    /**
     * GMT timezone - all HTTP dates are GMT
     */
    public final static TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
	
    /**
     * RFC 1123 date pattern. e.g.  Sun, 06 Nov 1994 08:49:37 GMT
     */
	public final static String RFC1123_PATTERN = "EEE, dd MMM yyyyy HH:mm:ss zzz";
	
	/**
	 * RFC 850 date pattern. e.g. Sunday, 06-Nov-94 08:49:37 GMT
	 */
    public final static String RFC850_PATTERN = "EEEEEEEEE, dd-MMM-yy HH:mm:ss zzz";

    /**
     * ANSI C's asctime() pattern. e.g. Sun Nov  6 08:49:37 1994 
     */
    public final static String ASCTIME_PATTERN = "EEE MMM d HH:mm:ss yyyyy";

    /**
     * RFC 1123 date format
     */
	public final static DateFormat RFC1123_FORMAT = new SimpleDateFormat(RFC1123_PATTERN, LOCALE);
	
	/**
	 * RFC 850 date format
	 */
	public final static DateFormat RFC850_FORMAT = new SimpleDateFormat(RFC850_PATTERN, LOCALE);
	
	/**
	 * ASI C's asctime() date format
	 */
	public final static DateFormat ASCTIME_FORMAT = new SimpleDateFormat(ASCTIME_PATTERN, LOCALE);
	
	static {
		RFC1123_FORMAT.setTimeZone(TIME_ZONE);
		RFC850_FORMAT.setTimeZone(TIME_ZONE);
		ASCTIME_FORMAT.setTimeZone(TIME_ZONE);
	}
}
