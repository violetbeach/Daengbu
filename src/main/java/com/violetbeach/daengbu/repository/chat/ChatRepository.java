package com.violetbeach.daengbu.repository.chat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.violetbeach.daengbu.dto.model.chat.ChatDto;
import com.violetbeach.daengbu.dto.model.chat.RoomDto;

@Repository
public interface ChatRepository {
	
	Long getByUserId(Long userId1, Long userId2);
	void createRoom(RoomDto roomDto);
	void addUser(Long roomId, Long userId);
	void addMessage(ChatDto chatDto);
	List<ChatDto> getListByUserId(Long userId);
	Long getOtherUserByUserId(Long roomId, Long userId);
	Integer getUnreadMessageCount(Long roomId, Long userId);
	void updateIsRead(Long roomId, Long userId);
	List<ChatDto> getByRoomId(Long id);
	Integer getTotalUnreadCount(Long user_id);
	void delRoomUser(Long roomId, Long userId);
	
}