package br.com.jeff.listaprofx.model;

public enum Priority {
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta");

    private String description;

    Priority(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
