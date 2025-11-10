package com.example.demo.notificacion.entity;

import com.example.demo.notificacion.strategy.IEstrategiaNotificacion;
import com.example.demo.user.entity.User;

import java.util.List;

public class Notificador {
    private List<IEstrategiaNotificacion> estrategias;

    public Notificador(List<IEstrategiaNotificacion> estrategias) {
        this.estrategias = estrategias;
    }

    public void setEstrategias(List<IEstrategiaNotificacion> estrategias) {
        this.estrategias = estrategias;
    }

    public void notificar(User usuario, String mensaje){
        for (IEstrategiaNotificacion estrategia : estrategias){
            estrategia.notificar(usuario, mensaje);
        }
    }
}
