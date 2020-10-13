package com.example.unamedappproject;

public class AllRequests {
    private String documentId;
    private String Description;
    private String Title;
    private String Owner;


    public AllRequests() {
        //no-args constructor
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public AllRequests(String description,String title,String owner){
        this.Description = description ;
        this.Title= title;
        this.Owner = owner;

    }

    public  String getDescription(){
        return Description;
    }

    public String getTitle(){
        return Title;
    }

    public String getOwner(){
        return Owner;
    }
}
