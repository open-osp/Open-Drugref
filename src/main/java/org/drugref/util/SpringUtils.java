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

/**
 *
 * @author jackson
 */

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * This class holds utilities used to work with spring.
 * The main usage is probably the beanFactory singleton.
 */
public class SpringUtils
{
	private static final String[] configs={"spring_config.xml"};

	public static ClassPathXmlApplicationContext beanFactory=new ClassPathXmlApplicationContext(configs);


	public static Object getBean(String beanName) {
                System.out.println("DISPLAY NAME="+beanFactory.getDisplayName());
                System.out.println("CLASSPATH_URL_PREFIX="+beanFactory.CLASSPATH_ALL_URL_PREFIX);
               // System.out.println("BEANFACOTRY                                                 "+beanFactory);
		return(beanFactory.getBean(beanName));
	}

	public static ClassPathXmlApplicationContext getClassPathXmlApplicationContext()
	{
		return(beanFactory);
	}

	/**
	 * The following is example code on how to use programmatic spring transactions.
	 */
	@Deprecated
	public static void exampleProgrammaticTransaction()
	{
		JpaTransactionManager txManager=(JpaTransactionManager)SpringUtils.getBean("txManager");
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
		try
		{
			// some database work like the following
			// emailBroadcastDao.findWaitingToSendAndLock();

			txManager.commit(status);
		}
		finally
		{
		    if (!status.isCompleted()) txManager.rollback(status);
		}
	}
}
