package edu.tommcamm.laboratorio3.restservice;

import edu.tommcamm.laboratorio3.entities.Corso;
import edu.tommcamm.laboratorio3.entities.Esame;
import edu.tommcamm.laboratorio3.entities.Universita;
import edu.tommcamm.laboratorio3.entities.dtos.CorsoDto;
import edu.tommcamm.laboratorio3.entities.dtos.UniversitaDto;
import edu.tommcamm.laboratorio3.repositories.UniversitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    UniversitaRepository uniRepo;

    // Code Injection per utilizzare la repository dei corsi
    @Autowired
    public MainController(UniversitaRepository uniRepo) {
        this.uniRepo = uniRepo;
    }

    // Ritorna JSON con corsi di una data universita
    @GetMapping("/api/{IDuniversita}")
    public Universita getUniversita(@PathVariable String IDuniversita) {
        return uniRepo.findById(IDuniversita).orElse(null);
    }

    // Ritorna JSON con gli esami di un dato corso di una universita
    @GetMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}")
    public Corso getCorso(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea) {
        // TODO: return an error (404) if not found
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);

        if (uni != null) {
            return uni.getCorsi()
                    .stream()
                    .filter(c -> IDcorsoDiLaurea.equals(c.getCorsoDiLaurea()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @GetMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}")
    public Esame getEsame(@PathVariable String IDuniversita,
                          @PathVariable String IDcorsoDiLaurea,
                          @PathVariable String esame) {

        Universita uni = uniRepo.findById(IDuniversita).orElse(null);

        if (uni != null) {
            Corso cs = uni.getCorsi()
                    .stream()
                    .filter(c -> IDcorsoDiLaurea.equals(c.getCorsoDiLaurea()))
                    .findFirst()
                    .orElse(null);

            if (cs != null) {
                return cs.getEsami()
                        .stream()
                        .filter(c -> esame.equals(c.getNome()))
                        .findFirst()
                        .orElse(null);
            }
        }
        // TODO: return an error (404)
        return null;
    }


    // Creazione di una nuova universita
    @PostMapping("/api")
    public ResponseEntity<String> addUniversita (@RequestBody UniversitaDto extUniv) {
        uniRepo.save(new Universita(extUniv.getNome()));
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }


    // Creazione di un nuovo corso all'interno di una universita esistente
    @PostMapping(value = "/api/{IDuniversita}/")
    public ResponseEntity<String> addCorso (@PathVariable String IDuniversita, @RequestBody CorsoDto extCorso) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);

        if(uni != null) {
            uni.aggiungiCorso(new Corso(extCorso.getNome()));
            uniRepo.save(uni);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
    }


    // Creazione di un nuovo esame all'interno di un corso localizzato in una universita esistente
    @PostMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}")
    public ResponseEntity<String> addEsame (@PathVariable String IDuniversita,
                                            @PathVariable String IDcorsoDiLaurea,
                                            @RequestBody Esame extEsame) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);

        if (uni != null) {
            Corso cs = uni.getCorsi()
                    .stream()
                    .filter(c -> IDcorsoDiLaurea.equals(c.getCorsoDiLaurea()))
                    .findFirst()
                    .orElse(null);

            if (cs != null) {
                cs.aggiungiEsame(new Esame(extEsame.getNome(), extEsame.getCfu()));
                uni.modificaCorso(cs);
                uniRepo.save(uni);
                return new ResponseEntity<>("OK", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
    }

    // Modifica CFU di un corso esistente (in una universita esistente)
    // TODO: Fix spaghetti code and refactor code
    @PutMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}")
    public  ResponseEntity<String> updateCfu(@PathVariable String IDuniversita,
                                             @PathVariable String IDcorsoDiLaurea,
                                             @PathVariable String esame,
                                             @RequestParam(name = "CFU") String cfu) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);

        if (uni != null) {
            Corso cs = uni.getCorsi()
                    .stream()
                    .filter(c -> IDcorsoDiLaurea.equals(c.getCorsoDiLaurea()))
                    .findFirst()
                    .orElse(null);

            if (cs != null) {
                Esame es = cs.getEsami()
                        .stream()
                        .filter(e -> e.equals(new Esame(esame, 0)))
                        .findFirst()
                        .orElse(null);
                if (es != null) {
                    // TODO: Catch integer exception
                    es.setCfu(Integer.parseInt(cfu));
                    cs.modificaEsame(es);
                    uni.modificaCorso(cs);
                    uniRepo.save(uni);
                    return new ResponseEntity<>("OK", HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>("Failed", HttpStatus.OK);
    }

    // Cancellazione di una universita da id fornito
    @DeleteMapping("/api/{IDuniversita}")
    public ResponseEntity<String> deleteUniversita (@PathVariable String IDuniversita) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        if (uni != null) {
            uniRepo.delete(uni);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed", HttpStatus.OK);
    }

    // Cancellazione di un corso in una università
    @DeleteMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}")
    public ResponseEntity<String> deleteCorso (@PathVariable String IDuniversita,
                                               @PathVariable String IDcorsoDiLaurea) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        if (uni != null) {
            uni.eliminaCorso(new Corso(IDcorsoDiLaurea));
            uniRepo.save(uni);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
    }

    // Cancellazione di un esame da un corso in una università
    // TODO: Refactor and fix spaghetti code
    @DeleteMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}/{IDesame}")
    public ResponseEntity<String> deleteEsame (@PathVariable String IDuniversita,
                                               @PathVariable String IDcorsoDiLaurea,
                                               @PathVariable String IDesame) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        if (uni != null) {
            Corso cs = uni.getCorsi()
                    .stream()
                    .filter(c -> IDcorsoDiLaurea.equals(c.getCorsoDiLaurea()))
                    .findFirst()
                    .orElse(null);

            if (cs != null) {
                cs.eliminaEsame(new Esame(IDesame, 0));
                uni.modificaCorso(cs);
                uniRepo.save(uni);
                return new ResponseEntity<>("OK", HttpStatus.CREATED);
            }

        }
        return new ResponseEntity<>("Failed", HttpStatus.OK);
    }

}
