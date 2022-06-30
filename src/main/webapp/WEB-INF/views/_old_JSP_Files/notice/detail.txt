<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>    
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/head.jsp"/>
<title>MainStream! -공지사항- ${board.title}</title>
<link rel="stylesheet" href="../css/modal.css">
<link rel="stylesheet" href="../css/style.css">
<link rel="stylesheet" href="../css/board/detail.css">
<script src="../js/filter.js"></script>
</head>
<body>
<jsp:include page="../common/header.jsp"/>
	<main class="detail-main">
        <div class="top"></div>
        <div class="titleBox">
            <p class="title"><b><c:out value="${board.title}"/></b></p>
            <p class="date">${board.regDate}</p>
            <p class="id"><b>${board.id }</b></p>
        </div> 
        <div class="content"><c:out value="${board.content }"/></div>
       	<c:if test="${ not empty board.attachs }">
	        <div class="attachs">
	       		<p>첨부파일</p> 		
	       		<c:forEach items="${board.attachs }" var="attach">
					<p title="${attach.origin }">
						<a href="${pageContext.request.contextPath }/download?filename=${attach.path}/${attach.uuid}">${ attach.origin }</a>
					</p>
	       		</c:forEach>
	        </div>
       	</c:if>
	  	<div class="list"><button onclick="location.href='${pageContext.request.contextPath}/notice/list'">목록</button></div>
	    <div class="modify"><button>수정</button></div>
    	<div class="remove"><button>삭제</button></div>
	</main>
	<script>
		var cp = "${pageContext.request.contextPath}";
		var bno = "${param.bno}";
		
		$(function() {
			var originWriter = "${board.id}";
			if(originWriter !== "${member.id}"){
				$(".modify").css("display", "none");
				$(".remove").css("display", "none");
			}
			
			// 글수정 버튼 클릭시 작성자인지 확인
			$(".modify").find("button").click(function(e){
				originWriter = "${board.id}";
	
				if(originWriter !== "${member.id}"){
					alert("권한이 없습니다!");
				}
				else{
					location.href = cp + "/board/modify?bno=${board.bno}&category=0";
				}
			});
			
			// 삭제 버튼 클릭시 작성자인지 확인하고 댓글부터 삭제하고 글삭제
			$(".remove").find("button").click(function(e){
				originWriter = "${board.id}";
				
				if(originWriter !== "${member.id}"){
					alert("권한이 없습니다!");
				}
				else{
					if(confirm("정말 삭제 하시겠습니까?")){
						// 글삭제, 첨부파일삭제, 댓글삭제
						$.ajax(cp + "/board/remove?bno=${board.bno}",{
							success : function(data){
								alert("삭제완료");
								location.href = "list";
							}
						});
					}
				}
				
			});
			
		});
	</script>
<jsp:include page="../common/footer.jsp"/>
</body>
</html>