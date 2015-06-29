package org.drugref.dinInteractionCheck;
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
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   
 */
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class DinRecord {
	
	String recordType = null;
	String subType = null;
	String kdc1 = null;
	String kdc2 = null;
	String kdc3 = null;
	String actcode = null;
	String route = null;
	String din  = null;
	
	public static DinRecord parseString(String str){
		DinRecord dinRecord = new DinRecord();
		LineCursor starterIndex = new LineCursor();
		starterIndex.setCurrentPosition(0);
		dinRecord.recordType = LineCursor.getStringLengthStartingHere(str,starterIndex,3);
		dinRecord.subType    = LineCursor.getStringLengthStartingHere(str,starterIndex,1);
		dinRecord.kdc1        = LineCursor.getStringLengthStartingHere(str,starterIndex,5);
		dinRecord.kdc2    = LineCursor.getStringLengthStartingHere(str,starterIndex,2);
		dinRecord.kdc3       = LineCursor.getStringLengthStartingHere(str,starterIndex,3);
		dinRecord.actcode       = LineCursor.getStringLengthStartingHere(str,starterIndex,1);
		dinRecord.route     = LineCursor.getStringLengthStartingHere(str,starterIndex,2);
		dinRecord.din     = LineCursor.getStringLengthStartingHere(str,starterIndex,8);
		return dinRecord;
	}

	public String getRecordType() {
		return recordType;
	}

	public String getSubType() {
		return subType;
	}

	public String getKdc1() {
		return kdc1;
	}

	public String getKdc2() {
		return kdc2;
	}

	public String getKdc3() {
		return kdc3;
	}

	public String getActcode() {
		return actcode;
	}

	public String getRoute() {
		return route;
	}

	public String getDin() {
		return din;
	}
	
	@Override
    public String toString(){
		return(ReflectionToStringBuilder.toString(this));
	}
	

}
