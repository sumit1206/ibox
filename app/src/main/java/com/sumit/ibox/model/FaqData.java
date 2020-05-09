package com.sumit.ibox.model;
public class FaqData {
    String faqQuestion;
    String faqAnswer;
    String faqConsumer;
    String faqLikeCount;
    String faqUnlikeCount;

    public FaqData(String faqQuestion, String faqAnswer, String faqConsumer, String faqLikeCount, String faqUnlikeCount) {
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
        this.faqConsumer = faqConsumer;
        this.faqLikeCount = faqLikeCount;
        this.faqUnlikeCount = faqUnlikeCount;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public void setFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }

    public void setFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
    }

    public String getFaqConsumer() {
        return faqConsumer;
    }

    public void setFaqConsumer(String faqConsumer) {
        this.faqConsumer = faqConsumer;
    }

    public String getFaqLikeCount() {
        return faqLikeCount;
    }

    public void setFaqLikeCount(String faqLikeCount) {
        this.faqLikeCount = faqLikeCount;
    }

    public String getFaqUnlikeCount() {
        return faqUnlikeCount;
    }

    public void setFaqUnlikeCount(String faqUnlikeCount) {
        this.faqUnlikeCount = faqUnlikeCount;
    }
}
