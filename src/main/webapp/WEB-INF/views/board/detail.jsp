<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/head.jsp"/>
<title>MainStream! -게시판- ${board.title}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources//css/modal.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources//css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources//css/board/detail.css">
<script src="${pageContext.request.contextPath }/resources/js/filter.js"></script>
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
        <div class="content"><c:out value="${board.content }"/>
        	<c:if test="${param.category == 2 }">
        	<c:if test="${not empty board.attachs}">
        		<c:forEach items="${board.attachs}" var="attach">
					<img src="${pageContext.request.contextPath}/display?filename=${attach.path}/${attach.uuid}">	
	       		</c:forEach>
        	</c:if>
        	</c:if>
        </div>
       	<c:if test="${ not empty board.attachs }">
	        <div class="attachs">
		       		<p>첨부파일</p> 		
		       		<c:forEach items="${board.attachs }" var="attach">
						<p title="${attach.origin }">
							<a href="${pageContext.request.contextPath }/FileDownload?filename=${attach.path}/${attach.uuid}">${ attach.origin }</a>
						</p>
		       		</c:forEach>
	        </div>
       	</c:if>
        
	  	<div class="list"><button onclick="location.href='/board/list?category=${param.category}&pageNum=${param.pageNum}'">목록</button></div>
	    <div class="modify"><button>수정</button></div>
    	<div class="remove"><button>삭제</button></div>
        
        <c:if test="${not empty member }">
	        <div class=reply-write>
				<div class="form-group">
	 				<form id="frmReplyWrite">
		 				<p>${member.name }</p>
						<button id="reply-btn">댓글등록</button>
						<input type="text" class="form-control" placeholder="댓글 제목을 입력하세요" name="title" id="reply-title" size="30" maxlength="30">
						<textarea class="form-control" placeholder="댓글을 입력하세요" name="content" id="reply-content"></textarea>
						<input type="hidden" name="bno" value="${board.bno }">
						<input type="hidden" name="id" value="${member.id }">
						<input type="hidden" name="uName" value="${member.name}">
	 				</form>
				</div>        
	        </div>
        </c:if>
        
        <div class="reply-wrapper">
        </div>
        <!-- 댓글 상세 모달창  -->
		<div id="modal" class="modal-overlay">
	       <div class="modal-window">
	           <div class="modal-reply-writer">
	               <p>id</p>
	           </div>
	           <div class="close-area" id="close-area">X</div>
	           <div class="content">
	              <textarea class="modal-title" rows="1" cols="" maxlength=30>title</textarea>
				  <textarea class="modal-content" rows="" cols="" maxlength=300>content</textarea>
	           </div>
	           <form id="frm-modify-ok">
		           <div class="box modal-modify-ok"><button class="modal-modify-ok-btn">수정완료</button></div>
	           </form>
		       <form id="frm-modify">
				   <div class="box modal-modify"><button id="modal-ok" class="modal-modify-btn">수정</button></div>
		       </form>
		       <form id="frm-delete">
				   <div class="box"><button id="modal-back" class="modal-delete-btn">삭제</button></div>
		       </form>
	       </div>
	     </div>
