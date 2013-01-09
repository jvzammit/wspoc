package com.cif.dt.app.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import com.cif.dt.app.chatlog.ChatLogHandler;
import com.cif.dt.app.util.HTMLFilter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class MainSocket extends WebSocketServlet {	
	private static final long serialVersionUID = 1L;
	
	private final AtomicInteger connectionIds = new AtomicInteger(0);
	
	private final Set<SocketMessageInbound> connections =
            new CopyOnWriteArraySet<SocketMessageInbound>();
	
	private Provider<ChatLogHandler> chatLogHdlr;
	
    @Inject
	public void setChatLogHandler(Provider<ChatLogHandler> chatLogHdlr) {
    	this.chatLogHdlr = chatLogHdlr;
    }
    
    @Inject
    public MainSocket() {
    }
	
    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {    	
    	ServletContext servletContext = request.getServletContext();
    	
    	SocketMessageInbound socketMessageInbound = new SocketMessageInbound(
    		connectionIds.incrementAndGet(),
    		this.chatLogHdlr.get()
		); 
	    	
		servletContext.setAttribute("websocketconns", connections);
		return socketMessageInbound; 
    }
    
    public class SocketMessageInbound extends MessageInbound {
    	
    	private final boolean DO_LOG = true;
    	
    	private int clientNo;
    	
    	private ChatLogHandler _chatLogHdlr; // SocketMessageInbound chatLogHdlr
    	
    	public SocketMessageInbound(int clientNo,
    			ChatLogHandler chatLogHdlr) {
    	    super();
    	    this.clientNo = clientNo;
    	    this._chatLogHdlr = chatLogHdlr;    	    
        }

    	@Override
    	protected void onOpen(WsOutbound outbound) {
    		connections.add(this);
    		String message = String.format("* Guest %s %s",
                    this.clientNo, "has joined.");
    		broadcast(message);
    	};
    	
    	@Override
    	protected void onClose(int status) {
    		connections.remove(this);
    		String message = String.format("* Guest %s %s",
                    this.clientNo, "has disconnected.");
    		broadcast(message);
    	};

    	@Override
    	protected void onBinaryMessage(ByteBuffer message) throws IOException {
    		throw new UnsupportedOperationException("Binary message not supported.");
    	}

    	@Override
    	protected void onTextMessage(CharBuffer cb) throws IOException {
    		String filteredMessage = String.format("Guest %s: %s",
    				clientNo, HTMLFilter.filter(cb.toString()));
    		if (this.DO_LOG) {
    			this._chatLogHdlr.logChatMsg(clientNo, filteredMessage);
    		}
            broadcast(filteredMessage);
    	}
    	
    	private void broadcast(String message) {	
            for (SocketMessageInbound connection : connections) {            	
            	try {
            		CharBuffer buffer = CharBuffer.wrap(message);
            		connection.getWsOutbound().writeTextMessage(buffer);
        		} catch (IOException ignore) {
        			// 	ignore
        		}
            }
    	}
    }

}
