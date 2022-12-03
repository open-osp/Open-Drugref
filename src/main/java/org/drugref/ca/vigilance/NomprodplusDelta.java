package org.drugref.ca.vigilance;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vig_nomprodPlus_delta")
public class NomprodplusDelta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "productID", nullable = false)
    private Integer productID;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "GENcode")
    private String GENcode;

    @Column(name = "productNameFrench")
    private String productNameFrench;

    @Column(name = "strengthFrench")
    private String strengthFrench;

    @Column(name = "formFrench")
    private String formFrench;

    @Column(name = "productNameEnglish")
    private String productNameEnglish;

    @Column(name = "strengthEnglish")
    private String strengthEnglish;

    @Column(name = "formEnglish")
    private String formEnglish;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "legalStatusCanada")
    private String legalStatusCanada;

    @Column(name = "discontinued")
    private String discontinued;

    @Column(name = "pseudoDIN")
    private String pseudoDIN;

    @Column(name = "administrationRoute")
    private String administrationRoute;

    @Column(name = "defaultRouteDoseValidation")
    private String defaultRouteDoseValidation;

    @Column(name = "productNameCapitalizedFrench")
    private String productNameCapitalizedFrench;

    @Column(name = "lowercaseStrengthFrench")
    private String lowercaseStrengthFrench;

    @Column(name = "lowercaseFormFrench")
    private String lowercaseFormFrench;

    @Column(name = "productNameCapitalizedEnglish")
    private String productNameCapitalizedEnglish;

    @Column(name = "lowercaseStrengthEnglish")
    private String lowercaseStrengthEnglish;

    @Column(name = "lowercaseFormEnglish")
    private String lowercaseFormEnglish;

    @Column(name = "tallManProductNameFrench")
    private String tallManProductNameFrench;

    @Column(name = "tallManProductNameEnglish")
    private String tallManProductNameEnglish;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getGENcode() {
        return GENcode;
    }

    public void setGENcode(String GENcode) {
        this.GENcode = GENcode;
    }

    public String getProductNameFrench() {
        return productNameFrench;
    }

    public void setProductNameFrench(String productNameFrench) {
        this.productNameFrench = productNameFrench;
    }

    public String getStrengthFrench() {
        return strengthFrench;
    }

    public void setStrengthFrench(String strengthFrench) {
        this.strengthFrench = strengthFrench;
    }

    public String getFormFrench() {
        return formFrench;
    }

    public void setFormFrench(String formFrench) {
        this.formFrench = formFrench;
    }

    public String getProductNameEnglish() {
        return productNameEnglish;
    }

    public void setProductNameEnglish(String productNameEnglish) {
        this.productNameEnglish = productNameEnglish;
    }

    public String getStrengthEnglish() {
        return strengthEnglish;
    }

    public void setStrengthEnglish(String strengthEnglish) {
        this.strengthEnglish = strengthEnglish;
    }

    public String getFormEnglish() {
        return formEnglish;
    }

    public void setFormEnglish(String formEnglish) {
        this.formEnglish = formEnglish;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getLegalStatusCanada() {
        return legalStatusCanada;
    }

    public void setLegalStatusCanada(String legalStatusCanada) {
        this.legalStatusCanada = legalStatusCanada;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(String discontinued) {
        this.discontinued = discontinued;
    }

    public String getPseudoDIN() {
        return pseudoDIN;
    }

    public void setPseudoDIN(String pseudoDIN) {
        this.pseudoDIN = pseudoDIN;
    }

    public String getAdministrationRoute() {
        return administrationRoute;
    }

    public void setAdministrationRoute(String administrationRoute) {
        this.administrationRoute = administrationRoute;
    }

    public String getDefaultRouteDoseValidation() {
        return defaultRouteDoseValidation;
    }

    public void setDefaultRouteDoseValidation(String defaultRouteDoseValidation) {
        this.defaultRouteDoseValidation = defaultRouteDoseValidation;
    }

    public String getProductNameCapitalizedFrench() {
        return productNameCapitalizedFrench;
    }

    public void setProductNameCapitalizedFrench(String productNameCapitalizedFrench) {
        this.productNameCapitalizedFrench = productNameCapitalizedFrench;
    }

    public String getLowercaseStrengthFrench() {
        return lowercaseStrengthFrench;
    }

    public void setLowercaseStrengthFrench(String lowercaseStrengthFrench) {
        this.lowercaseStrengthFrench = lowercaseStrengthFrench;
    }

    public String getLowercaseFormFrench() {
        return lowercaseFormFrench;
    }

    public void setLowercaseFormFrench(String lowercaseFormFrench) {
        this.lowercaseFormFrench = lowercaseFormFrench;
    }

    public String getProductNameCapitalizedEnglish() {
        return productNameCapitalizedEnglish;
    }

    public void setProductNameCapitalizedEnglish(String productNameCapitalizedEnglish) {
        this.productNameCapitalizedEnglish = productNameCapitalizedEnglish;
    }

    public String getLowercaseStrengthEnglish() {
        return lowercaseStrengthEnglish;
    }

    public void setLowercaseStrengthEnglish(String lowercaseStrengthEnglish) {
        this.lowercaseStrengthEnglish = lowercaseStrengthEnglish;
    }

    public String getLowercaseFormEnglish() {
        return lowercaseFormEnglish;
    }

    public void setLowercaseFormEnglish(String lowercaseFormEnglish) {
        this.lowercaseFormEnglish = lowercaseFormEnglish;
    }

    public String getTallManProductNameFrench() {
        return tallManProductNameFrench;
    }

    public void setTallManProductNameFrench(String tallManProductNameFrench) {
        this.tallManProductNameFrench = tallManProductNameFrench;
    }

    public String getTallManProductNameEnglish() {
        return tallManProductNameEnglish;
    }

    public void setTallManProductNameEnglish(String tallManProductNameEnglish) {
        this.tallManProductNameEnglish = tallManProductNameEnglish;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang.builder.ToStringBuilder(this)
                .append("productID", productID)
                .append("province", province)
                .append("GENcode", GENcode)
                .append("productNameFrench", productNameFrench)
                .append("strengthFrench", strengthFrench)
                .append("formFrench", formFrench)
                .append("productNameEnglish", productNameEnglish)
                .append("strengthEnglish", strengthEnglish)
                .append("formEnglish", formEnglish)
                .append("manufacturer", manufacturer)
                .append("legalStatusCanada", legalStatusCanada)
                .append("discontinued", discontinued)
                .append("pseudoDIN", pseudoDIN)
                .append("administrationRoute", administrationRoute)
                .append("defaultRouteDoseValidation", defaultRouteDoseValidation)
                .append("productNameCapitalizedFrench", productNameCapitalizedFrench)
                .append("lowercaseStrengthFrench", lowercaseStrengthFrench)
                .append("lowercaseFormFrench", lowercaseFormFrench)
                .append("productNameCapitalizedEnglish", productNameCapitalizedEnglish)
                .append("lowercaseStrengthEnglish", lowercaseStrengthEnglish)
                .append("lowercaseFormEnglish", lowercaseFormEnglish)
                .append("tallManProductNameFrench", tallManProductNameFrench)
                .append("tallManProductNameEnglish", tallManProductNameEnglish)
                .toString();
    }
}
