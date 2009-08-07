/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.ca.dpd;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jackson
 */
@Entity
@Table(name = "cd_drug_status", catalog = "drugref2", schema = "public")
@NamedQueries({@NamedQuery(name = "CdDrugStatus.findAll", query = "SELECT c FROM CdDrugStatus c"), @NamedQuery(name = "CdDrugStatus.findByDrugCode", query = "SELECT c FROM CdDrugStatus c WHERE c.drugCode = :drugCode"), @NamedQuery(name = "CdDrugStatus.findByCurrentStatusFlag", query = "SELECT c FROM CdDrugStatus c WHERE c.currentStatusFlag = :currentStatusFlag"), @NamedQuery(name = "CdDrugStatus.findByStatus", query = "SELECT c FROM CdDrugStatus c WHERE c.status = :status"), @NamedQuery(name = "CdDrugStatus.findByHistoryDate", query = "SELECT c FROM CdDrugStatus c WHERE c.historyDate = :historyDate"), @NamedQuery(name = "CdDrugStatus.findById", query = "SELECT c FROM CdDrugStatus c WHERE c.id = :id")})
public class CdDrugStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "drug_code")
    private Integer drugCode;
    @Column(name = "current_status_flag")
    private Character currentStatusFlag;
    @Column(name = "status", length = 40)
    private String status;
    @Column(name = "history_date")
    @Temporal(TemporalType.DATE)
    private Date historyDate;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    public CdDrugStatus() {
    }

    public CdDrugStatus(Integer id) {
        this.id = id;
    }

    public Integer getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(Integer drugCode) {
        this.drugCode = drugCode;
    }

    public Character getCurrentStatusFlag() {
        return currentStatusFlag;
    }

    public void setCurrentStatusFlag(Character currentStatusFlag) {
        this.currentStatusFlag = currentStatusFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(Date historyDate) {
        this.historyDate = historyDate;
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
        if (!(object instanceof CdDrugStatus)) {
            return false;
        }
        CdDrugStatus other = (CdDrugStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.drugref.ca.dpd.CdDrugStatus[id=" + id + "]";
    }

}
