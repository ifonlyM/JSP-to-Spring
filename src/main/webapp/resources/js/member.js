/**
 * 회원가입, 회원정보수정등 주요 스크립트를 함수로 정의
 */

var idCheck = false;
var pwdCheck = false;
var pwd2Check = false;
var emailCheck = false;
var nameCheck = false;
var isEdit = false;
var prevEvent;

function enterSequence(curID, nextID, eventName){
	if($(curID).trigger(eventName)){
		$(nextID).focus();
		event.preventDefault();
		return true;
	}
	else{
		event.preventDefault();
		return false;
	}
}

function buttonSequence(targetID, curID, event, activeEvent){
	if(targetID === curID){
		if(isEdit){
			activeEvent();
		}
		else{
			prevEvent = activeEvent;
			$("#modal").css("display", "flex");
			$("#pwdCheck").focus();
		}
		event.preventDefault();
	}
}

function pwdConfirm(){
	// 비밀번호 양식 확인 ==================================================================================================================
	$("#pwd").blur(function(){
		var pwd = $(this).val();
		var msgObj = $(this).next(); 
		
		if(pwd){
			if(/(\w)\1\1/.test(pwd)){
				msgObj.text("같은 문자를 3번이상 반복할수 없습니다");
				msgObj.css("color", "red");
				pwdCheck = false;
				return false;
			}
			else if(pwd.length < 12){
				msgObj.text("비밀번호의 길이는 12자 이상이여야 합니다");
				msgObj.css("color", "red");
				pwdCheck = false;
				return false;
			}
			else if(pwd.search(/\s/) != -1){
				msgObj.text("비밀번호내에 공백은 없어야합니다");
				msgObj.css("color", "red");
				pwdCheck = false;
				return false;
			}
			else{
				msgObj.text("사용가능한 비밀번호입니다.");
				msgObj.css("color", "gray");
				pwdCheck = true;
				return true;
			}
		}
		else{
			msgObj.text("");
			pwdCheck = false;
			return false;
		}
	});
	
	//비밀번호 재 확인
	$("#pwd2").blur(function(){
		var msgObj = $(this).next();
		var pwd1 = $("#pwd").val();
		var pwd2 = $(this).val();
		if(pwd2){
			if(!pwdCheck){
				msgObj.text("올바른 양식의 비밀번호를 먼저 입력해주세요");
				msgObj.css("color", "red");
				$(this).val("");
				pwd2Check = false;
				return false;
			}
			else{
				if(pwd1 === pwd2){
					msgObj.text("동일한 비밀번호입니다");
					msgObj.css("color", "gray");
					pwd2Check = true;
					return true;
				}
				else{
					msgObj.text("다른비밀번호 입니다 다시확인해주세요");
					msgObj.css("color", "red");
					pwd2Check = false;
					return false;
				}
			}
		}
		else{
			msgObj.text("");
			pwd2Check = false;
			return false;
		}
	});
}
function emailConfirm(useCase){
	//이메일 중복체크 =========================================================================================================================
	$("#email").blur(function(){
		var msgObj = $(this).next();
		var email = $("#email").val();
		var regExp = /^[a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		// ^[a-zA-Z] : ^x_문자열의 시작 x문자로 시작됨, [xy]_문자선택을 표현 x와y중 하나를 의미 
		
		// ([-_\.]?[0-9a-zA-Z])* : (x)_그룹을 표현하며 x를 그룹으로 처리함을 의미
		//						   x?_존재여부를 표현하며 x문자가 존재할 수도, 존재하지 않을 수도 있음을 의미
		//						   x*_반복여부를 표현하며 x문자가 0번 또는 그 이상 반복됨을 의미
		
		//	[a-zA-Z]{2,3}$/i : 	x{n,m}_반복을 표현하며 x문자가 최소 n번 이상 m번 이하로 반복됨을 의미
		if(email){
			$.ajax("emailValid?email="+ email, {
				success : function(data) {

					if(!email.match(regExp)){
						msgObj.text("사용불가능한 이메일 형식입니다 .(dot)뒤에는 2~3자리 영문만 올수있습니다. ");
						msgObj.css("color", "red");
						/*$(this).select();*/
						emailCheck = false;
						return false;
					}
					// 중복없음
					else if(data/1) { 
						msgObj.text("사용가능한 이메일입니다");
						msgObj.css("color", "gray");
						emailCheck = true;
						return true;
					}
					// 중복있음
					else if(!(data/1)) { 
						msgObj.text("이미 가입된 이메일입니다 : " + email);
						msgObj.css("color", "red");
						/*$(this).select();*/
						emailCheck = false;
						return false;
					}
				}
			});
		}
		else{
			if(useCase === "join")
				msgObj.text("메일주소를 입력하지 않았어요! (╬▔皿▔)╯");
			else if(useCase === "info")
				msgObj.text("");
			
			emailCheck = false;
			return false;
		}
		event.preventDefault();
	});
}
function nameConfirm(){
	// 이름 입력되었는지 확인 =================================================================================================================
	$("#name").blur(function(){
		
		if(/[a-zA-Z가-힣]{2,10}/.test($("#name").val())){
			$("#name").next().text("좋은 이름이네요!");
			$("#name").next().css("color","#0D0");
			nameCheck = true;
			return true;
		}
		else{
			$("#name").next().text("이름은 2~10글자 영문과 한국어로 입력해주세요.");
			$("#name").next().css("color","red");
			nameCheck = false;
			return false;
		}
	});
}




