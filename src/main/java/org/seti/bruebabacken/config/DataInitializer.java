package org.seti.bruebabacken.config;

import org.seti.bruebabacken.model.Fondo;
import org.seti.bruebabacken.repository.FondoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FondoRepository fondoRepository;

    @Autowired
    public DataInitializer(FondoRepository fondoRepository) {
        this.fondoRepository = fondoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen fondos
        if (fondoRepository.count() == 0) {
            crearFondosIniciales();
        }
    }

    private void crearFondosIniciales() {
        // Fondo 1
        Fondo fondo1 = new Fondo();
        fondo1.setId("1");
        fondo1.setNombre("FPV_BTG_PACTUAL_RECAUDADORA");
        fondo1.setMontoMinimo(new BigDecimal("75000"));
        fondo1.setCategoria("FPV");
        fondoRepository.save(fondo1);

        // Fondo 2
        Fondo fondo2 = new Fondo();
        fondo2.setId("2");
        fondo2.setNombre("FPV_BTG_PACTUAL_ECOPETROL");
        fondo2.setMontoMinimo(new BigDecimal("125000"));
        fondo2.setCategoria("FPV");
        fondoRepository.save(fondo2);

        // Fondo 3
        Fondo fondo3 = new Fondo();
        fondo3.setId("3");
        fondo3.setNombre("DEUDAPRIVADA");
        fondo3.setMontoMinimo(new BigDecimal("50000"));
        fondo3.setCategoria("FIC");
        fondoRepository.save(fondo3);

        // Fondo 4
        Fondo fondo4 = new Fondo();
        fondo4.setId("4");
        fondo4.setNombre("FDO-ACCIONES");
        fondo4.setMontoMinimo(new BigDecimal("250000"));
        fondo4.setCategoria("FIC");
        fondoRepository.save(fondo4);

        // Fondo 5
        Fondo fondo5 = new Fondo();
        fondo5.setId("5");
        fondo5.setNombre("FPV_BTG_PACTUAL_DINAMICA");
        fondo5.setMontoMinimo(new BigDecimal("100000"));
        fondo5.setCategoria("FPV");
        fondoRepository.save(fondo5);

        System.out.println("✅ Fondos iniciales creados exitosamente");
    }
} 