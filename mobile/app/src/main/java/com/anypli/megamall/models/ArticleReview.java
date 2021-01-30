package com.anypli.megamall.models;

public class ArticleReview {
    private String mArticleID ;
    private String mUsername ;
    private String mUsericon ;
    private String mUsercomment;
    private float mUserrating ;

    public ArticleReview(String articleID ,String username, String usericon, String usercomment, float userrating) {
        this.mArticleID=articleID;
        this.mUsername = username;
        this.mUsericon = usericon;
        this.mUsercomment = usercomment;
        this.mUserrating = userrating;
    }

    public String getUserIconUrl() {
        return mUsericon;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getComment() {
        return mUsercomment;
    }

    public float getRating() {
        return mUserrating;
    }

    public String getArticleID() {
        return mArticleID;
    }
}
