import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;           
import javax.websocket.OnClose;            
import javax.websocket.OnMessage;          
import javax.websocket.OnOpen;               

import java.util.logging.Logger;              
import java.util.*;

@ServerEndpoint("/chat")
public class ChatServer{
    
    private static List<Session> sessions = new ArrayList<Session>();;
    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.info("Abrindo conexao com a sessao: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("Fechando conexao com a sessao: " + session.getId());
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("sessao " + session.getId() + " mandou essa msg: `" + message);
        for (Session sess : sessions) {
                try {
                    if (sess != session)
                        sess.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    logger.warning("Deu ruim rapazeada " + sess.getId());
                    sessions.remove(sess);
                }
        }
    }
}