package com.perfumeria.auth.domain.model.gateway;

import com.perfumeria.auth.domain.model.Notificacion;

public interface NotificationGateway {

    void enviarMensaje(Notificacion mensajeJson);
}