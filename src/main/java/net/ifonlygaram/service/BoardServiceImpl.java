package net.ifonlygaram.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import net.ifonlygaram.dao.BoardDao;
import net.ifonlygaram.domain.Attach;
import net.ifonlygaram.domain.Board;
import net.ifonlygaram.domain.Criteria;

@Service("BoardService")
public class BoardServiceImpl implements BoardService {
	@Setter @Autowired
	BoardDao dao;
	@Override @Transactional
	// 트랜잭션으로 동작해야함.
	public Long write(Board board) {
		// 글작성 > 후 글 번호 반환
		Long bno = dao.insert(board);
		
		// 각 첨부파일에 글번호 부여
		for(Attach attach: board.getAttachs()) {
			attach.setBno(bno);
			// 첨부 파일 작성
			dao.writeAttach(attach);
		}
		return bno;
	}

	@Override
	public Board read(Long bno) {
		Board board = dao.read(bno);
		board.setAttachs(dao.readAttach(bno));
		return board;
	}

	@Override
	public List<Board> list() {
		return null;
	}
	
	@Override
	public List<Board> list(Criteria cri) {
		// TODO Auto-generated method stub
		List<Board> list = dao.list(cri);
		list.forEach(b -> b.setAttachs(dao.readAttach(b.getBno())));
		return list;
	}

	@Override
	public void modify(Board board) {
		dao.update(board);
		
		// 만약 첨부파일 리스트가 비어있다면 첨부파일 작성은 하지않고 끝낸다.
		if(board.getAttachs().isEmpty()) return;
		
		// 각 첨부파일에 글번호 부여
		for(Attach attach: board.getAttachs()) {
			attach.setBno(board.getBno());
			// 첨부 파일 작성
			dao.writeAttach(attach);
		}
	}

	@Override
	public void remove(Long bno) {
		// 업로드 폴더에있는 파일도 지워야 하나?
		dao.deleteAttach(bno);
		dao.delete(bno);
	}

	@Override
	public String findOriginBy(String uuid) {
		// TODO Auto-generated method stub
		return dao.findOriginBy(uuid);
	}

	@Override
	public int getCount(Criteria cri) {
		// TODO Auto-generated method stub
		return dao.getCount(cri);
	}

	@Override
	public List<Attach> readAttachByPath(String path) {
		// TODO Auto-generated method stub
		return dao.readAttachByPath(path);
	}

	@Override
	public void removeAttachBy(String uuid) {
		// TODO Auto-generated method stub
		dao.deleteAttachBy(uuid);
	}

}
