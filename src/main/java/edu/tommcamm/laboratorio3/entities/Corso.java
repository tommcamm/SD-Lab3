package edu.tommcamm.laboratorio3.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Corso {

    @Id
    private final String corsoDiLaurea;

    @OneToMany(cascade = {CascadeType.ALL})
    private final List<Esame> esami;

    public Corso(String corsoDiLaurea, List<Esame> esami) {
        this.corsoDiLaurea = corsoDiLaurea;
        this.esami = esami;
    }

    public Corso(String nomeCorso) {
        this.corsoDiLaurea = nomeCorso;
        this.esami = new ArrayList<>();
    }

    public Corso() {
        this.corsoDiLaurea = "";
        this.esami = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Corso{" +
                "corsoDiLaurea='" + corsoDiLaurea + '\'' +
                ", esami=" + esami +
                '}';
    }

    public String getCorsoDiLaurea() {
        return corsoDiLaurea;
    }

    public List<Esame> getEsami() {
        return esami;
    }

    public void aggiungiEsame(Esame e) {
        // TODO: make a proper check that Esame != null
        assert(e!=null);
        esami.add(e);
    }

    public void modificaEsame(Esame e, int indice){
        esami.set(indice, e);
    }

    public void eliminaEsame(Esame e) {
        esami.remove(e);
    }


}
