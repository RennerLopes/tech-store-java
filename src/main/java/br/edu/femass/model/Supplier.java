package br.edu.femass.model;

import lombok.Data;

@Data
public class Supplier {
    private Long id;
    private String name;
    private String cnpj;
    private String phone;
    private String address;

    @Override
    public String toString() {
        return this.id + " - " + this.name;
    }
}

