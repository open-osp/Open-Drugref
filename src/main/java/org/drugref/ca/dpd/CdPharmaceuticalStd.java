/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.ca.dpd;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jackson
 */
@Entity
@Table(name = "cd_pharmaceutical_std", catalog = "drugref2", schema = "public")
@NamedQueries({@NamedQuery(name = "CdPharmaceuticalStd.findAll", query = "SELECT c FROM CdPharmaceuticalStd c"), @NamedQuery(name = "CdPharmaceuticalStd.findByDrugCode", query = "SELECT c FROM CdPharmaceuticalStd c WHERE c.drugCode = :drugCode"), @NamedQuery(name = "CdPharmaceuticalStd.findByPharmaceuticalStd", query = "SELECT c FROM CdPharmaceuticalStd c WHERE c.pharmaceuticalStd = :pharmaceuticalStd"), @NamedQuery(name = "CdPharmaceuticalStd.findById", query = "SELECT c FROM CdPharmaceuticalStd c WHERE c.id = :id")})
public class CdPharmaceuticalStd implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "drug_code")
    private Integer drugCode;
    @Column(name = "pharmaceutical_std", length = 40)
    private String pharmaceuticalStd;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    public CdPharmaceuticalStd() {
    }

    public CdPharmaceuticalStd(Integer id) {
        this.id = id;
    }

    public Integer getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(Integer drugCode) {
        this.drugCode = drugCode;
    }

    public String getPharmaceuticalStd() {
        return pharmaceuticalStd;
    }

    public void setPharmaceuticalStd(String pharmaceuticalStd) {
        this.pharmaceuticalStd = pharmaceuticalStd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CdPharmaceuticalStd)) {
            return false;
        }
        CdPharmaceuticalStd other = (CdPharmaceuticalStd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.drugref.ca.dpd.CdPharmaceuticalStd[id=" + id + "]";
    }

}
