var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function(){

	var emailPattern = RegExp(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i);
	var passwordPattern = RegExp(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/);
	var usernamePattern = RegExp(/\s/g);
	var telPattern = RegExp(/^\d{2,3}\d{3,4}\d{4}$/);
	var emailFlag = false;
	var usernameFlag = false;
	var passwordFlag = false;
	var passwordConfirmFlag = false;
	var telFlag = false;
	
	$("#input_email").focusin(function(){
		emailFlag = false;
	});
	
	$("#input_email").focusout(function(){
		if($("#input_email").val().trim()=="") $("#valid_email").text("필수 정보입니다.")
		else{
			$.ajax({
			async : true,
			url: "api/v1/user/check-dup-email?email="+$("#input_email").val(),
			type : 'get',
			contentType: "application/json; charset=UTF-8",
			success: function(data, status){
				if(data==true) $("#valid_email").text("이미 사용중이거나 탈퇴한 아이디입니다.");
				else if(!emailPattern.test($("#input_email").val())) $("#valid_email").text("올바른 이메일 형식이 아닙니다.");
				else{
					$("#valid_email").text("");
					emailFlag = true;
				}
			},
			error: function(request, error){
				alert(request, error);
			}
		});
		}
	});
	
	$("#input_username").focusin(function(){
		usernameFlag = false;
	});
	
	$("#input_username").focusout(function(){
		if($("#input_username").val().trim()=="") $("#valid_username").text("필수 정보입니다.");
		else{
			$.ajax({
			async : true,
			url: "api/v1/user/check-dup-username?username="+$("#input_username").val(),
			type : 'get',
			contentType: "application/json; charset=UTF-8",
			success: function(data, status){
				if(data==true) $("#valid_username").text("이미 사용중인 닉네임입니다.");
				else if(usernamePattern.test($("#input_username").val())) $("#valid_username").text("공백은 사용할 수 없습니다.");
				else if($("#input_username").val().length<2) $("#valid_username").text("2~16자만 사용 가능합니다.");
				else{
					$("#valid_username").text("");
					usernameFlag = true;
				}
			},
			error: function(request, error){
				alert(request, error);
			}
		});
		}
	});
	
	$("#input_password").focusin(function(){
		passwordFlag = false;
	});
	
	$("#input_password").focusout(function(){
		if($("#input_password").val().trim()=="") $("#valid_password").text("필수 정보입니다.");
		else if(!passwordPattern.test($("#input_password").val())) $("#valid_password").text("8~16자 영문 소문자, 숫자, 특수문자를 사용하세요.");
		else{
			$("#valid_password").text("");
			passwordFlag = true;
		}
	});
	
	$("#input_password_confirm").focusin(function(){
		passwordConfirmFlag = false;
	});
	
	$("#input_password_confirm").focusout(function(){
		if($("#input_password_confirm").val().trim()=="") $("#valid_password_confirm").text("필수 정보입니다.");
		else if($("#input_password_confirm").val()!=$("#input_password").val()) $("#valid_password_confirm").text("비밀번호가 일치하지 않습니다.");
		else{
			$("#valid_password_confirm").text("");
			passwordConfirmFlag = true;
		}
	});
	
	$("#input_tel").focusin(function(){
		telFlag = false;
	});
	
	$("#input_tel").focusout(function(){
		if($("#input_tel").val().trim()=="") $("#valid_tel").text("필수 정보입니다.");
		else if(!telPattern.test($("#input_tel").val())) $("#valid_tel").text("형식에 맞지 않는 번호입니다.");
		else{
			$("#valid_tel").text("");
			telFlag = true;
		}
	});
	
	$("#btn_signup_submit").click(function(){
		if(emailFlag==true && usernameFlag==true && passwordFlag==true && passwordConfirmFlag==true && telFlag==true) {
			$("#btn_signup_submit").prop("type","submit");
		} else $('html, body').animate({scrollTop : 0}, 100);
	})
	
});