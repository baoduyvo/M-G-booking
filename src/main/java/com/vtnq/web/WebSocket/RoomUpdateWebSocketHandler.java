package com.vtnq.web.WebSocket;

import com.vtnq.web.Entities.Type;
import com.vtnq.web.Repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoomUpdateWebSocketHandler extends TextWebSocketHandler {
    private List<WebSocketSession>sessions=new ArrayList<>();
    @Autowired
    private TypeRepository typeRepository;
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
    public void NotifyRoomStatus(int RoomType,boolean status) throws Exception{
        Type type=typeRepository.findById(RoomType).orElse(null);
        String message="Room Type "+type.getName()+" is "+((status ? "available" : "already booked")) + ".";
        for (WebSocketSession session : sessions) {
            if(session.isOpen()){
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}
