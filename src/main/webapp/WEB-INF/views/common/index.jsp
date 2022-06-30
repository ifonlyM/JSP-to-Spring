<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>모든취향의 음악커뮤니티 MainStream!</title>
	<jsp:include page="head.jsp"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css">		
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<main>
        <!-- 컨텐츠 -->
        <article>
            <section class="main-content">
                <div class="main-png">
            		<!-- <div class="slider"> -->
                	<img src="${pageContext.request.contextPath }/resources/images/오아시스형제_수정본.png" alt="갤러거 형제">
                	<!-- </div> -->
                	<div class="main-text">
	                    <h3>오아시스 재결합 소식</h3>
	                    <p>노앨 갤러거, 리암 갤러거 형제가 드디어
	                        재결합 한다고 합니다.<br> 오아시스를
	                        해체한지 약 10년 만인데요. 그들은 다시 과거
	                        의 좋았던 시절로 돌아 가는 것일까요?</p>
                    </div>
                </div>
            </section>
            <section class="middle-content">
                <h2>세계의 다양한 아티스트 소개</h2>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/fka-twigs.jpg" alt="fka-twigs">
                    <figcaption>
                        <h3>FKA-Twigs</h3>
                        <p>한번 들어보면 당혹스러울 정도로 이상하게 느껴지죠.</p>
                    </figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/bts.jpg" alt="bts">
                    <figcaption>
                        <h3>BTS</h3>
                        <p>BTS진짜 대단해 아직도 너무 신기하고 개쩐다.</p>
                    </figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/r3hab.jpg" alt="r3hab">
                    <figcaption>
                        <h3>R3HAB</h3>
                        <p>EDM 아티스트중 나의 세손가락 안에 꼽히는 그분 개좋다. </p>
                    </figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/billie-eilish.jpg" alt="billie-eilish">
                    <figcaption>
                        <h3>Billie-Eilish</h3>
                        <p>뭔가 이상한 매력의 소유자, 노래들이 신선해서 좋다. </p>
                    </figcaption>
                </figure>
            </section>
            <section class="gallery-content">
            	<h2>갤러리</h2>
            	<div class="gall-img-box">
	           		<c:forEach begin="0" end="2" var="i">
	           			<c:if test="${not empty gallList[i]}">
		           			<a href="board/detail?bno=${gallList[i].bno}&category=${gallListCri.category}&pageNum=${gallListCri.pageNum}">
			           			<figure>
			           				<div class="gall-img-wrapper">
			           					<c:choose>
											<c:when test="${gallList[i].attachs[0].fileSize > 200000 }">
					           					<img src="${pageContext.request.contextPath}/display?filename=${gallList[i].attachs[0].path}/s_${gallList[i].attachs[0].uuid}">
											</c:when>
											<c:otherwise>
					           					<img src="${pageContext.request.contextPath}/display?filename=${gallList[i].attachs[0].path}/${gallList[i].attachs[0].uuid}">
											</c:otherwise>
			           					</c:choose>
			           				</div>
			           				<figcaption>
			           					<p>${gallList[i].title}</p>
			           				</figcaption>
			           			</figure>
		           			</a>
	           			</c:if>
	           		</c:forEach>
            	</div>
            </section>
            <section class="notice-content">
            	<h3>자유게시판</h3>
            	<table>
            		<c:forEach begin="0" end="7" var="i" step="2">
	           			<tr>
	            			<td>
	            				<a href="board/detail?bno=${list[i].bno}&category=${listCri.category}&pageNum=${listCri.pageNum}">
		            				<div><p><c:out value="${list[i].title}" escapeXml="true"/></p><span>${list[i].id}</span></div>
		            				<p><c:out value="${list[i].content}" escapeXml="true"/></p>
	            				</a>
	            			</td>
	            			<td>
	            				<a href="board/detail?bno=${list[i+1].bno}&category=${listCri.category}&pageNum=${listCri.pageNum}">
		            				<div><p><c:out value="${list[i+1].title}" escapeXml="true"/></p><span>${list[i+1].id}</span></div>
		            				<p><c:out value="${list[i+1].content}" escapeXml="true"/></p>
	            				</a>
	            			</td>
	            		</tr>
            		</c:forEach>
            	</table>
            </section>
            <section class="bottom-content">
                <h2>오늘의 추천 앨범</h2>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/JasmineSokko-Nº.jpg" alt="jasminesokko">
                    <figcaption>JasmineSokko-Nº</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/OfMonstersAndMen-MyHeadIsAnAnimal.jpg" alt="ofmonstersandmen">
                    <figcaption>OfMonstersAndMen-MyHeadIsAnAnimal</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/검정치마-Everything.jpg" alt="검정치마">
                    <figcaption>검정치마-Everything</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/r3hab-Trouble.jpg" alt="r3hab">
                    <figcaption>r3hab-Trouble</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/새소년-여름깃.jpg" alt="새소년">
                    <figcaption>새소년-여름깃</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/오원더-Ultralife.jpg" alt="오원더">
                    <figcaption>오원더-Ultralife</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/위아더나잇-들뜬마음가라앉히고.jpg" alt="위아더나잇">
                    <figcaption>위아더나잇-들뜬마음가라앉히고</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/크러시-나빠.jpg" alt="크러시">
                    <figcaption>크러시-나빠</figcaption>
                </figure>
                <figure>
                    <img src="${pageContext.request.contextPath }/resources/images/시쏘-AirBalloon.jpg" alt="시쏘">
                    <figcaption>시쏘-AirBalloon</figcaption>
                </figure>
            </section>
        </article>

        <!-- 사이드 바 -->
        <aside>
        <c:choose>
        	<c:when test="${empty member}">
        		<div class="loginbox"><a class="linkButton" href="login">로그인</a></div>
            	<a href="contract">회원가입</a> <a href="findinfo">ID/PW 찾기</a>
        	</c:when>
        	<c:otherwise>
        		<div class="loginbox-after">
	        	<p>${member.name}님 환영합니다!</p>
	        	<p><a href = "info">내 정보</a> <a href="logout">로그아웃</a></p>
        		</div>
        	</c:otherwise>
        </c:choose>
        </aside>
    </main>
    <script>
    	$(function(){
    		/* $(".slider").bxSlider({
    			mode: 'fade',
    			pager:false,
    			controls:false,
    			auto:true
    		}); */
    	})
    </script>
	<jsp:include page="footer.jsp"/>
</body>
</html>