/*
 * Copyright (c) 2001-2002. McMaster University Hamilton Ontario. All Rights Reserved. *
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
 * Created on May 4, 2007, 10:42 AM
 */
package org.drugref.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author jackson
 */
public class MiscUtils {
//convert integer arrayList to string arrayList
    public static ArrayList toStringArrayList(List intList) {
        ArrayList<String> strArrayList = new ArrayList<String>();

        for (int i = 0; i < intList.size(); i++) {
            String element = intList.get(i).toString();
            strArrayList.add(element);
        }
        return strArrayList;
    }

    public static ArrayList toIntegerArrayList(List strList) {
        ArrayList<Integer> intArrayList = new ArrayList<Integer>();

        for (int i = 0; i < strList.size(); i++) {
            Integer element = Integer.parseInt(strList.get(i).toString());
            System.out.println("in  toIntegerArrayList: "+element);
            intArrayList.add(element);
        }
        return intArrayList;
    }

    public static Logger getLogger(){
    		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    		String caller = ste[2].getClassName();
    		return(Logger.getLogger(caller));
    }

    public static boolean isStringToInteger(String s){
        if(s==null) return false;

        boolean retBool=false;
        Pattern p1=Pattern.compile("\\d+");
        Matcher m1=p1.matcher(s);
        if(m1.find()){
            String numStr=s.substring(m1.start(), m1.end());
            String restStr=s.replace(numStr, "").trim();
            if(restStr!=null&&restStr.length()>0)
                retBool=false;
            else
                retBool=true;
        }else
            retBool=false;

        return retBool;

    }


}
