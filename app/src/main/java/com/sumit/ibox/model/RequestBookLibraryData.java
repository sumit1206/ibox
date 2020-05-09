package com.sumit.ibox.model;

public class RequestBookLibraryData {

    private String bookId;
    private String bookAuthor;
    private String bookName;

    public String getBookId() {
        return bookId;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookName() {
        return bookName;
    }

    public RequestBookLibraryData(String bookId, String bookAuthor, String bookName) {
        this.bookId = bookId;
        this.bookAuthor = bookAuthor;
        this.bookName = bookName;
    }
}
