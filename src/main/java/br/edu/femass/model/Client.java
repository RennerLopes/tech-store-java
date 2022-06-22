package br.edu.femass.model;

import lombok.Data;

@Data
public class Client {
    private Long id;
    private String name;
    private String cpf;
    private String phone;
    private String address;

    @Override
    public String toString() {
        return this.id + " - " + this.name;
    }
}

