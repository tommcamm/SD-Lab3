package edu.tommcamm.laboratorio3.entities.dtos;

public class UniversitaDto {
    private String nome;

    public UniversitaDto() {}

    public UniversitaDto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
