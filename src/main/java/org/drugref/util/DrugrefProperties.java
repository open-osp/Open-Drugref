// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster Unviersity
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------

package org.drugref.util;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

/**
 *
 * @author jackson
 */
public class DrugrefProperties extends Properties implements Serializable {
    private static Logger logger = MiscUtils.getLogger();
    private static final long serialVersionUID = -5965807410049845132L;
	private static final String DEFAULT_PROPERTIES = "/drugref.properties";
    private static DrugrefProperties drugrefProperties = new DrugrefProperties();
    private static boolean loaded = false;

	/*
	 * What database integration is being used.
	 * cdpd: Canadian Drug Product Database
	 * vigilance: Vigilance Sante https://www.vigilance.ca/home
	 */
	public enum DATA_BASE {cdpd, vigilance}

    private DrugrefProperties() {
		logger.debug("DRUGREF PROPS CONSTRUCTOR");
	}

    public static DrugrefProperties getInstance() {
	    return drugrefProperties;
    }
    	/**
	 * @return DrugrefProperties the instance of DrugrefProperties
	 */
	public static DrugrefProperties getInstance(String url) {
		readFromFile(url, drugrefProperties);
		return drugrefProperties;
	}

    private static void readFromFile(String url, Properties properties) {

		/*
		 * load local resource settings.
		 */
		try (InputStream is = DrugrefProperties.class.getResourceAsStream(DEFAULT_PROPERTIES)) {
			if(is != null) {
				properties.load(is);
			} else {
				// this should be unlikely.
				logger.error("Default properties file not found at:  " + DEFAULT_PROPERTIES);
			}
		} catch (FileNotFoundException e1) {
			logger.error("Default properties file not found at: " + DEFAULT_PROPERTIES, e1);
		} catch (IOException e1) {
			logger.error("IO while retrieving default properties: " + DEFAULT_PROPERTIES, e1);
		}

		/*
		 * load override settings
		 */
	    try(InputStream inputStream = new FileInputStream(url)) {
			if(inputStream != null) {
				properties.load(inputStream);
			}
		} catch (FileNotFoundException e) {
		    logger.error("Override properties not found at:  " + url + " Override properties may not be set.", e);
	    } catch (IOException e) {
		    logger.error("IO while retrieving override properties file: " + url + " Override properties may not be set.", e);
	    }

	}

    public String getDbUrl(){
        return getProperty("db_url");
    }
    public String getDbUser(){
        return getProperty("db_user");
    }
    public String getDbPassword(){
        return getProperty("db_password");
    }

    public String getAllDrugClasses(){
        return getProperty("all_drug_classes");
    }

    public boolean isMysql(){
        if(getDbUrl().contains("mysql"))
            return true;
        else
            return false;
    }
    public boolean isPostgres(){
        if(getDbUrl().contains("postgresql"))
            return true;
        else
            return false;
    }

	public DATA_BASE getDatbase(){
		return DATA_BASE.valueOf(getProperty("database.integration"));
	}

}

