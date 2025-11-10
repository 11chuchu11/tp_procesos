package com.example.demo.notificacion;

import com.example.demo.notificacion.adapter.AdapterJavaPush;
import com.example.demo.notificacion.adapter.IEstrategiaNotificacion;
import com.example.demo.notificacion.entity.NotificacionEmail;
import com.example.demo.notificacion.entity.NotificacionPush;
import com.example.demo.notificacion.entity.Notificador;
import com.example.demo.notificacion.factory.EmailAdapterFactory;
import com.example.demo.notificacion.smtp.SmtpConfig;

import java.util.Arrays;

public class DemoNotificacion {
    public static void main(String[] args) {
        // Gmail SMTP config
        SmtpConfig gmailCfg = new SmtpConfig(
                "smtp.gmail.com",     // host
                465,               // 587 (STARTTLS) o 465 (SSL)
                "manutest2799@gmail.com", // proveedor
                "dhkjgqgwnlqftnat", // api password
                false,              // startTls (true si es 587)
                true,             // ssl (true si es 465)
                "manutest2799@gmail.com",   // FROM
                true,             // debug
                false               // html
        );

        // Adapter
        EmailAdapterFactory factory = new EmailAdapterFactory(gmailCfg);

        // Strategy
        IEstrategiaNotificacion emailStrategy = new NotificacionEmail(factory);
        IEstrategiaNotificacion pushStrategy  = new NotificacionPush(new AdapterJavaPush());

        Notificador notificador = new Notificador(Arrays.asList(emailStrategy, pushStrategy));

        // Pruebas
        //Usuario uHotmail = new Usuario("leclerc", "leclerc123@hotmail.com"); // simulado por consola
        //Usuario uGmail   = new Usuario("Manu", "mattrexyqunoa@gmail.com"); // envío por Gmail
        //Usuario uOtro    = new Usuario("Braian", "Otrera996@gmail.com"); // envío por Gmail

        System.out.println("HOTMAIL (simulación por consola)");
        //notificador.notificar(uHotmail, "Hola! Esto es una prueba para Hotmail.");

        System.out.println("\nGMAIL (envío real)");
        //notificador.notificar(uGmail, "Hola! Esto es una prueba por Gmail.");

        System.out.println("\nGMAIL (envío real)");
        //notificador.notificar(uOtro, "Hola! Esto es una prueba por Gmail.");
    }
}