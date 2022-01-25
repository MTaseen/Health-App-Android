package com.example.healthcareapp;

public class ModelIllnessData {
    private String illnessId, illnessName, illnessDescription, illnessAgeCategory, illnessEthnicityCategory, illnessCountryCategory, illnessSummary, illnessSymptoms, illnessTreatments, timestamp, uid;

    public ModelIllnessData() {

    }



    public ModelIllnessData(String illnessId, String illnessName, String illnessDescription, String illnessAgeCategory, String illnessEthnicityCategory, String illnessCountryCategory, String illnessSummary,
                            String illnessSymptoms, String illnessTreatments, String timestamp, String uid) {
        this.illnessId = illnessId;
        this.illnessName = illnessName;
        this.illnessDescription = illnessDescription;
        this.illnessAgeCategory = illnessAgeCategory;
        this.illnessEthnicityCategory = illnessEthnicityCategory;
        this.illnessCountryCategory = illnessCountryCategory;
        this.illnessSummary = illnessSummary;
        this.illnessSymptoms = illnessSymptoms;
        this.illnessTreatments = illnessTreatments;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getIllnessId() {
        return illnessId;
    }

    public void setIllnessId(String illnessId) {
        this.illnessId = illnessId;
    }

    public String getIllnessName() {
        return illnessName;
    }

    public void setIllnessName(String illnessName) {
        this.illnessName = illnessName;
    }

    public String getIllnessDescription() {
        return illnessDescription;
    }

    public void setIllnessDescription(String illnessDescription) {
        this.illnessDescription = illnessDescription;
    }

    public String getIllnessAgeCategory() {
        return illnessAgeCategory;
    }

    public void setIllnessAgeCategory(String illnessAgeCategory) {
        this.illnessAgeCategory = illnessAgeCategory;
    }

    public String getIllnessEthnicityCategory() {
        return illnessEthnicityCategory;
    }

    public void setIllnessEthnicityCategory(String illnessEthnicityCategory) {
        this.illnessEthnicityCategory = illnessEthnicityCategory;
    }

    public String getIllnessCountryCategory() {
        return illnessCountryCategory;
    }

    public void setIllnessCountryCategory(String illnessCountryCategory) {
        this.illnessCountryCategory = illnessCountryCategory;
    }

    public String getIllnessSummary() {
        return illnessSummary;
    }

    public void setIllnessSummary(String illnessSummary) {
        this.illnessSummary = illnessSummary;
    }

    public String getIllnessSymptoms() {
        return illnessSymptoms;
    }

    public void setIllnessSymptoms(String illnessSymptoms) {
        this.illnessSymptoms = illnessSymptoms;
    }

    public String getIllnessTreatments() {
        return illnessTreatments;
    }

    public void setIllnessTreatments(String illnessTreatments) {
        this.illnessTreatments = illnessTreatments;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
