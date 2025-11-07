import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Properties;

public class AdapterJakartaMail implements IAdapterEmail {

    private final Session session;
    private final String from;
    private final boolean html;

    public AdapterJakartaMail(SmtpConfig config) {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", String.valueOf(config.getPort()));

        if (config.isStartTls()) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (config.isSsl()) {
            props.put("mail.smtp.ssl.enable", "true");
        }

        boolean needsAuth = config.getUsername() != null && !config.getUsername().isEmpty();
        props.put("mail.smtp.auth", String.valueOf(needsAuth));

        Authenticator authenticator = needsAuth
                ? new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        config.getUsername(),
                        config.getPassword()
                );
            }
        }
                : null;

        this.session = Session.getInstance(props, authenticator);
        this.session.setDebug(config.isDebug());
        this.from = config.getFrom();
        this.html = config.isHtml();
    }

    @Override
    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        try {
            MimeMessage mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(from));
            mensaje.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatario, false));
            mensaje.setSubject(asunto, "UTF-8");
            mensaje.setSentDate(new Date());

            if (html) {
                mensaje.setContent(cuerpo, "text/html; charset=UTF-8");
            } else {
                mensaje.setText(cuerpo, "UTF-8");
            }

            Transport.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo", e);
        }
    }
}