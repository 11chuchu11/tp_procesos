package com.example.demo.notificacion.entity;


import com.example.demo.notificacion.adapter.IAdapterEmail;
import com.example.demo.notificacion.adapter.IEstrategiaNotificacion;
import com.example.demo.notificacion.factory.EmailAdapterFactory;
import com.example.demo.user.entity.User;


public class NotificacionEmail implements IEstrategiaNotificacion {

    private final EmailAdapterFactory factory;

    public NotificacionEmail(EmailAdapterFactory factory) {
        this.factory = factory;
    }

    @Override
    public void notificar(User usuario, String mensaje) {
        if (usuario == null || usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            System.out.println("Usuario sin email. No se envía.");
            return;
        }
        IAdapterEmail adapter = factory.getAdapter(usuario.getEmail());
        String asunto = "Mensaje para " + usuario.getUsername();
        String cuerpo = mensaje;
        adapter.enviarEmail(usuario.getEmail(), asunto, cuerpo);
    }
}