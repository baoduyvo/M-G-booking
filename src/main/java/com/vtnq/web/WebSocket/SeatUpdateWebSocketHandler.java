package com.vtnq.web.WebSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtnq.web.Controllers.Seat.SeatDTO;
import com.vtnq.web.Entities.Seat;
import com.vtnq.web.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeatUpdateWebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession>sessions=new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

     @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
       for (WebSocketSession session1 : sessions) {
           if(session1.isOpen()){
               session1.sendMessage(message);
           }
       }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }
    public void notifySeatStatus(Seat seat, boolean status) throws IOException {
      String message="Seat "+seat.getIndex()+ " is "+((status ? "available" : "already booked")) + ".";
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage(message));
            }
        }
    }

}
