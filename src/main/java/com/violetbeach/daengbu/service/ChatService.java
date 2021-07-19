package com.violetbeach.daengbu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.violetbeach.daengbu.dto.model.chat.ChatDto;
import com.violetbeach.daengbu.dto.model.chat.RoomDto;
import com.violetbeach.daengbu.repository.chat.ChatRepository;

@Service
public class ChatService {

	@Autowired
	ChatRepository chatRepository;
	
	public Long getByUserId(Long userId1, Long userId2) {
		return chatRepository.getByUserId(userId1, userId2);
	}
	
	public void createRoom(RoomDto roomDto) {
		chatRepository.createRoom(roomDto);
	}
	
	public void addUser(Long roomId, Long userId) {
		chatRepository.addUser(roomId, userId);
	}
	
	public void addChat(ChatDto chatDto) {
		chatRepository.addMessage(chatDto);
	}
	
	public List<ChatDto> getListByUserId(Long userId){
		return chatRepository.getListByUserId(userId);
	}
	
	public Long getOtherUserByUserId(Long roomId, Long userId) {
		return chatRepository.getOtherUserByUserId(roomId, userId);
	}
	
	public Integer getUnreadMessageCount(Long id) {
		return chatRepository.getUnreadMessageCount(id);
	}
	
	public void updateIsRead(Long roomId, Long userId) {
		chatRepository.updateIsRead(roomId, userId);
	}
	
	public List<ChatDto> getByRoomId(Long id) {
		return chatRepository.getByRoomId(id);
	}
	
}