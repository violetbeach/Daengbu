package com.violetbeach.daengbu.dto.model.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class KakaoProfile {	
	public Integer id;
	public String connected_at;
	public Properties properties;
	public KakaoAccount kakao_account;

	@Data
	public class Properties {
		public String nickname;
		public String profile_image;
		public String thumbnail_image;
	}
	
	@Data
	public class KakaoAccount {
		public Boolean profile_needs_agreement;
		public Profile profile;
		public Boolean has_email;
		public Boolean email_needs_agreement;
		public Boolean is_email_valid;
		public Boolean is_email_verified;
		public String email;
		
		@Data
		public class Profile {
			public String nickname;
			public String thumbnail_image_url;
			public Boolean is_default_image;
			public String profile_image_url;
		}
	}
}