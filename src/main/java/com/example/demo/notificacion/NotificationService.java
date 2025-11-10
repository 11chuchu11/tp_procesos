package com.example.demo.notificacion;

import com.example.demo.notificacion.adapter.AdapterJavaPush;
import com.example.demo.notificacion.entity.NotificacionEmail;
import com.example.demo.notificacion.entity.NotificacionPush;
import com.example.demo.notificacion.entity.Notificador;
import com.example.demo.notificacion.factory.EmailAdapterFactory;
import com.example.demo.notificacion.smtp.SmtpConfig;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.profile.entity.Profile;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final Notificador notificador;

    public NotificationService(
            @Value("${notification.smtp.host:}") String host,
            @Value("${notification.smtp.port:0}") int port,
            @Value("${notification.smtp.username:}") String username,
            @Value("${notification.smtp.password:}") String password,
            @Value("${notification.smtp.starttls:false}") boolean startTls,
            @Value("${notification.smtp.ssl:false}") boolean ssl,
            @Value("${notification.smtp.from:}") String from,
            @Value("${notification.smtp.debug:false}") boolean debug,
            @Value("${notification.smtp.html:false}") boolean html
    ) {
        SmtpConfig cfg = new SmtpConfig(
                host,
                port,
                (username != null && !username.isEmpty()) ? username : null,
                (password != null && !password.isEmpty()) ? password : null,
                startTls,
                ssl,
                from,
                debug,
                html
        );

        EmailAdapterFactory factory = new EmailAdapterFactory(cfg);

        // build strategies: email + push (console)
        NotificacionEmail emailStrategy = new NotificacionEmail(factory);
        NotificacionPush pushStrategy = new NotificacionPush(new AdapterJavaPush());

        List<com.example.demo.notificacion.adapter.IEstrategiaNotificacion> estrategias = new ArrayList<>();
        estrategias.add(emailStrategy);
        estrategias.add(pushStrategy);

        this.notificador = new Notificador(estrategias);
    }

    public void notifyUser(User usuario, String mensaje) {
        if (usuario == null) return;
        try {
            notificador.notificar(usuario, mensaje);
            System.out.println("Notification sent to user: " + usuario.getUsername() + " <" + usuario.getEmail() + ">");
        } catch (Exception e) {
            // Log the error but do not rethrow to avoid rolling back business transactions
            System.out.println("ERROR sending notification to user " + usuario.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void notifyUsers(List<User> usuarios, String mensaje) {
        if (usuarios == null) return;
        for (User u : usuarios) {
            notifyUser(u, mensaje);
        }
    }

    public void notifyProfiles(List<Profile> profiles, String mensaje) {
        if (profiles == null) return;
        for (Profile p : profiles) {
            if (p.getUser() != null) notifyUser(p.getUser(), mensaje);
        }
    }

    public void notifyPlayersInScrim(Scrim scrim, String mensaje) {
        List<User> users = new ArrayList<>();
        if (scrim.getLobby() != null && scrim.getLobby().getTeams() != null) {
            scrim.getLobby().getTeams().forEach(team -> {
                if (team.getPlayers() != null) {
                    team.getPlayers().forEach(p -> {
                        if (p.getUser() != null) users.add(p.getUser());
                    });
                }
            });
        }
        notifyUsers(users, mensaje);
    }
}
