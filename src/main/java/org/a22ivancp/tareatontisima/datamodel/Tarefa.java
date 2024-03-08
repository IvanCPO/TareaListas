package org.a22ivancp.tareatontisima.datamodel;

import java.time.LocalDate;

public class Tarefa {
    private String description;
    private String detalles;
    private LocalDate limitDate;

    public Tarefa() {
    }

    public Tarefa(String description, String detalles, LocalDate limitDate) {
        this.description = description;
        this.detalles = detalles;
        this.limitDate = limitDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    @Override
    public String toString() {
        return description;
    }
}
