package com.cif.dt.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cif.dt.domain.AbstractEntity;

@Entity
@Table(
	uniqueConstraints=@UniqueConstraint(columnNames={"username"})
)
public class ChatLog extends AbstractEntity {
	static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="id", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Column(name="client_no", nullable=false)
	private int clientNo;
	
	@Column(name="chat_msg", nullable=false, length=64)
	private String chatMsg;
	
	public ChatLog() {}
	
	public ChatLog(int clientNo, String chatMsg) {
		this.clientNo = clientNo;
		this.chatMsg = chatMsg;
	}
	
	public Long getId() { return this.id; }

	public int getClientNo() {
    	return clientNo;
    }

	public void setClientNo(int clientNo) {
    	this.clientNo = clientNo;
    }

	public String getChatMsg() {
    	return chatMsg;
    }

	public void setChatMsg(String chatMsg) {
    	this.chatMsg = chatMsg;
    }	
	

}
