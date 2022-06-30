<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>ID/PWD 찾기 - Main Stream -</title>
	<jsp:include page="../common/head.jsp"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/member/findinfo.css">
</head>
<body>
	<jsp:include page="../common/header.jsp"/>
	<main>
		<div class="main-header">
			<h2>ID/PWD 찾기입니다.</h2>
		</div>
		<div class="main-content">
			<div class="emailBox">
				<label for="email">가입시 사용한 이메일을 입력해주세요</label>
				<form method="post" class="emailCheck-frm">
					<input type="email" name="email" id="email" placeholder="이메일을 입력해주세요" autocomplete="off">
					<button type="submit">확인하기</button>
					<p class="msg"></p>
				</form>
			</div>
			<div class="findBox">
				<form method="post" class="id-pwd">
					<button id="find-id">아이디 찾기</button>		
					<button id="find-pwd">비밀번호 찾기</button>
					<input type="text" name="id-auth" id="id-auth" placeholder="인증번호를 입력해주세요" autocomplete="off">		
					<input type="text" name="pwd-auth" id="pwd-auth" placeholder="인증번호를 입력해주세요" autocomplete="off">
					<button type="submit" id="authNumCheck">인증하기</button>		
				</form>
				<p class="msg"></p>
			</div>
		</div>
		<div class="btnBox">
			<form method="post">
				<button id="login-btn">로그인</button>
				<button id="back-btn" type="reset" onclick="history.go(-1)">뒤로</button>
			</form>
		</div>
	</main>
	<script>
		var email = "";
		var authNumber = "";
		
		$(function(){
			
			$(".id-pwd").submit(function(e){
				e.preventDefault();
				var curFocus = $(":focus").attr("id");
				
				if(curFocus === "find-id" || curFocus === "find-pwd"){
					if(curFocus === "find-id"){
						$(".findBox").find("#id-auth").css("display", "inline-block");
						$(".findBox").find("#pwd-auth").css("display", "none");
					}
					else if(curFocus === "find-pwd"){
						$(".findBox").find("#pwd-auth").css("display", "inline-block");
						$(".findBox").find("#id-auth").css("display", "none");
					}
					$("#authNumCheck").css("display", "inline-block");
					
					$(".findBox").find(".msg").text("메일로 인증코드를 보냈습니다.");
					$.ajax("emailSend?email=" + email, {
						success : function(data){
							authNumber = data;
						}
					});
				}
				else if((curFocus === "id-auth" || curFocus === "authNumCheck") && $("#pwd-auth").css("display") == "none" ){
					
					if($("#id-auth").val() === authNumber){
						$.ajax("getFindId?email=" + email,{
							success : function(data){
								alert("회원님의 아이디는 : " + data + " 입니다.");
							}
						});
					}
					else{
						alert("유효하지 않은 인증번호 입니다.");
					}
				}
				else if((curFocus === "pwd-auth" || curFocus === "authNumCheck") && $("#id-auth").css("display") == "none"){
					
					if($("#pwd-auth").val() === authNumber){
						$.ajax("getNewPwd?email=" + email,{
							success : function(data){
								alert("회원님의 새 비밀번호는 : " + data + " 입니다.");
								alert("로그인후 비밀번호를 다시 설정해주세요.");
							}
						});
					}
					else{
						alert("유효하지 않은 인증번호 입니다.");
					}
				}
			});
			
			
			$(".emailCheck-frm").submit(function(e){
				e.preventDefault();
				var msgObj = $(this).find("p");
				email = $("#email").val();
				
				if(email){
					$.ajax("emailValid?email="+ email + "&findInfo=true", {
						success : function(data) {
							// 존재하지 않는 이메일
							if(data/1){
								msgObj.text("잘못된 이메일입니다.").css("color", "red");
								email = "";
								
								$(".findBox").css("display", "none");
							}
							// 존재하는 이메일
							else{
								msgObj.text("올바른 이메일입니다.").css("color", "black");
								
								// 이메일 체크 통과 했을때 메일로 아이디 or 비밀번호 찾기 버튼 활성화
								$(".findBox").css("display", "block");
							}
						}
					});
				}
				else{
					msgObj.text("이메일을 입력해주세요!!!");
					msgObj.css("color", "red");
				}
			});
			
			
		});
	</script>
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>