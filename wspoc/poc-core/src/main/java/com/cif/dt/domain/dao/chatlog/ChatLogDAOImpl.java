package com.cif.dt.domain.dao.chatlog;

import javax.persistence.TypedQuery;

import com.cif.dt.domain.dao.AbstractDAO;
import com.cif.dt.domain.entities.ChatLog;
import com.google.inject.persist.Transactional;

public class ChatLogDAOImpl extends AbstractDAO<ChatLog> implements ChatLogDAO {

	public Class<ChatLog> getClazz() {
		return ChatLog.class;
	}

}
