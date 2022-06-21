package br.edu.femass.model;

import lombok.Data;

@Data
public class Category {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
