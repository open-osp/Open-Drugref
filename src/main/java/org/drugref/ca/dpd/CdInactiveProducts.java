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
@Table(name = "cd_inactive_products", catalog = "drugref2")
@NamedQueries({@NamedQuery(name = "CdInactiveProducts.findAll", query = "SELECT c FROM CdInactiveProducts c"), @NamedQuery(name = "CdInactiveProducts.findByDrugCode", query = "SELECT c FROM CdInactiveProducts c WHERE c.drugCode = :drugCode"), @NamedQuery(name = "CdInactiveProducts.findByDrugIdentificationNumber", query = "SELECT c FROM CdInactiveProducts c WHERE c.drugIdentificationNumber = :drugIdentificationNumber"), @NamedQuery(name = "CdInactiveProducts.findByBrandName", query = "SELECT c FROM CdInactiveProducts c WHERE c.brandName = :brandName"), @NamedQuery(name = "CdInactiveProducts.findByHistoryDate", query = "SELECT c FROM CdInactiveProducts c WHERE c.historyDate = :historyDate"), @NamedQuery(name = "CdInactiveProducts.findById", query = "SELECT c FROM CdInactiveProducts c WHERE c.id = :id")})
public class CdInactiveProducts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "drug_code")
    private Integer drugCode;
    @Column(name = "drug_identification_number", length = 8)
    private String drugIdentificationNumber;
    @Column(name = "brand_name", length = 200)
    private String brandName;
    @Column(name = "history_date")
    @Temporal(TemporalType.DATE)
    private Date historyDate;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    public CdInactiveProducts() {
    }

    public CdInactiveProducts(Integer id) {
        this.id = id;
    }

    public Integer getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(Integer drugCode) {
        this.drugCode = drugCode;
    }

    public String getDrugIdentificationNumber() {
        return drugIdentificationNumber;
    }

    public void setDrugIdentificationNumber(String drugIdentificationNumber) {
        this.drugIdentificationNumber = drugIdentificationNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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
        if (!(object instanceof CdInactiveProducts)) {
            return false;
        }
        CdInactiveProducts other = (CdInactiveProducts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.drugref.ca.dpd.CdInactiveProducts[id=" + id + "]";
    }

}
