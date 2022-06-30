package net.ifonlygaram.service;

import java.util.List;

import net.ifonlygaram.domain.Reply;

public interface ReplyService {
	// 댓글 쓰기
	void write(Reply reply);
	
	// 댓글 목록
	List<Reply> list(Long bno);
	
	// 댓글 단일 조회
	Reply get(Long rno);
	
	// 댓글 삭제
	void remove(Long rno);
	
	// bno로 댓글 삭제
	void removeBy(Long bno);

	// 댓글 수정
	void modiify(Reply reply);

}
