<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>MainStream! 내 정보</title>
	<jsp:include page="../common/head.jsp"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/info.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/modal.css">
	<script src="${pageContext.request.contextPath }/resources/js/member.js"></script>
</head>
<body>
	<jsp:include page="../common/header.jsp"/>
	<main class="info-main">
		<h2>${member.name }님의 정보</h2>
		<form method="post" class="form-group">
		<ul>
			<li>
				<p class="p-info">ID : ${member.id}</p>
			</li>
			<li>
				<button id="pwdEdit">비밀번호 변경</button>
				<input type="hidden" name="pwd" id="pwd" placeholder="변경할 비밀번호를 입력하세요">
				<p class="msg"></p>
				<input type="hidden" name="pwd2" id="pwd2" placeholder="비밀번호를 한번더 입력하세요">
				<p class="msg"></p>
			</li>
			<li>
				<p class="p-info">EMAIL : ${member.email}</p>
				<button id="emailEdit">메일 변경</button>
				<input type="hidden" name="emaile" id="email" autocomplete="off" placeholder="변경할이메일을 입력하세요">
				<p class="msg"></p>
			</li>
			<li>
				<p class="p-info">NAME : ${member.name}</p>
				<button id="nameEdit">이름 변경</button>
				<input type="hidden" name="name" id="name" autocomplete="off" placeholder="변경할이름을 입력하세요">
				<p class="msg"></p>
			</li>
			<li>
				<button id="signOut">회원 탈퇴</button>
			</li>
		</ul>
		
		<!-- 버튼  -->
		<div class="button-group">
			<div class="box"><button id="edit">수정</button></div>
			<div class="box"><button id="back" type="reset" onclick="history.go(-1)">뒤로</button></div>
		</div>
		
		<!-- 비밀번호 체크 모달창  -->
		<div id="modal" class="modal-overlay">
	       <div class="modal-window">
	           <div class="title">
	               <h2>비밀번호 확인</h2>
	           </div>
	           <div class="close-area" id="close-area">X</div>
	           <div class="content">
	               <p>정보를 변경하려면 비밀번호를 입력하세요</p>
	               <input type="password" name="pwdCheck" id="pwdCheck" autocomplete="off" placeholder="비밀번호를 입력하세요">
	               <p class="modal-msg"></p>
	               <div class="box"><button type="submit" id="modal-ok">확인</button></div>
				   <div class="box"><button id="modal-back">취소</button></div>
	           </div>
	       </div>
	     </div>
		</form>
	</main>
	<script>
		$(function(){
			var url = location.href;
			var path = "${pageContext.request.contextPath}";
			console.log("${member.id}");
			  
			// 폼태그 내의 submit기능을 하는 요소들 제어
			$("form").submit(function(e){
				var curFocus = $(":focus").attr("id");
				
				// 비밀번호 변경버튼 submit시 제어
				buttonSequence(curFocus, "pwdEdit", e,
					function(){
						$("#pwdEdit").css("margin-bottom", "20px")
							.nextAll("input").attr("type", "password")
							.next().css("height","24px");
						$("#pwd").focus();
					}		
				);
				// 메일 변경버튼 submit시 제어
				buttonSequence(curFocus,"emailEdit", e,
					function(){
						$("#emailEdit").css("margin-bottom", "20px")
							.next("input").attr("type", "text")
							.next().css("height","24px");
						$("#email").focus();
					}
				);
				// 이름 변경버튼 submit시 제어
				buttonSequence(curFocus, "nameEdit", e,
					function(){
						$("#nameEdit").css("margin-bottom", "20px")
							.next("input").attr("type", "text")
							.next().css("height","24px");
						$("#name").focus();
					}		
				);
				
				//회원탈퇴
				buttonSequence(curFocus, "signOut", e,
					function(){
						$.ajax("signout?id=${member.id}",{
							success: function(result){
								alert("탈퇴 되었습니다.");
								location.href="${pageContext.request.contextPath}/";
							}
						})
					}
				);
				
				// input태그에서 엔터입력(submit)시 제어
				if(curFocus === "pwd"){
					enterSequence("#pwd", "#pwd2", "blur");
				}
				else if(curFocus === "pwd2"){
					enterSequence("#pwd2", "#edit", "blur");
				}
				else if(curFocus === "email"){
					enterSequence("#email", "#edit", "blur");
				}
				else if(curFocus === "name"){
					enterSequence("#name", "#edit", "blur");
				}

				//수정버튼 입력
				if(curFocus === "edit"){
					e.preventDefault();			
					editInfo();
				}

				// 모달창 비밀번호 확인부분
				if(curFocus === "modal-ok"){
					e.preventDefault();
					var input = $("#pwdCheck").val();
					$.ajax(path + "pwdCheck",{
						type: "post",						
						data: input,	
						contentType : "application/text; charset=utf-8",
						success : function(data){
							// 비밀번호 success 
							if(data/1){
								isEdit = true;
								prevEvent();
								$("#edit").parent().css("display", "inline-block");
								$("#modal").css("display", "none");
							}
							// fail
							else{
								isEdit = false;
								$("#pwdCheck").next().text("잘못된 비밀번호 입니다!").css("color","red");
							}
						}
					});
				}
				
				// 모달창 끄기 버튼 <button>
				if(curFocus === "modal-back"){
					e.preventDefault();
					$("#pwdCheck").val("");
					$("#pwdCheck").next().text("");
					$("#modal").css("display", "none");
				}
				// 모달창내 비밀번호 입력버튼
				else if(curFocus === "pwdCheck"){
					e.preventDefault();
					$("#modal-ok").focus();
				}
			});

			// 정보수정
			function editInfo(){
				var myEditInfo = {id : "", pwd : "", email : "", name : "" };

				if(pwdCheck)
					myEditInfo.pwd = $("#pwd").val();
				
				if(emailCheck)
					myEditInfo.email = $("#email").val();
				
				if(nameCheck)
					myEditInfo.name = $("#name").val();
				
				var member = JSON.stringify(myEditInfo);
				$.ajax(url, {
					method:"post",
					data : member,
					contentType : "application/json; charset=utf-8",
					success : function(result){
						alert(result);
						location.reload();
					}
				});
			}
			
			// 모달창 끄기버튼 <div>
			$("#close-area").click(function() {
				$("#pwdCheck").val("");
				$("#pwdCheck").next().text("");
				$("#modal").css("display", "none");
			});
			
			// 비밀번호 양식 확인
			pwdConfirm();
			
			// 이메일 양식 확인
			emailConfirm("info");
			
			// 이름 양식 확인
			nameConfirm();
		});
	</script>
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>