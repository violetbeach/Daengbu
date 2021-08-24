package com.violetbeach.daengbu.controller.v1.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.chat.ChatDto;
import com.violetbeach.daengbu.dto.model.chat.RoomDto;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.dto.response.Response;
import com.violetbeach.daengbu.service.ChatService;
import com.violetbeach.daengbu.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
@RequestMapping("/api/v1/chat")
public class ChatController {
	
	private final ChatService chatService;
	
	private final UserService userService;
	
	@ApiOperation(value = "메시지 생성", notes = "채팅방 id와 text를 이용하여 메시지를 생성합니다.")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Response createMessage(Long roomId, String message) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		ChatDto chatDto = new ChatDto()
				.setRoomId(roomId)
				.setUserId(userDto.getId())
				.setMessage(message);
		chatService.addChat(chatDto);
		return Response.ok();
	}
	
	@ApiOperation(value = "방 생성", notes = "상대방 사용자 id로 채팅방을 생성합니다.")
	@PostMapping("/room")
	@ResponseStatus(HttpStatus.CREATED)
	public Response createRoom(Long authorId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		Long id = chatService.getByUserId(userDto.getId(), authorId);
		if(id!=null) {
			return Response.ok().setPayload("/chat/" + id);
		} else {
			RoomDto roomDto = new RoomDto();
			chatService.createRoom(roomDto);
			chatService.addUser(roomDto.getId(), userDto.getId());
			chatService.addUser(roomDto.getId(), authorId);
			return Response.ok().setPayload("/chat/" + roomDto.getId());
		}
	}
	
	@ApiOperation(value = "메시지 알림 수 조회", notes = "사용자 id로 안 읽은 메시지 수를 조회합니다.")
	@GetMapping("/alert-count")
	public Response getAlertCount() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		return Response.ok().setPayload(chatService.getTotalUnreadCount(userDto.getId()));
	}
	
	@ApiOperation(value = "채팅방 나가기", notes = "채팅방 id와 사용자 id로 채팅방을 나갑니다.")
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void exitRoom(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		chatService.delRoomUser(id, userDto.getId());
	}
		
}