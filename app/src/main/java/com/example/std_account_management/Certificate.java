package com.example.std_account_management;

public class Certificate {
    String email, certName, provider, validTo;

    public Certificate() {}
    public Certificate(String std_email, String certificate_name, String provider, String validTo) {
        this.email = std_email;
        this.certName = certificate_name;
        this.provider = provider;
        this.validTo = validTo;
    }

    public String getProvider() {return provider;}
    public void setProvider(String provider) {this.provider = provider;}
    public String getValidTo() {return validTo;}
    public void setValidTo(String validTo) {this.validTo = validTo;}
    public String getStd_email_certificate() {
        return email;
    }
    public void setStd_email_certificate(String std_email_certificate) {this.email = std_email_certificate;}

    public String getCertificate_name() {
        return certName;
    }

    public void setCertificate_name(String certificate_name) {this.certName = certificate_name;}

    @Override
    public String toString() {
        return email + ',' + certName +','+ provider +',' + validTo;

    }
}
