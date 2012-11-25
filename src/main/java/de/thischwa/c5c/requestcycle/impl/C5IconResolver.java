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
package de.thischwa.c5c.requestcycle.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;

import de.thischwa.c5c.requestcycle.IconResolver;
import de.thischwa.c5c.resource.PropertiesLoader;
import de.thischwa.c5c.util.Path;
import de.thischwa.c5c.util.StringUtils;
import de.thischwa.c5c.util.VirtualFile;
import de.thischwa.c5c.util.VirtualFile.Type;

/**
 * Implementation of {@link IconResolver} which resolves the icons from the c5 FILE manager. It respects the FILE manager 
 * path settings from the properties.
 */
public class C5IconResolver implements IconResolver {
	
	/** The icon path inside the filemanager. */
	private static String iconPath = "images/fileicons";
	
	private Map<String, String> iconsPerType;
	
	public void setServletContext(ServletContext servletContext) throws RuntimeException {
		iconsPerType = new HashMap<String, String>();
		
		Path fileSystemPath = new Path(PropertiesLoader.getFilemangerPath());
		fileSystemPath.addFolder(iconPath);

		File iconFolder = new File(servletContext.getRealPath(fileSystemPath.toString()));
		if(!iconFolder.exists())
			throw new RuntimeException("C5 FILE icons folder couldn't be found!");
		
		Path urlPath;
		if(!StringUtils.isNullOrEmpty(servletContext.getContextPath())) {
			urlPath = new Path(servletContext.getContextPath());
			urlPath.addFolder(fileSystemPath.toString());
		}
		else {
			urlPath = new Path(fileSystemPath.toString());
		}
		for(File icon : iconFolder.listFiles(new IconNameFilter())) {
			String knownExtension = FilenameUtils.getBaseName(icon.getName());
			iconsPerType.put(knownExtension, urlPath.addFile(icon.getName()));
		}
		
		iconsPerType.put(IconResolver.key_directory, urlPath.addFile("_Open.png"));
		iconsPerType.put(IconResolver.key_unknown, urlPath.addFile("default.png"));
	}
	
	@Override
	public String getIconPath(VirtualFile vf) {
		if(vf.getType() == Type.DIRECTORY)
			return iconsPerType.get(IconResolver.key_directory);
		if(vf.getExtension() == null || !iconsPerType.containsKey(vf.getExtension().toLowerCase()))
			return iconsPerType.get(IconResolver.key_unknown);
		return iconsPerType.get(vf.getExtension().toLowerCase());
	}

	
	
	private class IconNameFilter implements FilenameFilter {
		
		@Override
		public boolean accept(File dir, String name) {
			return !name.contains("_") && name.endsWith("png");
		}
		
	}
}
