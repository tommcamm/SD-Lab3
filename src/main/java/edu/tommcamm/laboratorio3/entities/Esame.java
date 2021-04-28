package edu.tommcamm.laboratorio3.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Esame {

    @Id
    String nome;

    @Column(nullable = false)
    int cfu;

    protected Esame() {}

    public Esame(String nome, int cfu) {
        this.nome = nome;
        this.cfu = cfu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Esame esame = (Esame) o;
        return nome.equals(esame.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "Esame{" +
                "nome='" + nome + '\'' +
                ", cfu=" + cfu +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public int getCfu() {
        return cfu;
    }

    public void setCfu(int cfu) {
        this.cfu = cfu;
    }
}
