/*
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
 * <OSCAR TEAM>
 *
 * OscarSpringContextLoader.java
 *
 * Created on May 4, 2007, 10:42 AM
 */
package org.drugref.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class SpringPropertyConfigurer extends PropertyPlaceholderConfigurer {

    public SpringPropertyConfigurer() {
        //setProperties(ConfigUtils.getProperties());
        //System.out.println(" SpringPropertyConfigurer is called");
        setProperties(DrugrefProperties.getInstance());
    }
}
