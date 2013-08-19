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
package de.thischwa.c5c.resource;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static object for managing the default and user-defined properties files.<br/>
 * The files are loaded in the following order:
 * <ol>
 * <li>The default properties.</li>
 * <li>The user-defined properties <code>c5connector.properties</code> if present.</li>
 * </ol> 
 * Static wrapper methods are provided for all properties.<br/>
 * Please note: The user-defined properties <em>override</em> the default one.<br/> 
 * Moreover, you can set properties programmatically too ({@link #setProperty(String, String)}), 
 * but make sure to override them <em>before</em> the first call of any property.
 */
public class PropertiesLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
	
	private static final String DEFAULT_FILENAME = "default.properties";
	
	private static final String LOCAL_PROPERTIES = "/c5connector.properties";
	
	private static Properties properties = new Properties();
	
	private static Locale defaultLocale;

	static {
		// 1. load library defaults
		InputStream inDefault = PropertiesLoader.class.getResourceAsStream(DEFAULT_FILENAME);

		if (inDefault == null) {
			logger.error("{} not found", DEFAULT_FILENAME);
			throw new RuntimeException(DEFAULT_FILENAME + " not found");
		} else {
			if (!(inDefault instanceof BufferedInputStream))
				inDefault = new BufferedInputStream(inDefault);
			try {
				properties.load(inDefault);
				logger.info("{} successful loaded", DEFAULT_FILENAME);
			} catch (Exception e) {
				String msg = "Error while processing " + DEFAULT_FILENAME;
				logger.error(msg);
				throw new RuntimeException(msg, e);
			} finally {
				IOUtils.closeQuietly(inDefault);
			}
		}

		// 2. load user defaults if present
		InputStream inUser = PropertiesLoader.class.getResourceAsStream(LOCAL_PROPERTIES);
		if (inUser == null) {
			logger.info("{} not found", LOCAL_PROPERTIES);
		} else {
			if (!(inUser instanceof BufferedInputStream))
				inUser = new BufferedInputStream(inUser);
			try {
				properties.load(inUser);
				inUser.close();
				logger.info("{} successful loaded", LOCAL_PROPERTIES);
			} catch (Exception e) {
				String msg = "Error while processing " + LOCAL_PROPERTIES;
				logger.error(msg);
				throw new RuntimeException(msg, e);
			} finally {
				IOUtils.closeQuietly(inUser);
			}
		}
		
		// 3. build the default locale
		defaultLocale = new Locale(properties.getProperty("default.language"));
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 *
	 * @param key the key
	 * @return the property
	 * @see Properties#getProperty(String)
	 */
	public static String getProperty(final String key) {
		return properties.getProperty(key);
	}

	/**
	 * Sets the property with the specified key in this property list.
	 *
	 * @param key the key
	 * @param value the value
	 * @see Properties#setProperty(String, String)
	 */
	public static void setProperty(final String key, final String value) {
		properties.setProperty(key, value);
	}

	/**
	 * Returns <code>default.encoding</code> property.
	 *
	 * @return the default encoding
	 */
	public static String getDefaultEncoding() {
		return properties.getProperty("default.encoding");
	}
	
	/**
	 * Obtains the default locale dependent on the <code>default.language</code> property.
	 * 
	 * @return the default locale
	 */
	public static Locale getDefaultLocale() {
		return defaultLocale;
	}

	/**
	 * Returns <code>connector.resourceType.archive.extensions.allowed</code> property
	 *
	 * @return the archive resource type allowed extensions
	 */
	public static String getArchiveResourceTypeAllowedExtensions() {
		return properties.getProperty("connector.resourceType.archive.extensions.allowed");
	}

	/**
	 * Returns <code>connector.resourceType.archive.extensions.denied</code> property
	 *
	 * @return the archive resource type denied extensions
	 */
	public static String getArchiveResourceTypeDeniedExtensions() {
		return properties.getProperty("connector.resourceType.archive.extensions.denied");
	}
	
	/**
	 * Returns <code>connector.resourceType.doc.extensions.allowed</code> property
	 *
	 * @return the doc resource type allowed extensions
	 */
	public static String getDocResourceTypeAllowedExtensions() {
		return properties.getProperty("connector.resourceType.doc.extensions.allowed");
	}

	/**
	 * Returns <code>connector.resourceType.doc.extensions.denied</code> property
	 *
	 * @return the doc resource type denied extensions
	 */
	public static String getDocResourceTypeDeniedExtensions() {
		return properties.getProperty("connector.resourceType.doc.extensions.denied");
	}

	/**
	 * Returns <code>connector.resourceType.image.extensions.allowed</code> property
	 *
	 * @return the image resource type allowed extensions
	 */
	public static String getImageResourceTypeAllowedExtensions() {
		return properties.getProperty("connector.resourceType.image.extensions.allowed");
	}

	/**
	 * Returns <code>connector.resourceType.image.extensions.denied</code> property
	 *
	 * @return the image resource type denied extensions
	 */
	public static String getImageResourceTypeDeniedExtensions() {
		return properties.getProperty("connector.resourceType.image.extensions.denied");
	}

	/**
	 * Returns <code>connector.resourceType.other.extensions.allowed</code> property
	 *
	 * @return the other resource type allowed extensions
	 */
	public static String getOtherResourceTypeAllowedExtensions() {
		return properties.getProperty("connector.resourceType.other.extensions.allowed");
	}

	/**
	 * Returns <code>connector.resourceType.other.extensions.denied</code> property
	 *
	 * @return the oher resource type denied extensions
	 */
	public static String getOherResourceTypeDeniedExtensions() {
		return properties.getProperty("connector.resourceType.other.extensions.denied");
	}
	
	/**
	 * Returns <code>connector.filemanagerPath</code> property
	 *
	 * @return the filemanger path
	 */
	public static String getFilemangerPath() {
		return properties.getProperty("connector.filemanagerPath");
	}

	/**
	 * Returns <code>connector.secureImageUploads</code> property
	 *
	 * @return true, if is secure image uploads
	 */
	public static boolean isSecureImageUploads() {
		return Boolean.valueOf(properties.getProperty("connector.secureImageUploads"));
	}

	/**
	 * Returns <code>connector.userActionImpl</code> property
	 *
	 * @return the user action impl
	 */
	public static String getUserActionImpl() {
		return properties.getProperty("connector.userActionImpl");
	}

	/**
	 * Returns <code>connector.iconResolverImpl</code> property
	 *
	 * @return the icon resolver impl
	 */
	public static String getIconResolverImpl() {
		return properties.getProperty("connector.iconResolverImpl");
	}

	/**
	 * Returns <code>jii.impl</code> property
	 *
	 * @return the dimension provider impl
	 */
	public static String getDimensionProviderImpl() {
		return properties.getProperty("jii.impl");
	}

	/**
	 * Returns <code>connector.userPathBuilderImpl</code> property
	 *
	 * @return the user action impl
	 */
	public static String getUserPathBuilderImpl() {
		return properties.getProperty("connector.userPathBuilderImpl");
	}

	/**
	 * Returns <code>connector.v</code> property
	 *
	 * @return the message resolver impl
	 */
	public static String getMessageResolverImpl() {
		return properties.getProperty("connector.messageResolverImpl");
	}

	/**
	 * Returns <code>connector.forceSingleExtension</code> property
	 *
	 * @return true, if is force single extension
	 */
	public static boolean isForceSingleExtension() {
		return Boolean.valueOf(properties.getProperty("connector.forceSingleExtension"));
	}

	/**
	 * Gets the connector impl.
	 *
	 * @return <code>connector.impl</code> property
	 */
	public static String getConnectorImpl() {
		return properties.getProperty("connector.impl");
	}
	
	/**
	 * Gets the default capacity.
	 *
	 * @return <code>connector.capabilities</code> property
	 */
	public static String getDefaultCapacity() {
		return properties.getProperty("connector.capabilities");
	} 
	
	/**
	 * Gets the file capability impl.
	 *
	 * @return <code>connector.fileCapabilityImpl</code> property
	 */
	public static String getFileCapabilityImpl() {
		return properties.getProperty("connector.fileCapabilityImpl");
	} 
	
	/**
	 * Returns <code>connector.imagePreview</code> property
	 *
	 * @return true, if successful
	 */
	public static boolean imageViewingEnabled() {
		return Boolean.valueOf(properties.getProperty("connector.imagePreview"));
	}
}
