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
package de.thischwa.c5c.util;

import de.thischwa.c5c.Constants;

/**
 * Util object to encapsulate the handling of folder, name and the extension of a file path.
 */
public class VirtualFile {

	public enum Type { directory, file }

	/** The type. */
	private Type type;

	/** The folder. */
	private String folder;

	/** The full path. */
	private String fullPath;

	/** The extension. */
	private String extension;

	/** The name. */
	private String name;

	/**
	 * Instantiates a new virtual file.
	 *
	 * @param fullPath the full path
	 * @param isDir the is dir
	 */
	public VirtualFile(String fullPath, boolean isDir) {
		this.fullPath = fullPath;
		if(!fullPath.startsWith(Constants.separator))
			fullPath = Constants.separator.concat(fullPath);
		if (isDir && !fullPath.endsWith(Constants.separator)) // simple normalization
			fullPath += Constants.separator;
		
		type = Type.file;
		String cleanPath = fullPath;
		if(cleanPath.endsWith(Constants.separator))
			cleanPath = cleanPath.substring(0, cleanPath.length()-1);
		if (fullPath.endsWith(Constants.separator)) {
			folder = fullPath;
			type = Type.directory;
		} else {
			int extPos = fullPath.lastIndexOf(".");
			if (extPos == -1)
				extension = null;
			else
				extension = fullPath.substring(extPos + 1);
		}
		
		int pathPos = cleanPath.lastIndexOf(Constants.separator);
		if (pathPos != -1) {
			folder = cleanPath.substring(0, pathPos + 1);
			name = cleanPath.substring(pathPos + 1, cleanPath.length());
		}
	}

	/**
	 * Instantiates a new virtual file.
	 *
	 * @param fullPath the full path
	 */
	public VirtualFile(String fullPath) {
		this(fullPath, false);
	}

	/**
	 * Gets the full path.
	 *
	 * @return the full path
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * Gets the folder.
	 *
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the extension.
	 *
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
}
