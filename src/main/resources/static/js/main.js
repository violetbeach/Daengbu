$(document).ready(function(){

// 유저 정보 동기화
	$.ajax({
			async: 'true',
			url: '/api/v1/user/me',
			type : 'get',
			success : function(response, status, xhr){
				if(response['status']=='OK') {
					$('.has_authority').show();
					$('.username').html(response['payload']['username']);
				}
				else $('.anonymous').show();
			}
	});

// 로그아웃
	$(".btn_logout").click(function(){
		$.ajax({
			url: '/api/v1/user/logout',
			type : 'get',
			success : function(response, status, xhr){
				window.location = '/';
			}
		});
	});
	
});