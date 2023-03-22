/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of Startup
 *
 *
 * Startup.java
 *
 * Created on September 22, 2005, 3:13 PM
 */
package org.drugref.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.Logger;


/**
 * This ContextListener is used to Initialize classes at startup - Initialize the DBConnection Pool.
 *
 * @author Jay Gallagher
 * 
 * Modified by: Dennis Warren 
 * March 2015
 */
public class StartUp implements ServletContextListener {
	private static Logger logger = MiscUtils.getLogger();
	public StartUp() {
		// Default
	}

	public void contextInitialized(ServletContextEvent sc) {

		logger.info("contextInit called");

		String contextPath = sc.getServletContext().getServletContextName();
		String filePath = sc.getServletContext().getInitParameter("DRUGREF_PROPERTIES_PATH");
		String propertiesFilePath =  filePath + contextPath + ".properties";

		logger.info( "Looking for properties file at: " + propertiesFilePath );

		DrugrefProperties drugRefProperties = DrugrefProperties.getInstance(propertiesFilePath);

		logger.info("Initializing database: " + drugRefProperties.get("database.integration"));
		logger.info("Starting interactions checker with key"+drugRefProperties.getProperty("licence_key"));
//		InteractionsCheckerFactory.start(); //Get the file loading
		logger.info("LAST LINE IN contextInitialized");

	}

	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("DRUGREF SHUTTING DOWN");
	}

}
