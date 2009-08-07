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
@Table(name = "cd_schedule", catalog = "drugref2", schema = "public")
@NamedQueries({@NamedQuery(name = "CdSchedule.findAll", query = "SELECT c FROM CdSchedule c"), @NamedQuery(name = "CdSchedule.findByDrugCode", query = "SELECT c FROM CdSchedule c WHERE c.drugCode = :drugCode"), @NamedQuery(name = "CdSchedule.findBySchedule", query = "SELECT c FROM CdSchedule c WHERE c.schedule = :schedule"), @NamedQuery(name = "CdSchedule.findById", query = "SELECT c FROM CdSchedule c WHERE c.id = :id")})
public class CdSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "drug_code")
    private Integer drugCode;
    @Column(name = "schedule", length = 40)
    private String schedule;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    public CdSchedule() {
    }

    public CdSchedule(Integer id) {
        this.id = id;
    }

    public Integer getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(Integer drugCode) {
        this.drugCode = drugCode;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
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
        if (!(object instanceof CdSchedule)) {
            return false;
        }
        CdSchedule other = (CdSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.drugref.ca.dpd.CdSchedule[id=" + id + "]";
    }

}
