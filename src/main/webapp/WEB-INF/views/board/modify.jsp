<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/head.jsp"/>
<title>MainStream!-글수정</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/board/modify.css">
</head>
<body>
	<jsp:include page="../common/header.jsp"/>
	<main class="modify-main">
		<form method="post" enctype="multipart/form-data">
			<div class="top"></div>
	        <div class="title"><input type="text" name="title" maxlength="30" ></div>
	        <div class="content"><textarea cols="90" rows="20" name="content" ></textarea></div>
	        <input type="hidden" name="category" value="${param.category}">
	        <input type="hidden" name="pageNum" value="${param.pageNum}">
	        <c:forEach begin="0" end="2" var="i" varStatus="stat">
	        	<c:choose>
	        		<c:when test="${ not empty fileList[i]}">
		        		<div class="fileAdd" style="padding: 0;">
			        		<label for="file${stat.count}" title="클릭하여 변경!" >
			        			${fileList[i].origin}
			        		</label>
			        		<input type='file' name="file${stat.count}" id="file${stat.count}" style="display: none;" data-uuid="${fileList[i].uuid }" data-origin="${fileList[i].origin }">
			        		<button type="button" class="removeBtn" style="width: 23px; height: 20px;">x</button>
				        </div>
	        		</c:when>
	        		<c:otherwise>
	        			<div class="fileAdd" style="padding: 0;">
			        		<input type='file' name="file${stat.count}" id="file${stat.count}" style="display: inline-block;">
				        </div>
	        		</c:otherwise>
	        	</c:choose>
	        </c:forEach>
	        
	        <div class="pass"><button type="submit">수정</button></div>
	        <div class="cancle"><button type="reset" onclick="history.go(-1)">취소</button></div> <!-- 목록페이지로 이동할것  -->
	        <input type="hidden" name="bno" value="${board.bno}">
	        <input type="hidden" name="id" value="${board.id}">
		</form>
	</main>
	<script>
		// 첨부파일 변경시 기존자리의 첨부파일삭제 리스트
		var atcDeleteSet = new Set();
		
		$(function(){
			$(".title").find("input").val("${board.title}");
			$(".content").find("textarea").val("${board.content}");
			
			$(".fileAdd").each(function(idx, obj){
				
				//-- 기존의 첨부파일을 삭제할때
				$(".fileAdd").eq(idx).find("button").click(function(){
					var input = $(this).parent().find("input");
					
					$(this).css({"display" : "none"}); // 버튼 지우기
					$(this).parent().find("label").css({"display" : "none"}); // 라벨 지우기
					$(this).parent().find("label").text("");
					input.css({"display" : "inline-block"}); // 인풋버튼 그리기
					
					// 중복으로 값이 들어가는 경우가 있어서 자료구조를 set으로 변경(중복된값은 들어가지 않음)
					atcDeleteSet.add(input.data("uuid"));
				});
				
				//-- 첨부파일란에 변경이 일어났을때
				$(".fileAdd").eq(idx).find("input").change(function(){
					
					// 첨부파일이 이미 있는데 변경하는 경우
					// 라벨로 보여지던 부분을 가리고 변경할 첨부파일을 보여준다
					if($(this).val() && $(this).data("uuid")){
						$(this).parent().find("label").css({"display" : "none"});
						$(this).parent().find("label").text("");
						$(this).css({"display" : "inline-block"});
						
						// 기존의 첨부파일은 삭제를 위해 자료구조에 uuid를 저장
						atcDeleteSet.add($(this).data("uuid"));
					}
					
					// 첨부파일 수정을 취소하고 그냥 원래대로 두고싶을때 다시 원상복귀한다.
					if(!$(this).val() && $(this).data("uuid")){
						$(this).parent().find("label").css({"display" : "inline-block"});
						$(this).parent().find("label").text($(this).data("origin"));
						$(this).parent().find("button").css({"display" : "inline-block"});
						$(this).css({"display" : "none"});
						
						// 기존의 첨부파일의 삭제를 취소해야 하므로 리스트에 uuid를 제거
						atcDeleteSet.delete($(this).data("uuid"));
						
						// 배열방법
						/* var i = atcDeleteList.indexOf($(this).data("uuid"));
						if(i > -1) atcDeleteList.splice(i, 1); */
					}
				});
			});
			
			$("form").submit(function(e){
				e.preventDefault();
				
				var delAttachs = Array.from(atcDeleteSet);
				if(delAttachs.length > 0) {
					var data = JSON.stringify(delAttachs);
					$.ajax("${pageContext.request.contextPath}/board/deleteAttachs", {
						method: "POST",
						data : data,
						contentType : "application/json; charset=utf-8",
						success : function(){
							alert("success");
						},
						error : function(request,status,error){
							 alert("code = "+ request.status + " message = " + request.responseText + " error = " + error); // 실패 시 처리
						}
					});
				}
				
				this.submit();
			});
		});
		
	</script>
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>