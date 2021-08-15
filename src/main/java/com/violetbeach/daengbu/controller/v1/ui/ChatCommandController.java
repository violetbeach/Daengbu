package com.violetbeach.daengbu.controller.v1.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.violetbeach.daengbu.dto.model.chat.ChatDto;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.service.ChatService;
import com.violetbeach.daengbu.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatCommandController {
	
	private final UserService userService;
	
	private final ChatService chatService;
	
	@GetMapping("/chat")
	public ModelAndView initChatListForm(){
		ModelAndView modelAndView = new ModelAndView("chatlist");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		List<ChatDto> chatListDto = chatService.getListByUserId(userDto.getId());
		List<String> otherUserList = new ArrayList<>();
		List<Integer> unreadMessageCountList = new ArrayList<>();
		for(ChatDto chatDto : chatListDto) {
			Long other = chatService.getOtherUserByUserId(chatDto.getRoomId(), userDto.getId());
			otherUserList.add(userService.getUsernameById(other));
			unreadMessageCountList.add(chatService.getUnreadMessageCount(other));
		}
		modelAndView.addObject("chatListDto", chatListDto);
		modelAndView.addObject("otherUserList", otherUserList);
		modelAndView.addObject("unreadMessageCountList", unreadMessageCountList);
		return modelAndView;
	}
	
	@GetMapping("/chat/{id}")
	public ModelAndView initChatForm(@PathVariable Long id){
		ModelAndView modelAndView = new ModelAndView("chat");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		List<ChatDto> chatListDto = chatService.getByRoomId(id);
		Long other = chatService.getOtherUserByUserId(id, userDto.getId());
		String otherUsername;
		if (other==null) otherUsername = "(알수 없음)";
		else otherUsername = userService.getUsernameById(other);
		List<Boolean> chatIsMineList = new ArrayList<>();
		for(ChatDto chatDto : chatListDto) {
			chatIsMineList.add(chatDto.getUserId() == userDto.getId());
		}
		chatService.updateIsRead(id, userDto.getId());
		modelAndView.addObject("chatListDto", chatListDto);
		modelAndView.addObject("otherUsername", otherUsername);
		modelAndView.addObject("chatIsMineList", chatIsMineList);
		return modelAndView;
	}
	
	@PostMapping("/chat/{id}")
	public String initChatFragment(@PathVariable Long id, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		List<ChatDto> chatListDto = chatService.getByRoomId(id);
		List<Boolean> chatIsMineList = new ArrayList<>();
		for(ChatDto chatDto : chatListDto) {
			chatIsMineList.add(chatDto.getUserId() == userDto.getId());
		}
		chatService.updateIsRead(id, userDto.getId());
		model.addAttribute("chatListDto", chatListDto);
		model.addAttribute("chatIsMineList", chatIsMineList);
		return "chat :: #chat_fragment";
	}
		
}