package edu.tommcamm.laboratorio3.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Universita {
    @Id
    private String nome;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Corso> corsi;

    protected Universita () {}

    public Universita(String nome, List<Corso> corsi) {
        this.nome = nome;
        this.corsi = corsi;
    }

    public Universita(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Universita{" +
                "nome='" + nome + '\'' +
                ", corsi=" + corsi +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public List<Corso> getCorsi() {
        return corsi;
    }

    public void aggiungiCorso(Corso corso) {
        assert (corso != null);
        corsi.add(corso);
    }

    public void modificaCorso(Corso corso) {
        assert (corso != null);
        corsi.set(corsi.indexOf(corso), corso);
    }

    public void eliminaCorso(Corso corso) {
        assert (corso != null);
        corsi.remove(corso);
    }
}
