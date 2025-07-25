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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class JpaUtils {

    private static EntityManagerFactory entityManagerFactory = (EntityManagerFactory) SpringUtils.getBean("entityManagerFactory");
    /**
     * This method will close the entity manager.
     * Any active transaction will be rolled back.
     */
    public static void close(EntityManager entityManager) {
        EntityTransaction tx = entityManager.getTransaction();
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
        entityManager.close();
    }

    public static EntityManager createEntityManager() {
     //   System.out.println("in createEntityManager()");
        if (entityManagerFactory==null) {
            System.out.println("entityManagerFactory is null");
        }
        return entityManagerFactory.createEntityManager();
    }

   
}

