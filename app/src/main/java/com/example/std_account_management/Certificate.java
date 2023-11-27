package com.example.std_account_management;

public class Certificate {
    String std_email_certificate, certificate_name;

    public Certificate() {}
    public Certificate(String std_email_certificate, String certificate_name) {
        this.std_email_certificate = std_email_certificate;
        this.certificate_name = certificate_name;
    }

    public String getStd_email_certificate() {
        return std_email_certificate;
    }

    public void setStd_email_certificate(String std_email_certificate) {
        this.std_email_certificate = std_email_certificate;
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name;
    }
}
