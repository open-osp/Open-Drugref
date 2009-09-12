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
public class PluginImpl implements Plugin{
    private String name;
    private String version;
    private Hashtable provides;
    private Object plugin;

    public void drugrefPlugin(){
        this.name=null;
        this.version=null;
        this.provides=new Hashtable();
        this.plugin=new Object();
    }
    public void setName (String name){
        this.name=name;
    }
    public void setVersion(String version){
        this.version=version;
    }
    public void setPlugin(Object plugin){
        this.plugin=plugin;
    }
    public void setProvides(Hashtable provides){
        this.provides=provides;
    }
    public String getName(){
        return this.name;
    }
    public String getVersion(){
        return this.version;
    }
    public Object getPlugin(){
        return this.plugin;
    }
    public Hashtable getProvides(){
        return this.provides;
    }

    public Vector register(){
        Vector vec=new Vector();
        vec.addElement(this.name);
        vec.addElement(this.version);
        vec.addElement(this.provides);
        vec.addElement(this.plugin);
        return vec;
    };

}
