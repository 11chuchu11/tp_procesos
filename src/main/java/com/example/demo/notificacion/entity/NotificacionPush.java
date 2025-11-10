package com.example.demo.notificacion.entity;

import com.example.demo.notificacion.adapter.AdapterJavaPush;
import com.example.demo.notificacion.adapter.IEstrategiaNotificacion;
import com.example.demo.user.entity.User;

public class NotificacionPush implements IEstrategiaNotificacion {
    private final com.example.demo.notificacion.adapter.IAdapterPush adapter;

    public NotificacionPush(com.example.demo.notificacion.adapter.IAdapterPush adapterJavaPush) {
        this.adapter = adapterJavaPush;
    }

    @Override
    public void notificar(User usuario, String mensaje) {
        if (usuario == null) return;
        String destinatario = usuario.getUsername();
        adapter.enviarPush(destinatario, mensaje);
    }

}
