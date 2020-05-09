package com.sumit.ibox.model;

public class ArticlesForYouData
{
    private String blogTitle;
    private String blogUploaderName;
    private int uploaderThumb;
    private String url;
    private String blogPostDate;

    public String getBlogTitle() {
        return blogTitle;
    }

    public String getBlogUploaderName() {
        return blogUploaderName;
    }

    public int getUploaderThumb() {
        return uploaderThumb;
    }

    public String getUrl() {
        return url;
    }

    public String getBlogPostDate() {
        return blogPostDate;
    }

    public ArticlesForYouData(String blogTitle, String blogUploaderName, int uploaderThumb, String url, String blogPostDate) {
        this.blogTitle = blogTitle;
        this.blogUploaderName = blogUploaderName;
        this.uploaderThumb = uploaderThumb;
        this.url = url;
        this.blogPostDate = blogPostDate;
    }
}
