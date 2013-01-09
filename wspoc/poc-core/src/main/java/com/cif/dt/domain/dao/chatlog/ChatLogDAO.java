package com.cif.dt.domain.dao.chatlog;

import com.cif.dt.domain.dao.DAO;
import com.cif.dt.domain.entities.ChatLog;
import com.google.inject.ImplementedBy;

@ImplementedBy(ChatLogDAOImpl.class)
public interface ChatLogDAO extends DAO<ChatLog> {
	
}
