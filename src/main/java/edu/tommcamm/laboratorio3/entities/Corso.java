package edu.tommcamm.laboratorio3.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Corso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private final String corsoDiLaurea;

    @OneToMany
    private final List<Esame> esami;

    public Corso(String corsoDiLaurea, List<Esame> esami) {
        this.corsoDiLaurea = corsoDiLaurea;
        this.esami = esami;
    }

    public Corso() {
        this.corsoDiLaurea = "";
        this.esami = new ArrayList<>();
    }
}
