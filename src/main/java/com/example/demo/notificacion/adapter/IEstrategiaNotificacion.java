package com.example.demo.notificacion.adapter;

import com.example.demo.user.entity.User;

public interface IEstrategiaNotificacion {
    void notificar(User usuario, String mensaje);
}
