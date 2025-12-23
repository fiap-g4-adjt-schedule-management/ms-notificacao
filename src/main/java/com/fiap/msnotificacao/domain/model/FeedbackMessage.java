package com.fiap.msnotificacao.domain.model;

import java.time.Instant;

public class FeedbackMessage {

    private String id;
    private Integer rating;
    private String description;
    private String email;
    private Boolean critical;
    private String createdAt;

    public boolean isCritical() {
        return Boolean.TRUE.equals(critical);
    }

    public String getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getCritical() {
        return critical;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCritical(Boolean critical) {
        this.critical = critical;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = String.valueOf(createdAt);
    }
}