</main>
<script>
	var cp = "${pageContext.request.contextPath}";
	var bno = "${param.bno}";
	// 댓글을 새로 읽어올때 중복된 값이 들어가지 않게 set을 이용(삭제할때 사용)
	/* var rnoSet = new Set(); */
	
	$(function() {
		showList();
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
				location.href = "modify?bno=${board.bno}&category=${param.category}&pageNum=${param.pageNum}";
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
					// 댓글 먼저 삭제 // set 순환은 for ~ of로 가능
					// 서블릿에서 처리함!
					/* for(var rno of rnoSet ){
						var url = cp + "/reply?rno=" + rno;
						$.ajax(url, {
							method:"delete",
							error : function() {
								alert("댓글 삭제도중 에러발생");
							}
						});
					} */
					
					// 글삭제, 첨부파일삭제, 댓글삭제
					$.ajax("remove?bno=${board.bno}",{
						success : function(data){
							alert("삭제완료");
							location.href = "list?category=${param.category}&pageNum=${param.pageNum}";
						}
					});
				}
				else{
					//console.log(rnoSet);
				}
			}
		});
		
		// 모달창 닫는 이벤트
		$(".modal-overlay").click(function(e){
			if($(e.target).hasClass("modal-overlay") || $(e.target).hasClass("close-area")){
				closeModal();				
			}
		});
		
		// 모달창을 닫을때 처리해하는 것들 함수로정의
		function closeModal(){
			// 변경된 레이아웃 초기화 시킴
			$(".modal-modify-ok").css("display", "none");
			$(".modal-modify").css("display", "inline-block");
			
			// textarea 읽기전용속성 true
			$(".modal-title").attr("readonly", true);
			$(".modal-content").attr("readonly", true);
			
			// 아웃라인지우기
			$(".modal-content").css("outline", "none");
			$(".modal-title").css("outline", "none");
			
			// 모달창 종료
			$("#modal").css("display", "none");
		}

		// 댓글 리스트 출력
		function showList() {
			var url = cp + "/reply/list?bno=" + bno;
			/* console.log(url); */
	
			// ajax 프로세스 리스너 영역
			$.getJSON(url).done(function(data) {
				var str = "";
				console.log(data);
				for(var i in data){
					//data[i].title
					str += '<div class="card" data-rno="'+ data[i].rno +'">'
					str += '	<div class="card-header">'+'<p>' + XSSfilter(data[i].title) + '</p>'+ '<p>'+ data[i].name + '</p>' + '</div>'
			      	str += '	<div class="card-body">'+ XSSfilter(data[i].content) +'</div>'
				    str += '</div>'
			    	
					/* rnoSet.add(data[i].rno); */
				}
				$(".reply-wrapper").html(str);
			});
		}
		/* // 비동기 통신의 문제점
		
		// 이벤트 위임을 통해 해결한다. 상위 노드에서 하위요소로 이벤트를 위임 */
		// 이벤트 위임 댓글 상세 이벤트
		// 모달창 오픈 이벤트
		$(".reply-wrapper").on("click",".card",function(){
			var url = cp + "/reply/get?rno=" + $(this).data("rno");
			$.getJSON(url).done(function(data) {
				// jquery  메서드 체이닝
				$("#modal")
					.data("rno", data.rno).data("id", data.id)
					.data("bno", data.bno).data("title", data.title)
					.data("content", data.content).data("regDate", data.regDate)
					.data("name", data.name)
					.find("p").text("작성자 : " + data.name)
					.end().find(".modal-title").val(data.title)
					.end().find(".modal-content").val(data.content)
					.end().css("display", "flex");
				
				$(".modal-title").attr("readonly", true);
				$(".modal-content").attr("readonly", true);
				//$("#myModal").modal("show");
			});
		});

		// 댓글 제목과 내용 미작성시 등록버튼 비활성화
		$("#reply-title, #reply-content").keyup(function(){
			var titleLen = $("#reply-title").val().trim().length;
			var contentLen = $("#reply-content").val().trim().length;

			if(titleLen && contentLen){
				$("#reply-btn").css("display", "block");
			}
			else{
				$("#reply-btn").css("display", "none");
			}
		});

		// 댓글 작성
		$("#frmReplyWrite").submit(function(e){
			e.preventDefault();
			/* console.log($(this).serialize());
			console.log($(this).serializeArray());
			console.log(JSON.stringify($(this).serializeArray())); */
			/* if($(this).find("#reply-btn").is(".disabled")) return; */
			
			var reply = {};
			reply.title = $(this.title).val();
			reply.content = $(this.content).val();
			reply.id = $(this.id).val();
			reply.bno = $(this.bno).val();
			reply.name = $(this.uName).val();
			
			var data = JSON.stringify(reply);

			var frm = this;
			var url = cp + "/reply/write";
			$.ajax(url, {
				type:"post",
				data : data,
				contentType : "application/json; charset=utf-8",
				success : function(){
					showList();
					frm.reset();
					/* $("#reply-btn").addClass("disabled"); */
				},
				error : function(request, status, error) {
					alert("code:"+request.status+"\n\n"+"message:"+request.responseText+"\n\n"+"error:"+error);
				}
			});
		});
		
		// 댓글 수정
		
		// 수정버튼 클릭시 
		$("#frm-modify").submit(function(e){
			e.preventDefault();
			
//			var originReplyWriter = $(".modal-reply-writer").find("p").text();
			var originReplyWriter = $("#modal").data("id");

//			if(originReplyWriter !== "작성자 : ${member.name}"){
			if(originReplyWriter !== "${member.id}"){
				alert("권한이 없습니다!");
				return;
			}
			// 댓글작성자와 회원이 같을경우
			else{
				// 버튼 체인지 수정버튼 -> 수정완료버튼
				$(".modal-modify").css("display", "none");
				$(".modal-modify-ok").css("display", "inline-block");
				
				// textarea 읽기전용속성 해제(쓰기가능)
				$(".modal-title").attr("readonly", false);
				$(".modal-content").attr("readonly", false);
				$(".modal-title").css("outline", "1px solid black");
				$(".modal-content").css("outline", "1px solid black");
				
				//댓글 내용창으로 커서 포커싱
				$(".modal-content").focus();
			}
		});
		
		// 수정완료 버튼 클릭
		$("#frm-modify-ok").submit(function(){
			event.preventDefault();
			
			var reply = {};
			// 모달데이터객체를 reply로 이전
			reply = $("#modal").data();
			reply.name = "${member.name}";
			console.log(reply.name);
			reply.title = $(".modal-title").val();
			reply.content = $(".modal-content").val();
			console.log(reply);
			
			var data = JSON.stringify(reply);
			var url = cp + "/reply/modify";
			$.ajax(url, {
				method:"post",
				data : data,
				contentType : "application/json; charset=utf-8",
				success : function(){
					closeModal();
					showList();
				}
			});
		});
		
		// 댓글 삭제버튼 눌렀을시 DB상에서도 삭제
		$("#frm-delete").submit(function() {
			event.preventDefault();
			
			var originWriter = $(".modal-reply-writer").find("p").text();
			if(originWriter !== "작성자 : ${member.name}"){
				alert("권한이 없습니다!");
				return;
			}
			
			var rno = $(this).closest("#modal").data("rno");
			var url = cp + "/reply?rno=" + rno;

			// ajax
			$.ajax(url, {
				method:"delete",
				success : function(data) {
					// 성공적으로 종료
					// show리스트 호출
					showList();
					/* $("#myModal").modal("hide"); */
					$("#modal").css("display", "none");
				}
			});
		});
	
	});
</script>
<jsp:include page="../common/footer.jsp"/>
</body>
</html>