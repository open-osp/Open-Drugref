/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.util;

import java.util.HashMap;
import java.util.List;
import org.drugref.Drugref;
import org.drugref.ca.dpd.fetch.DPDImport;
import org.drugref.ca.dpd.fetch.TempNewGenericImport;
import org.drugref.ca.dpd.history.HistoryUtil;

/**
 *
 * @author jackson
 */
public class RxUpdateDBWorker extends Thread{
    public RxUpdateDBWorker(){}
    public void run(){
        synchronized(this){
            Drugref.UPDATE_DB=true;

            DPDImport dpdImport =new DPDImport();
            long timeDataImport=0L;
            timeDataImport=dpdImport.doItDifferent();
            timeDataImport=(timeDataImport/1000)/60;
            TempNewGenericImport newGenericImport=new TempNewGenericImport();
            long timeGenericImport=0L;
            timeGenericImport=newGenericImport.run();//in miliseconds
            timeGenericImport=(timeGenericImport/1000)/60;
            dpdImport.setISMPmeds();
            HistoryUtil h=new HistoryUtil();
            h.addUpdateHistory();
            HashMap hm=dpdImport.numberTableRows();
            List<Integer> addedDescriptor=dpdImport.addDescriptorToSearchName();
            List<Integer> addedStrength=dpdImport.addStrengthToBrandName();

            Drugref.DB_INFO.put("tableRowNum", hm);
            Drugref.DB_INFO.put("timeImportDataMinutes", timeDataImport);
            Drugref.DB_INFO.put("timeImportGenericMinutes", timeGenericImport);
            Drugref.DB_INFO.put("descriptor", addedDescriptor);
            Drugref.DB_INFO.put("strength", addedStrength);

            Drugref.UPDATE_DB=false;
        }
    }
}
