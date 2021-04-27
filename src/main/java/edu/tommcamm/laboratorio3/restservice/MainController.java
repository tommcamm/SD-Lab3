package edu.tommcamm.laboratorio3.restservice;

import edu.tommcamm.laboratorio3.entities.Corso;
import edu.tommcamm.laboratorio3.entities.Esame;
import edu.tommcamm.laboratorio3.repositories.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MainController {

    CorsoRepository corsiRepo;

    // Code Injection per ottenere la repository dei corsi
    @Autowired
    public MainController(CorsoRepository repo) {
        this.corsiRepo = repo;
    }

    @GetMapping("/api/test")
    public String test() {
        Esame e1 = new Esame("Sistemi Distribuiti", 8);
        Esame e2 = new Esame("Architettura", 8);
        ArrayList<Esame> l1 = new ArrayList<>();
        l1.add(e1);
        l1.add(e2);
        Corso c1 = new Corso("antoniotti", l1);

        corsiRepo.save(c1);

        return "Operation Completed";
    }


}
