package edu.tommcamm.laboratorio3.restservice;

import edu.tommcamm.laboratorio3.entities.Corso;
import edu.tommcamm.laboratorio3.entities.Esame;
import edu.tommcamm.laboratorio3.entities.Universita;
import edu.tommcamm.laboratorio3.entities.dtos.AjaxResponseBody;
import edu.tommcamm.laboratorio3.entities.dtos.CorsoDto;
import edu.tommcamm.laboratorio3.entities.dtos.UniversitaDto;
import edu.tommcamm.laboratorio3.exceptions.ResourceNotFoundException;
import edu.tommcamm.laboratorio3.repositories.UniversitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiController {

    UniversitaRepository uniRepo;

    // Code Injection per utilizzare la repository dei corsi
    @Autowired
    public ApiController(UniversitaRepository uniRepo) {
        this.uniRepo = uniRepo;
    }

    // Ritorna JSON con corsi di una data universita
    @GetMapping("/api/{IDuniversita}")
    public Universita getUniversita(@PathVariable String IDuniversita) {
        return uniRepo.findById(IDuniversita).orElseThrow(ResourceNotFoundException::new);
    }

    // Ritorna JSON con gli esami di un dato corso di una universita
    @GetMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}")
    public Corso getCorso(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea) {
        return getUniversita(IDuniversita)
                .getCorsi()
                .stream()
                .filter(c -> IDcorsoDiLaurea.equals(c.getCorsoDiLaurea()))
                .findFirst()
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}")
    public Esame getEsame(@PathVariable String IDuniversita,
                          @PathVariable String IDcorsoDiLaurea,
                          @PathVariable String esame) {
        return getCorso(IDuniversita, IDcorsoDiLaurea).getEsami()
                    .stream()
                    .filter(c -> esame.equals(c.getNome()))
                    .findFirst()
                    .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/api")
    public List<Universita> listaUniversita() {
        List<Universita> uni = new ArrayList<>();
        uniRepo.findAll().iterator().forEachRemaining(uni::add);

        return uni;
    }


    // Creazione di una nuova universita
    @PostMapping("/api")
    public ResponseEntity<?> addUniversita (@RequestBody UniversitaDto extUniv) {

        AjaxResponseBody response = new AjaxResponseBody();

        if(!extUniv.getNome().isEmpty() && uniRepo.findById(extUniv.getNome()).isEmpty()){
            uniRepo.save(new Universita(extUniv.getNome()));
            response.setMsg("ok");
            return ResponseEntity.ok(response);
        }
        response.setMsg("already_created");
        return ResponseEntity.badRequest().body(response);
    }


    // Creazione di un nuovo corso all'interno di una universita esistente
    @PostMapping(value = "/api/{IDuniversita}/")
    public ResponseEntity<?> addCorso (@PathVariable String IDuniversita, @RequestBody CorsoDto extCorso) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        AjaxResponseBody response = new AjaxResponseBody();

        if(uni != null && !uni.getCorsi().contains(new Corso(extCorso.getNome()))) {
            uni.aggiungiCorso(new Corso(extCorso.getNome()));
            uniRepo.save(uni);

            response.setMsg("ok");
            return ResponseEntity.ok(response);
        }

        response.setMsg("invalid_data");
        return ResponseEntity.badRequest().body(response);
    }


    // Creazione di un nuovo esame all'interno di un corso localizzato in una universita esistente
    @PostMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}")
    public ResponseEntity<?> addEsame (@PathVariable String IDuniversita,
                                            @PathVariable String IDcorsoDiLaurea,
                                            @RequestBody Esame extEsame) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        AjaxResponseBody response = new AjaxResponseBody();

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

                response.setMsg("ok");
                return ResponseEntity.ok(response);
            }
        }

        response.setMsg("not_found");
        return ResponseEntity.badRequest().body(response);
    }

    // Modifica CFU di un corso esistente (in una universita esistente)
    // TODO: Fix spaghetti code and refactor code
    @PutMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}")
    public  ResponseEntity<?> updateCfu(@PathVariable String IDuniversita,
                                             @PathVariable String IDcorsoDiLaurea,
                                             @PathVariable String esame,
                                             @RequestParam(name = "CFU") String cfu) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        AjaxResponseBody response = new AjaxResponseBody();

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

                    response.setMsg("ok");
                    return ResponseEntity.ok(response);
                }
            }
        }
        response.setMsg("not_found");
        return ResponseEntity.badRequest().body(response);
    }

    // Cancellazione di una universita da id fornito
    @DeleteMapping("/api/{IDuniversita}")
    public ResponseEntity<?> deleteUniversita (@PathVariable String IDuniversita) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        AjaxResponseBody response = new AjaxResponseBody();
        if (uni != null) {
            uniRepo.delete(uni);

            response.setMsg("ok");
            return ResponseEntity.ok(response);
        }
        response.setMsg("not_found");
        return ResponseEntity.badRequest().body(response);
    }

    // Cancellazione di un corso in una università
    @DeleteMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}")
    public ResponseEntity<?> deleteCorso (@PathVariable String IDuniversita,
                                               @PathVariable String IDcorsoDiLaurea) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        AjaxResponseBody response = new AjaxResponseBody();
        if (uni != null) {
            uni.eliminaCorso(new Corso(IDcorsoDiLaurea));
            uniRepo.save(uni);
            response.setMsg("ok");
            return ResponseEntity.ok(response);
        }
        response.setMsg("not_found");
        return ResponseEntity.badRequest().body(response);
    }

    // Cancellazione di un esame da un corso in una università
    // TODO: Refactor and fix spaghetti code
    @DeleteMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}/{IDesame}")
    public ResponseEntity<?> deleteEsame (@PathVariable String IDuniversita,
                                               @PathVariable String IDcorsoDiLaurea,
                                               @PathVariable String IDesame) {
        Universita uni = uniRepo.findById(IDuniversita).orElse(null);
        AjaxResponseBody response = new AjaxResponseBody();
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

                response.setMsg("ok");
                return ResponseEntity.ok(response);
            }

        }
        response.setMsg("not_found");
        return ResponseEntity.badRequest().body(response);
    }

}
