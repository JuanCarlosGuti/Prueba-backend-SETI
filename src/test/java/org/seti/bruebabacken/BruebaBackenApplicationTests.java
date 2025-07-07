package org.seti.bruebabacken;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.seti.bruebabacken.model.Cliente;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BruebaBackenApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void clienteTieneSaldoInicialCorrecto() {
        Cliente cliente = new Cliente();
        assertEquals(new BigDecimal("500000"), cliente.getSaldo(), "El saldo inicial debe ser 500000");
    }

    @Test
    void clientePuedeAsignarEmail() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@email.com");
        assertEquals("test@email.com", cliente.getEmail());
    }
}
