package edu.tommcamm.laboratorio3.repositories;

import edu.tommcamm.laboratorio3.entities.Corso;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CorsoRepository extends CrudRepository<Corso, String> {

}
