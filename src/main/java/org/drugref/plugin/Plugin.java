/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.plugin;

import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author jackson
 */
public interface Plugin {
    public void drugrefPlugin();
    public void setName (String name);
    public void setVersion(String version);
    public void setProvides(Hashtable provides);
    public void setPlugin(Object plugin);

    public String getName();
    public String getVersion();
    public Hashtable getProvides();
    public Object getPlugin();

    public Vector register();
    

}
