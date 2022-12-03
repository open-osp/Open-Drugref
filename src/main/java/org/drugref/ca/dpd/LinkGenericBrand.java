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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jackson
 */
@Entity
@Table(name = "link_generic_brand")
@NamedQueries({@NamedQuery(name = "LinkGenericBrand.findAll", query = "SELECT l FROM LinkGenericBrand l"), @NamedQuery(name = "LinkGenericBrand.findByPkId", query = "SELECT l FROM LinkGenericBrand l WHERE l.pkId = :pkId"), @NamedQuery(name = "LinkGenericBrand.findById", query = "SELECT l FROM LinkGenericBrand l WHERE l.id = :id"), @NamedQuery(name = "LinkGenericBrand.findByDrugCode", query = "SELECT l FROM LinkGenericBrand l WHERE l.drugCode = :drugCode")})
public class LinkGenericBrand implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "pk_id", nullable = false)
    private Integer pkId;
    @Column(name = "id")
    private Integer id;
    @Column(name = "drug_code", length = 30)
    private String drugCode;

    public LinkGenericBrand() {
    }

    public LinkGenericBrand(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkId != null ? pkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LinkGenericBrand)) {
            return false;
        }
        LinkGenericBrand other = (LinkGenericBrand) object;
        if ((this.pkId == null && other.pkId != null) || (this.pkId != null && !this.pkId.equals(other.pkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.drugref.ca.dpd.LinkGenericBrand[pkId=" + pkId + "]";
    }

}
