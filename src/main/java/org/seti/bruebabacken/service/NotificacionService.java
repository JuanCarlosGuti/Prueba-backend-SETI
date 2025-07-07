package org.seti.bruebabacken.service;

import org.seti.bruebabacken.model.Cliente;
import org.seti.bruebabacken.model.Fondo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${app.notifications.enabled:true}")
    private boolean notificationsEnabled;

    @Value("${app.notifications.email.enabled:true}")
    private boolean emailEnabled;

    public void notificarSuscripcion(Cliente cliente, Fondo fondo, String monto) {
        if (!notificationsEnabled || !emailEnabled) {
            return;
        }

        String mensaje = String.format(
            "¡Hola %s! Tu suscripción al fondo %s por $%s COP ha sido exitosa. " +
            "Tu saldo actual es: $%s COP. Gracias por confiar en BTG Pactual.",
            cliente.getNombre(),
            fondo.getNombre(),
            monto,
            cliente.getSaldo()
        );

        enviarEmail(cliente.getEmail(), "Suscripción Exitosa - BTG Pactual", mensaje);
    }

    public void notificarCancelacion(Cliente cliente, Fondo fondo, String monto) {
        if (!notificationsEnabled || !emailEnabled) {
            return;
        }

        String mensaje = String.format(
            "¡Hola %s! Tu cancelación de suscripción al fondo %s por $%s COP ha sido procesada. " +
            "Tu saldo actual es: $%s COP. Gracias por confiar en BTG Pactual.",
            cliente.getNombre(),
            fondo.getNombre(),
            monto,
            cliente.getSaldo()
        );

        enviarEmail(cliente.getEmail(), "Cancelación Exitosa - BTG Pactual", mensaje);
    }

    private void enviarEmail(String email, String asunto, String mensaje) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(asunto);
            mailMessage.setText(mensaje);
            mailMessage.setFrom("noreply@btgpactual.com");

            emailSender.send(mailMessage);
            System.out.println("✅ Email enviado exitosamente a: " + email);
        } catch (Exception e) {
            System.err.println("❌ Error enviando email a " + email + ": " + e.getMessage());
        }
    }
} 