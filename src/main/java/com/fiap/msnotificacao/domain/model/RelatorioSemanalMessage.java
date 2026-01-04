package com.fiap.msnotificacao.domain.model;

import java.time.Instant;
import java.util.List;

public class RelatorioSemanalMessage {

    private List<Contagem> ratingCountByDate;
    private List<Contagem> ratingCountByUrgency;
    private String dateTimeEmission;

    public RelatorioSemanalMessage() {
    }

    public List<Contagem> getRatingCountByDate() {
        return ratingCountByDate;
    }

    public List<Contagem> getRatingCountByUrgency() {
        return ratingCountByUrgency;
    }

    public String getDateTimeEmission() {
        return dateTimeEmission;
    }

    public void setRatingCountByDate(List<Contagem> ratingCountByDate) {
        this.ratingCountByDate = ratingCountByDate;
    }

    public void setRatingCountByUrgency(List<Contagem> ratingCountByUrgency) {
        this.ratingCountByUrgency = ratingCountByUrgency;
    }

    public void setDateTimeEmission(String dateTimeEmission) {
        this.dateTimeEmission = dateTimeEmission;
    }

    public boolean isValido() {
        return ratingCountByDate != null
                && !ratingCountByDate.isEmpty()
                && ratingCountByUrgency != null
                && !ratingCountByUrgency.isEmpty()
                && dateTimeEmission != null;
    }

    @Override
    public String toString() {
        return "RelatorioSemanalMessage{" +
                "ratingCountByDate=" + ratingCountByDate +
                ", ratingCountByUrgency=" + ratingCountByUrgency +
                ", dateTimeEmission=" + dateTimeEmission +
                '}';
    }
}


