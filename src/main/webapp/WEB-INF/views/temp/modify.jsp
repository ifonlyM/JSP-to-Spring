<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/head.jsp"/>
<title>MainStream!-글작성</title>
<link rel="stylesheet" href="../css/style.css">
<link rel="stylesheet" href="../css/board/modify.css">
</head>
<body>
	<jsp:include page="../common/header.jsp"/>
	<main class="modify-main">
		<form method="post" enctype="multipart/form-data">
			<div class="top"></div>
	        <div class="title"><input type="text" name="title" maxlength="30" ></div>
	        <div class="content"><textarea cols="90" rows="20" name="content" ></textarea></div>
	        
	        <c:forEach begin="0" end="2" var="i" varStatus="stat">
	        	<c:choose>
	        		<c:when test="${ not empty fileNameList[i]}">
		        		<div class="fileAdd">
			        		<label for="file${stat.count}" title="변경하려면 클릭!" >
			        			${fileNameList[i]}
			        		</label>
			        		<input type='file' name="file${stat.count}" id="file${stat.count}" style="display: none;">
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
		$(function(){
			$(".title").find("input").val("${board.title}");
			$(".content").find("textarea").val("${board.content}");
		});
		
	</script>
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>