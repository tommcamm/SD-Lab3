package edu.tommcamm.laboratorio3.restservice;

import edu.tommcamm.laboratorio3.entities.Corso;
import edu.tommcamm.laboratorio3.entities.Esame;
import edu.tommcamm.laboratorio3.entities.dtos.CorsoDto;
import edu.tommcamm.laboratorio3.repositories.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    CorsoRepository corsiRepo;

    // Code Injection per utilizzare la repository dei corsi
    @Autowired
    public MainController(CorsoRepository repo) {
        this.corsiRepo = repo;
    }

    @GetMapping(value = "/api/{IDcorsoDiLaurea}")
    public Corso getCorso(@PathVariable String IDcorsoDiLaurea) {
        // TODO: return an error (404) if not found
        return corsiRepo.findById(IDcorsoDiLaurea).orElse(null);
    }

    @GetMapping(value = "/api/{IDcorsoDiLaurea}/{esame}")
    public Esame getCorso(@PathVariable String IDcorsoDiLaurea, @PathVariable String esame) {
        Corso cs = corsiRepo.findById(IDcorsoDiLaurea).orElse(null);
        if (cs != null) {
            return cs.getEsami()
                    .stream()
                    .filter(e -> e.equals(new Esame(esame, 0)))
                    .findFirst()
                    .orElse(null);
        }
        // TODO: return an error (404)
        return null;
    }

    @PostMapping(value = "/api")
    public ResponseEntity<String> addCorso (@RequestBody CorsoDto extCorso) {
        corsiRepo.save(new Corso(extCorso.getNome()));

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/{IDcorsoDiLaurea}")
    public ResponseEntity<String> addCorso (@PathVariable String IDcorsoDiLaurea, @RequestBody Esame extEsame) {
        Corso cs = corsiRepo.findById(IDcorsoDiLaurea).orElse(null);
        if (cs != null) {
            cs.aggiungiEsame(new Esame(extEsame.getNome(), extEsame.getCfu()));
            corsiRepo.save(cs);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed", HttpStatus.OK);
    }

    @PutMapping("/api/{IDcorsoDiLaurea}/{esame}")
    public  ResponseEntity<String> updateCfu(@PathVariable String IDcorsoDiLaurea, @PathVariable String esame,
                                             @RequestParam(name = "CFU") String cfu) {
        Corso cs = corsiRepo.findById(IDcorsoDiLaurea).orElse(null);
        if (cs != null) {
            Esame es = cs.getEsami()
                    .stream()
                    .filter(c -> c.equals(new Esame(esame, 0)))
                    .findFirst()
                    .orElse(null);

            if (es != null) {
                // TODO: Check if exception is thrown
                es.setCfu(Integer.parseInt(cfu));
                cs.modificaEsame(es, cs.getEsami().indexOf(es));

                corsiRepo.save(cs);

                return new ResponseEntity<>("OK", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Failed", HttpStatus.OK);
    }

    @DeleteMapping("/api/{IDcorsoDiLaurea}")
    public ResponseEntity<String> deleteCorso (@PathVariable String IDcorsoDiLaurea) {
        Corso cs = corsiRepo.findById(IDcorsoDiLaurea).orElse(null);
        if (cs != null) {
            corsiRepo.delete(cs);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed", HttpStatus.OK);
    }

    @DeleteMapping("/api/{IDcorsoDiLaurea}/{IDesame}")
    public ResponseEntity<String> deleteEsame (@PathVariable String IDcorsoDiLaurea, @PathVariable String IDesame) {
        Corso cs = corsiRepo.findById(IDcorsoDiLaurea).orElse(null);
        if (cs != null) {
            Esame es = cs.getEsami()
                    .stream()
                    .filter(c -> c.equals(new Esame(IDesame, 0)))
                    .findFirst()
                    .orElse(null);

            if (es != null) {
                cs.eliminaEsame(es);
                corsiRepo.save(cs);
                return new ResponseEntity<>("OK", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Failed", HttpStatus.OK);
    }


    @GetMapping("/api/test")
    public String test() {
        Esame e1 = new Esame("Sistemi Distribuiti", 8);
        Esame e2 = new Esame("Architettura", 8);
        ArrayList<Esame> l1 = new ArrayList<>();
        l1.add(e1);
        l1.add(e2);
        Corso c1 = new Corso("Informatica", l1);

        corsiRepo.save(c1);

        return "Operation Completed";
    }


}
