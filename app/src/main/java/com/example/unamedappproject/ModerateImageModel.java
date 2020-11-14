package com.example.unamedappproject;

public class ModerateImageModel {
        private String documentId;
        private Boolean verified;
        private String img_url;
        private Boolean correct;



        public ModerateImageModel() {
            //no-args constructor
        }

        public String getDocumentId() {
            return documentId;
        }
        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public ModerateImageModel(Boolean Status,String Url){
            this.verified =  Status;
            this.img_url= Url;
        }




    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
