package org.drugref.ca.vigilance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "vig_generxPlus")
public class Generxplus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "GENcode")
    private String GENcode;

    @Column(name = "genericNameFrench")
    private String genericNameFrench;

    @Column(name = "genericNameEnglish")
    private String genericNameEnglish;

    @Column(name = "strengthFrench")
    private String strengthFrench;

    @Column(name = "formFrench")
    private String formFrench;

    @Column(name = "strengthEnglish")
    private String strengthEnglish;

    @Column(name = "formEnglish")
    private String formEnglish;

    @Column(name = "doseUnitsPerDoseFormUnit")
    private String doseUnitsPerDoseFormUnit;

    @Column(name = "doseUnits")
    private String doseUnits;

    @Column(name = "defaultFrequency")
    private String defaultFrequency;

    @Column(name = "defaultRouteOfAdmin")
    private String defaultRouteOfAdmin;

    @Column(name = "ceRxFormCode")
    private String ceRxFormCode;

    @Column(name = "ceRxRouteCode")
    private String ceRxRouteCode;

    @Column(name = "lowercaseGenericNameFrench")
    private String lowercaseGenericNameFrench;

    @Column(name = "lowercaseStrengthFrench")
    private String lowercaseStrengthFrench;

    @Column(name = "lowercaseFormFrench")
    private String lowercaseFormFrench;

    @Column(name = "lowercaseGenericNameEnglish")
    private String lowercaseGenericNameEnglish;

    @Column(name = "lowercaseStrengthEnglish")
    private String lowercaseStrengthEnglish;

    @Column(name = "lowercaseFormEnglish")
    private String lowercaseFormEnglish;

    @Column(name = "activityIndicator")
    private Integer activityIndicator;

    @Column(name = "dosageValidated")
    private Integer dosageValidated;

    @Column(name = "NTPCodesOfCCDD")
    private String NTPCodesOfCCDD;

    @Column(name = "SIGUnitsSingularFrench")
    private String SIGUnitsSingularFrench;

    @Column(name = "SIGUnitsPluralFrench")
    private String SIGUnitsPluralFrench;

    @Column(name = "SIGUnitsSingularEnglish")
    private String SIGUnitsSingularEnglish;

    @Column(name = "SIGUnitsPluralEnglish")
    private String SIGUnitsPluralEnglish;

    @Column(name = "usualNameFrench")
    private String usualNameFrench;

    @Column(name = "usualNameEnglish")
    private String usualNameEnglish;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGENcode() {
        return GENcode;
    }

    public void setGENcode(String GENcode) {
        this.GENcode = GENcode;
    }

    public String getGenericNameFrench() {
        return genericNameFrench;
    }

    public void setGenericNameFrench(String genericNameFrench) {
        this.genericNameFrench = genericNameFrench;
    }

    public String getGenericNameEnglish() {
        return genericNameEnglish;
    }

    public void setGenericNameEnglish(String genericNameEnglish) {
        this.genericNameEnglish = genericNameEnglish;
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

    public String getDoseUnitsPerDoseFormUnit() {
        return doseUnitsPerDoseFormUnit;
    }

    public void setDoseUnitsPerDoseFormUnit(String doseUnitsPerDoseFormUnit) {
        this.doseUnitsPerDoseFormUnit = doseUnitsPerDoseFormUnit;
    }

    public String getDoseUnits() {
        return doseUnits;
    }

    public void setDoseUnits(String doseUnits) {
        this.doseUnits = doseUnits;
    }

    public String getDefaultFrequency() {
        return defaultFrequency;
    }

    public void setDefaultFrequency(String defaultFrequency) {
        this.defaultFrequency = defaultFrequency;
    }

    public String getDefaultRouteOfAdmin() {
        return defaultRouteOfAdmin;
    }

    public void setDefaultRouteOfAdmin(String defaultRouteOfAdmin) {
        this.defaultRouteOfAdmin = defaultRouteOfAdmin;
    }

    public String getCeRxFormCode() {
        return ceRxFormCode;
    }

    public void setCeRxFormCode(String ceRxFormCode) {
        this.ceRxFormCode = ceRxFormCode;
    }

    public String getCeRxRouteCode() {
        return ceRxRouteCode;
    }

    public void setCeRxRouteCode(String ceRxRouteCode) {
        this.ceRxRouteCode = ceRxRouteCode;
    }

    public String getLowercaseGenericNameFrench() {
        return lowercaseGenericNameFrench;
    }

    public void setLowercaseGenericNameFrench(String lowercaseGenericNameFrench) {
        this.lowercaseGenericNameFrench = lowercaseGenericNameFrench;
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

    public String getLowercaseGenericNameEnglish() {
        return lowercaseGenericNameEnglish;
    }

    public void setLowercaseGenericNameEnglish(String lowercaseGenericNameEnglish) {
        this.lowercaseGenericNameEnglish = lowercaseGenericNameEnglish;
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

    public Integer getActivityIndicator() {
        return activityIndicator;
    }

    public void setActivityIndicator(Integer activityIndicator) {
        this.activityIndicator = activityIndicator;
    }

    public Integer getDosageValidated() {
        return dosageValidated;
    }

    public void setDosageValidated(Integer dosageValidated) {
        this.dosageValidated = dosageValidated;
    }

    public String getNTPCodesOfCCDD() {
        return NTPCodesOfCCDD;
    }

    public void setNTPCodesOfCCDD(String NTPCodesOfCCDD) {
        this.NTPCodesOfCCDD = NTPCodesOfCCDD;
    }

    public String getSIGUnitsSingularFrench() {
        return SIGUnitsSingularFrench;
    }

    public void setSIGUnitsSingularFrench(String SIGUnitsSingularFrench) {
        this.SIGUnitsSingularFrench = SIGUnitsSingularFrench;
    }

    public String getSIGUnitsPluralFrench() {
        return SIGUnitsPluralFrench;
    }

    public void setSIGUnitsPluralFrench(String SIGUnitsPluralFrench) {
        this.SIGUnitsPluralFrench = SIGUnitsPluralFrench;
    }

    public String getSIGUnitsSingularEnglish() {
        return SIGUnitsSingularEnglish;
    }

    public void setSIGUnitsSingularEnglish(String SIGUnitsSingularEnglish) {
        this.SIGUnitsSingularEnglish = SIGUnitsSingularEnglish;
    }

    public String getSIGUnitsPluralEnglish() {
        return SIGUnitsPluralEnglish;
    }

    public void setSIGUnitsPluralEnglish(String SIGUnitsPluralEnglish) {
        this.SIGUnitsPluralEnglish = SIGUnitsPluralEnglish;
    }

    public String getUsualNameFrench() {
        return usualNameFrench;
    }

    public void setUsualNameFrench(String usualNameFrench) {
        this.usualNameFrench = usualNameFrench;
    }

    public String getUsualNameEnglish() {
        return usualNameEnglish;
    }

    public void setUsualNameEnglish(String usualNameEnglish) {
        this.usualNameEnglish = usualNameEnglish;
    }
}
