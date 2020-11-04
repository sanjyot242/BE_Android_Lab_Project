package com.example.unamedappproject;

public class MyRequest {
    private String documentId;
    private String Description;
    private String Title;



    public MyRequest() {
        //no-args constructor
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public MyRequest(String description,String title,String owner){
        this.Description = description ;
        this.Title= title;
    }

    public  String getDescription(){
        return Description;
    }

    public String getTitle(){
        return Title;
    }


}
