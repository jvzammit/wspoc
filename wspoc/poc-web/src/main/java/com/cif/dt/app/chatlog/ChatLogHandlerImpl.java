package com.cif.dt.app.chatlog;

import java.util.logging.Logger;

import com.cif.dt.domain.dao.chatlog.ChatLogDAO;
import com.cif.dt.domain.entities.ChatLog;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class ChatLogHandlerImpl implements ChatLogHandler {
	
	private final static Logger LOGGER = Logger.getLogger(ChatLogHandlerImpl.class.getName());
	
	final protected ChatLogDAO chatLogDAO;
	
	@Inject
	public ChatLogHandlerImpl(ChatLogDAO chatLogDAO) {
		super();
		this.chatLogDAO = chatLogDAO;
    }
	
	public ChatLog logChatMsg(int clientNo, String chatMsg) {
		ChatLog chatLog = new ChatLog(clientNo, chatMsg);
		this.chatLogDAO.insert(chatLog);
		this.chatLogDAO.refresh(chatLog); // refresh to get newly assigned DB id
		return chatLog;
	}
	
}