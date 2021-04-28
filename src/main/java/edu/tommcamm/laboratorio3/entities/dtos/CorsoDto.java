package edu.tommcamm.laboratorio3.entities.dtos;

public class CorsoDto {
    private String nome;

    // Deserialization requires a zero arg const.
    public CorsoDto() {}

    public CorsoDto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
