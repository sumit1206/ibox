package com.sumit.ibox.model;

public class MyBookLibraryData {
    public String authorLibrary;
    public  String bookNName;
    public String submissionDateLib;
    public String issueDateLib;


    public MyBookLibraryData(String authorLibrary, String bookNName, String submissionDateLib, String issueDateLib) {
        this.authorLibrary = authorLibrary;
        this.bookNName = bookNName;
        this.submissionDateLib = submissionDateLib;
        this.issueDateLib = issueDateLib;
    }

    public String getAuthorLibrary() {
        return authorLibrary;
    }

    public String getBookNName() {
        return bookNName;
    }

    public String getSubmissionDateLib() {
        return submissionDateLib;
    }

    public String getIssueDateLib() {
        return issueDateLib;
    }
}
