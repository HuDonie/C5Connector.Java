/*
 * C5Connector.Java - The Java backend for the filemanager of corefive.
 * It provides a simple object for creating an editor instance.
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

import javax.servlet.http.HttpServletRequest;

/**
 * TODO document me
 */
public interface C5FileCapability {
	
	/**
	 * The Enum CAPABILITY.
	 */
	public enum CAPABILITY {select, delete, rename, download};
	
	/**
	 * Gets the capabilities.
	 *
	 * @param req the req
	 * @param urlPath the url path
	 * @return the capabilities
	 */
	public CAPABILITY[] getCapabilities(final HttpServletRequest req, final String urlPath);
}
