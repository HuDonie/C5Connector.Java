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
 *  - GNU Lesser General Public License Version 3.0 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 *  - Mozilla Public License Version 2.0 or later (the "MPL")
 *    http://www.mozilla.org/MPL/2.0/
 * 
 * == END LICENSE ==
 */
package de.thischwa.c5c.requestcycle.response.mode;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.thischwa.c5c.requestcycle.response.Response;

/**
 * Holds the data for a CreateFolder response.
 */
public final class CreateFolder extends Response {

	private String parentUrlPath;
	
	private String folderName;
	
	public CreateFolder(String parentUrlPath, String folderName) {
		this.parentUrlPath = parentUrlPath;
		this.folderName = folderName;
	}

	@JsonProperty("Parent")
	public String getParentUrlPath() {
		return parentUrlPath;
	}
	
	@JsonProperty("Name")
	public String getFolderName() {
		return folderName;
	}
}
