package com.example.unamedappproject;

public class ModerateImageModel {
        private String documentId;
        private String Status;
        private String Url;


        public ModerateImageModel() {
            //no-args constructor
        }

        public String getDocumentId() {
            return documentId;
        }
        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public ModerateImageModel(String Status,String Url){
            this.Status = Status ;
            this.Url= Url;
        }

        public  String getStatus(){
            return Status;
        }

        public String getUrl(){
            return Url;
        }



}
