/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.drugref.ca.dpd;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author jackson
 */
@Entity
@Table(name = "cd_drug_search")
@NamedQueries({@NamedQuery(name = "CdDrugSearch.findAll", query = "SELECT c FROM CdDrugSearch c"), @NamedQuery(name = "CdDrugSearch.findById", query = "SELECT c FROM CdDrugSearch c WHERE c.id = :id"), @NamedQuery(name = "CdDrugSearch.findByDrugCode", query = "SELECT c FROM CdDrugSearch c WHERE c.drugCode = :drugCode"), @NamedQuery(name = "CdDrugSearch.findByCategory", query = "SELECT c FROM CdDrugSearch c WHERE c.category = :category"), @NamedQuery(name = "CdDrugSearch.findByName", query = "SELECT c FROM CdDrugSearch c WHERE c.name = :name")})
public class CdDrugSearch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private String uuid;

    @Column(name = "drug_code")
    private String drugCode;
    @Column(name = "category")
    private Integer category;
    @Column(name = "name")
    private String name;

    public CdDrugSearch() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof CdDrugSearch)) {
            return false;
        }
        CdDrugSearch other = (CdDrugSearch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.drugref.ca.dpd.CdDrugSearch[id=" + id + "]";
    }

}
