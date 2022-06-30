<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/head.jsp"/>	
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css">
	<script src="${pageContext.request.contextPath }/resources/js/member.js"></script>	
</head>
<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<!-- 회원가입 메인 -->
    <main class="signup-main">
	    <form method="post" class="form-group" >
	        <ul>
	            <li>
	                <label for="id">아이디</label>
	                <input type="text" name="id" id="id" autocomplete="off" placeholder="아이디를 입력하세요">
	            	<p class="msg msg-gray"></p>
	            </li>
	            <li>
	                <label for="pwd">비밀번호</label>
	                <input type="password" name="pwd" id="pwd" placeholder="비밀번호를 입력하세요">
	            	<p class="msg"></p>
	            </li>
	            <li>
	                <label for="pwd2">비밀번호 확인</label>
	                <input type="password" name="pwd2" id="pwd2" placeholder="비밀번호를 확인하세요">
	           		<p class="msg"></p>
	            </li>
	            <li>
	                <label for="email">메일 주소</label>
	                <input type="text" name="email" id="email" autocomplete="off" placeholder="메일주소를 입력하세요">
	           		<p class="msg"></p>
	            </li>
	            <li>
	                <label for="name">이름</label>
	                <input type="text" name="name" id="name" autocomplete="off" placeholder="이름을 입력하세요">
	            	<p class="msg"></p>
	            </li>
	        </ul>
	     	<div class="bottom-box"></div>
        	<div class="signupbox"><button id="btnJoin">가입하기</button></div>
        	<div class="canclebox"><button id="cancle" type="reset" onclick="history.go(-1)">취소</button></div>
	    </form>
    </main>
    <script>
    $(function(){
    	
    	// 잘못된 입력이 있을경우 가입처리를 진행하지 않음 =======================================================================================
    	$("form").submit(function(e){
    		// 현재커서가 어느 태그에 위치하는지 체크
    		var curFocus = $(":focus").attr("id");
    		
    		// 각 입력 창에서 submit이벤트(enter입력)가 발생했을 경우 이벤트처리
    		if(curFocus === "id"){
    			enterSequence("#id", "#pwd", "blur");
    			return;
    		}
    		else if(curFocus === "pwd"){
    			enterSequence("#pwd", "#pwd2", "blur");
    			return;
    		}
    		else if(curFocus === "pwd2"){
    			enterSequence("#pwd2", "#email", "blur");
    			return;
    		}
    		else if(curFocus === "email"){
    			enterSequence("#email", "#name", "blur");
    			return;
    		}
    		else if(curFocus === "name"){
    			if(!enterSequence("#name", "#btnJoin", "blur")){
    				return;
    			}
    		}
    		
   			if(!idCheck){
   				alert("아이디 입력을 다시 확인해주세요.");
   				$("#id").focus();
   				// 결과전송 중단
   				e.preventDefault();
   			}
   			else if(!pwdCheck){
   				alert("비밀번호 입력을 다시 확인해주세요.");
   				$("#pwd").focus();
   				e.preventDefault();
   			}
   			else if(!pwd2Check){
   				alert("비밀번호확인 입력을 다시 확인해주세요.");
   				$("#pwd2").focus();
   				e.preventDefault();
   			}
   			else if(!emailCheck){
   				alert("이메일 입력을 다시 확인해주세요.");
   				$("#email").focus();
   				e.preventDefault();
   			}
   			else if(!nameCheck){
   				alert("이름입력을 다시 확인해주세요.");
   				$("#name").focus();
   				e.preventDefault();
   			}
   			else {
   				if(curFocus === "btnJoin"){
   					alert("회원가입을 축하드립니다! (❁´◡`❁) 많은 활동 부탁드려요! ╰(*°▽°*)╯");
   				}
   			}
    	});
 		
 		// 아이디 입력양식 확인 ==================================================================================================================
 		$("#id").blur(function(){
			var msgObj = $("#id").next();
			if(!$(this).val().length){
				msgObj.text("필수 정보입니다");
				msgObj.css("color", "red");
				idCheck = false;
				return false;
			}
			
			// 정규식 : 첫글자 영소문자, 첫글자이후 영소문자,숫자가능, 총 6~20글자 사이의 조건을 만족
			if(!(/^[a-z]+[a-z0-9]{5,19}$/g).test($(this).val())){
				msgObj.text("6~20자의 영문 소문자, 숫자만 사용가능합니다");
				msgObj.css("color", "red");
				idCheck = false;
				return false;
			}
			// 정규식 조건을 통과했을 경우 아이디 중복체크를 실행
			else {
			  return idValid();
			}
		});
 		
 		// 아이디 중복체크 함수
    	function idValid() {
			var msgObj = $("#id").next();
			var id = $("#id").val();
			
			$.ajax("idValid?id="+ id, {
				success : function(data) {
					//회원가입 가능
					if(data/1) { 
						msgObj.text("사용가능한 아이디입니다 <중복체크완료>");
						msgObj.css("color", "gray");
						idCheck = true;
						return true;
					}
					//회원가입 불가능
					else { 
						msgObj.text("이미 가입된 회원입니다");
						msgObj.css("color", "red");
						idCheck = false;
						return false;
					}
					/* console.log(id, data, typeof data, typeof (data/1)); */
				}
			});
		}
		
    	// 비밀번호 양식 확인
		pwdConfirm();
		
		// 이메일 양식 확인
		emailConfirm("join");
		
		// 이름 양식 확인
		nameConfirm();
		
     });
    </script>
	<jsp:include page="../common/footer.jsp"></jsp:include>
</body>
</html>