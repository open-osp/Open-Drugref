/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.ca.dpd.history;

import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.drugref.ca.dpd.History;
import org.drugref.util.JpaUtils;

/**
 *
 * @author jackson
 */
public class HistoryUtil {
    public  static final String ACTION_UPDATE="update db";
    public boolean addUpdateHistory(){
        EntityManager entityManager = JpaUtils.createEntityManager();
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
        
            History h=new History();
            h.setDateTime(Calendar.getInstance().getTime());
            h.setAction(ACTION_UPDATE);            
            entityManager.persist(h);
            entityManager.flush();
            entityManager.clear();
            tx.commit();

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        finally{
            JpaUtils.close(entityManager);
        }
        return true;
    }
}
