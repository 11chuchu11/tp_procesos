package com.example.demo.notificacion.entity;

import com.example.demo.notificacion.adapter.AdapterJavaPush;
import com.example.demo.notificacion.adapter.IEstrategiaNotificacion;
import com.example.demo.user.entity.User;

public class NotificacionPush implements IEstrategiaNotificacion {


    public NotificacionPush(AdapterJavaPush adapterJavaPush) {

    }

    @Override
    public void notificar(User usuario, String mensaje) {

    }


}
