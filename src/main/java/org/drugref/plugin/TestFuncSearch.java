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
public class TestFuncSearch{
    //if __name__ == "__main__":
    public Vector testFunc(String key) {
        Vector vec = new Vector();
        Hashtable ha = new Hashtable();
        if (key.equals("thekey")) {
            ha.put("attribute", "value");
            ha.put("attribute2", "value2");
            vec.addElement(ha);
        }
        return vec;
    }

    /*     def testfunc(key):
    if key == 'thekey':
    return [{'attribute':'value', 'attribute2':'value2'}]*/
    public Vector testSearch(String key) {
        Vector vec = new Vector();
        Hashtable ha1 = new Hashtable();
        Hashtable ha2 = new Hashtable();
        ha1.put("found", "one");
        ha1.put("key", 1);
        ha2.put("found", "two");
        ha2.put("key", 2);
        vec.addElement(ha1);
        vec.addElement(ha2);
        return vec;

    }
}