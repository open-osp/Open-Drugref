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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
/**
 *
 * @author jackson
 */
public class DrugrefProperties extends Properties{
    //private static final long serialVersionUID = -5965807410049845132L; ??
    private static DrugrefProperties drugrefProperties = new DrugrefProperties();
    private static boolean loaded = false;

    static{
        try{
            System.out.println("static initializer of drugrefproperties");
            readFromFile("/drugref.properties",drugrefProperties);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    	/**
	 * @return DrugrefProperties the instance of DrugrefProperties
	 */
	public static DrugrefProperties getInstance() {
            Enumeration em=drugrefProperties.propertyNames();
            while(em.hasMoreElements()){
                String ss=(String)em.nextElement();
                System.out.println("property="+ss);
                System.out.println("value="+drugrefProperties.getProperty(ss));

        }
		return drugrefProperties;
	}
        private static void readFromFile(String url, Properties p) throws IOException {
		InputStream is = DrugrefProperties.class.getResourceAsStream(url);
		if (is == null) is = new FileInputStream(url);

		try {
			p.load(is);
		} finally {
			is.close();
		}
	}

        private DrugrefProperties() {
		System.out.println("DRUGREF PROPS CONSTRUCTOR");
	}


	public void loader(InputStream propertyStream) {
		if (!loaded) {
			try {
				load(propertyStream);
				propertyStream.close();
				loaded = true;
			} catch (IOException ex) {
				System.err.println("IO Error: " + ex.getMessage());
			}
		}
	}

	public void loader(String propFileName) throws java.io.FileNotFoundException {
		if (!loaded) {
			FileInputStream fis2 = new FileInputStream(propFileName);
			try {
				load(fis2);
				fis2.close();
				loaded = true;
			} catch (IOException ex) {
				System.err.println("IO Error: " + ex.getMessage());
			}
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


}

