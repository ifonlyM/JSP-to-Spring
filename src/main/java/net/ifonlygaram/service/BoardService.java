package net.ifonlygaram.service;

import java.util.List;

import net.ifonlygaram.domain.Attach;
import net.ifonlygaram.domain.Board;
import net.ifonlygaram.domain.Criteria;

public interface BoardService {
	// 글쓰기
	Long write(Board board);
	// 글조회
	Board read(Long bno);
	// 목록조회
	List<Board> list();
	List<Board> list(Criteria cri);
	
	// 글수정
	void modify(Board board);
	// 글삭제
	void remove(Long bno);
	
	/**
	 * 첨부파일 uuid 
	 * @param uuid
	 * @return origin
	 */
	String findOriginBy(String uuid);
	
	void removeAttachBy(String uuid);
	
	// 글최대 개수 가져오기
	int getCount(Criteria cri);
	
	List<Attach> readAttachByPath(String path);
}
