/*
 * C5Connector.Java - The Java backend for the filemanager of corefive.
 * It's a bridge between the filemanager and a storage backend and 
 * works like a transparent VFS or proxy.
 * Copyright (C) Thilo Schwarz
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 */
package de.thischwa.c5c.requestcycle;

import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.thischwa.c5c.Constants;
import de.thischwa.c5c.resource.PropertiesLoader;
import de.thischwa.c5c.util.StringUtils;
import de.thischwa.jii.IDimensionProvider;

/**
 * This container maintenance objects per request. It relies on {@link ThreadLocal}.<br/>
 * Provided Objects:<ul>
 * <li>The {@link Locale}: It is grabbed from the query string of the referrer. That's the location set by the filemanager.</li>
 * <li> The {@link HttpServletRequest}.</li>
 * </ul>
 * <i>Hint:</i> The implementation of the {@link IDimensionProvider} will be initialized 
 * and provided for each request because there isn't any a that it is thread-save.
 */
public class RequestData {
	private static Logger logger = LoggerFactory.getLogger(RequestData.class);
	
	private static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	
	private static ThreadLocal<IDimensionProvider> dimensionProvider = new ThreadLocal<IDimensionProvider>();
	
	private static ThreadLocal<Locale> locale = new ThreadLocal<Locale>();

	/**
	 * Initializes the current request cycle.
	 * 
	 * @param req
	 *            current user request instance
	 */
	public static void beginRequest(final HttpServletRequest req) {
		if (req == null)
			throw new NullPointerException("the request cannot be null");
		RequestData.request.set(req);
		
		// init the dimension provider
		try {
			String className = PropertiesLoader.getDimensionProviderImpl();
			Class<?> cls = Class.forName(className);
			dimensionProvider.set((IDimensionProvider)cls.newInstance());
		} catch (Throwable e) {
			throw new RuntimeException("Couldn't initialize the dimension provider.", e);
		}
		

		String referer = req.getHeader("referer");
		if(StringUtils.isNullOrEmptyOrBlank(referer))
			locale.set(req.getLocale());
		try {
			URL url = new URL(referer);
			Map<String, String> params = StringUtils.divideAndDecodeQueryString(url.getQuery());
			String langCode = params.get("langCode");
			if(StringUtils.isNullOrEmptyOrBlank(langCode)) {
				logger.warn("Couldn't analyse the locale from the referer to use, take the default one.");
				locale.set(Constants.DEFAULT_LOCALE);
			} else {
				locale.set(new Locale(langCode.toLowerCase()));
			}
		} catch (Exception e) {
			logger.warn("Couldn't analyse the locale to use, take the default one.");
			locale.set(Constants.DEFAULT_LOCALE);
		}
	}

	/**
	 * Returns the current user request instance.
	 * 
	 * @return the current user request instance
	 */
	public static HttpServletRequest getRequest() {
		return request.get();
	}
	
	/**
	 * Gets the dimension provider.
	 *
	 * @return the dimension provider
	 */
	public static IDimensionProvider getDimensionProvider() {
		return dimensionProvider.get();
	}
	
	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public static Locale getLocale() {
		return locale.get();
	}

	/**
	 * Terminates the current request cycle. <br />
	 * <strong>Important: To prevent memory leaks, make sure that this method is called at the end of the current request cycle!</strong>
	 */
	public static void endRequest() {
		request.remove();
		dimensionProvider.remove();
		locale.remove();
	}
}
