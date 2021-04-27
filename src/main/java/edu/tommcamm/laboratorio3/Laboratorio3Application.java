package edu.tommcamm.laboratorio3;

import edu.tommcamm.laboratorio3.repositories.CorsoRepository;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Laboratorio3Application {

    public static void main(String[] args) {
        SpringApplication.run(Laboratorio3Application.class, args);
    }

}
