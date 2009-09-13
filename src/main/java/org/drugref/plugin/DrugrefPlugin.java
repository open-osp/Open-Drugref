/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.plugin;

/**
 *
 * @author jackson
 */

public class DrugrefPlugin extends PluginImpl {
        public DrugrefPlugin() {

            this.setName("Holbrook Drug Interactions");
            this.setVersion("1.0");
            Holbrook hb=new Holbrook();
            this.setPlugin(hb);
            this.setProvides(hb.listCapabilities());

        }
    }