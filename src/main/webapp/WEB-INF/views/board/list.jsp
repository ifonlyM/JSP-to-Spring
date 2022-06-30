<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
	<title>MainStream! 자유게시판</title>
	<jsp:include page="../common/head.jsp"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources//css/style.css">	
</head>
<body>
	<jsp:include page="../common/header.jsp"/>
	<!-- 공지사항 메인 -->
    <main class="notice-main">
        <div class="top-space"></div>
        <table class="notice-Table">
        	<c:choose>
        		<c:when test="${page.cri.category == 0}">
	            <caption><strong>Main Stream 공지사항</strong></caption>
        		</c:when>
				<c:when test="${page.cri.category == 1 }">
	            <caption><strong>Main Stream 자유게시판</strong></caption>
				</c:when>        		
        	</c:choose>
            <thead>
                <tr>
                    <th class="col-no">연번</th>
                    <th class="col-title">제목</th>
                    <th class="id">작성자</th>
                    <th class="date">작성일</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${list}" var="b">
	               	<tr>
	               		
	               		<td class="no">${b.bno}</td>
	               		<td class="title"><p><a href="detail?bno=${b.bno}&category=${page.cri.category}&pageNum=${page.cri.pageNum}"><c:out value="${b.title}"/><span class="text-muted"> [${b.replyCnt}]</span></a></p></td>
	               		<td>${b.id}</td>
	               		<td><fmt:formatDate value="${b.regDate}" pattern="yy/MM/dd"/> </td>
	               	</tr>
                </c:forEach>
            </tbody>
        </table>
        <div>
        </div>
       	<!-- 로그인 한 상태에서만 글쓰기 버튼 활성화 -->
        <c:choose>
        	<c:when test="${not empty member }">
        	<div class='notice-writebox'><a class='linkButton' href='/board/write?category=${page.cri.category}'>글쓰기</a></div>
        	</c:when>
        	<c:otherwise>
        	<div class='notice-writebox'></div>
        	</c:otherwise>
        </c:choose>
        
        <div class="notice-index">
            <div class="box">
	        	<c:if test="${page.prev == true }">
	                <a class="linkButton" href="list?pageNum=${page.startPage-1 }&amount=${page.cri.amount}&category=${page.cri.category}">이전</a>
	        	</c:if>
            </div>
        	<c:forEach begin="${page.startPage }" end="${page.endPage }" var="i"> <!-- step생략하면 1씩 증가  -->
            <div class="box">
	            <a class="linkButton${i == page.cri.pageNum ? '-active' : '' }" href="list?pageNum=${i}&amount=${page.cri.amount}&category=${page.cri.category}">${i}</a>
            </div>
			</c:forEach>
            <div class="box">
	            <c:if test="${page.next == true }">
	                <a class="linkButton" href="list?pageNum=${page.endPage+1 }&amount=${page.cri.amount}&category=${page.cri.category}">다음</a>
	            </c:if>
            </div>
        </div>
    </main>
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>