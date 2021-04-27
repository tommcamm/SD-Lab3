package edu.tommcamm.laboratorio3.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Esame {

    // Per questo esercizio

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
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
        return id.equals(esame.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
