package com.cif.dt.app.chatlog;

import com.cif.dt.domain.entities.ChatLog;
import com.google.inject.ImplementedBy;

@ImplementedBy(ChatLogHandlerImpl.class)
public interface ChatLogHandler {
	
	ChatLog logChatMsg(int clientNo, String chatMsg);
	
}