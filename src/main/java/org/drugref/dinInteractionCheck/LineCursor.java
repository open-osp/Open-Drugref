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
import org.apache.log4j.Logger;
import org.drugref.util.MiscUtils;

public class LineCursor {
	private static final Logger logger = MiscUtils.getLogger();

	int currentPosition = 0;

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	public static String getStringLengthStartingHere(String str,LineCursor starterIndex, int length){
		
		String retval = null;
		if (str.length() == starterIndex.getCurrentPosition()){
			return null;
		}
		try{
			
			int endPosition = starterIndex.getCurrentPosition()+length;
			if(endPosition > str.length()){
				endPosition = str.length();
			}
			retval = str.substring(starterIndex.getCurrentPosition(),endPosition);
			starterIndex.setCurrentPosition(endPosition);
		}catch(Exception e){
			logger.error("String = "+str+" index "+starterIndex,e);
		}
		return retval;
	}
}