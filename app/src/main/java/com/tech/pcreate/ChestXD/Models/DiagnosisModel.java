package com.tech.pcreate.ChestXD.Models;

public class DiagnosisModel {
    String diagnosisid;
    String imageuri;
    String diagnosis;
    String patientname;
    String patientage;
    String patientphone;
    String patientemail;
    String timestamp;

    public DiagnosisModel(String diagnosisid, String imageuri, String diagnosis, String patientname,
                          String patientage, String patientphone, String patientemail, String timestamp) {
        this.diagnosisid = diagnosisid;
        this.imageuri = imageuri;
        this.diagnosis = diagnosis;
        this.patientname = patientname;
        this.patientage = patientage;
        this.patientphone = patientphone;
        this.patientemail = patientemail;
        this.timestamp = timestamp;
    }

    public String getDiagnosisid() {
        return diagnosisid;
    }

    public String getImageuri() {
        return imageuri;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPatientname() {
        return patientname;
    }

    public String getPatientage() {
        return patientage;
    }

    public String getPatientphone() {
        return patientphone;
    }

    public String getPatientemail() {
        return patientemail;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
