<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/head.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css">	
<title>MainStream! 갤러리</title>
</head>
<body>
	<jsp:include page="../common/header.jsp"/>
	<!-- 갤러리 메인 -->
    <main class="gallery-main">
      <section class="content">
      	<h2><a href="list">♪갤러리♬</a></h2>
      	<c:set var="endCount" value="${page.cri.amount - (page.cri.amount-1) % 3 + 2}"/>
      	<c:forEach begin="1" end="${page.cri.amount}" varStatus="stat" var="board">
    		<c:set var="board" value="${list[stat.index-1]}"></c:set>
      		<c:if test="${stat.index % 3 == 1 }">
      		<div class="row">
      		</c:if>
				<div class="col">
		      		<c:if test="${not empty board}">
	   				<a href="detail?bno=${board.bno}&category=${page.cri.category}&pageNum=${page.cri.pageNum}">
	  					<div class="gallList-img-box">
	  						<c:choose>
								<c:when test="${board.attachs[0].fileSize > 200000 }">
		           					<img src="${pageContext.request.contextPath}/display?filename=${board.attachs[0].path}/s_${board.attachs[0].uuid}">
								</c:when>
								<c:otherwise>
		           					<img src="${pageContext.request.contextPath}/display?filename=${board.attachs[0].path}/${board.attachs[0].uuid}">
								</c:otherwise>
	       					</c:choose>
	  					</div>
	   					<div>${board.title}</div>
	 				</a>
		     		</c:if>	
  				</div>
      		<c:if test="${stat.index % 3 == 0 }">
      		</div>
      		</c:if>
      	</c:forEach>
       </section>
        <%	
        	// 로그인 한 상태에서만 글쓰기 버튼 활성화
        	if(session.getAttribute("member") != null){
        		out.print("<div class='notice-writebox'><a class='linkButton' href='/board/write?category=2'>글쓰기</a></div>");
        	}
        	else {
        		out.print("<div class='notice-writebox'></div>");
        	}
        %>
        <div class="notice-index">
	            <div class="box">
		        	<c:if test="${page.prev == true }">
		                <a class="linkButton" href="list?pageNum=${page.startPage-1 }&amount=${page.cri.amount}">이전</a>
		        	</c:if>
	            </div>
        	<c:forEach begin="${page.startPage }" end="${page.endPage }" var="i"> <!-- step생략하면 1씩 증가  -->
	            <div class="box">
		            <a class="linkButton${i == page.cri.pageNum ? '-active' : '' }" href="list?pageNum=${i}&amount=${page.cri.amount}">${i}</a>
	            </div>
			</c:forEach>
            <div class="box">
	            <c:if test="${page.next == true }">
	                <a class="linkButton" href="list?pageNum=${page.endPage+1 }&amount=${page.cri.amount}">다음</a>
	            </c:if>
            </div>
        </div>
    </main>
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>