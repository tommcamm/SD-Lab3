package edu.tommcamm.laboratorio3.entities.dtos;

public class EsameDto {
    private String nome;
    private int cfu;

    public EsameDto() {}

    public EsameDto(String nome, int cfu) {
        this.nome = nome;
        this.cfu = cfu;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCfu() {
        return cfu;
    }

    public void setCfu(int cfu) {
        this.cfu = cfu;
    }
}
