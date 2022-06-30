package net.ifonlygaram.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import net.ifonlygaram.domain.Attach;
import net.ifonlygaram.domain.Board;
import net.ifonlygaram.domain.Criteria;

@Service
public class BoardDao {
	@Setter @Autowired
	private DataSource dataSource;
	private Connection conn;
	// CRUD
	// 글쓰기
	// 글조회
	// 목록조회
	// 글수정
	// 글삭제
	public Long insert(Board board) {
		PreparedStatement pstmt = null;
		Long bno = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			
			// 글 번호를 먼저 발급
			ResultSet rs = conn.prepareStatement("SELECT SEQ_BOARD.NEXTVAL FROM DUAL").executeQuery();
			rs.next();
			bno = rs.getLong(1);
			
			// 글작성
			pstmt = conn.prepareStatement("INSERT INTO TBL_BOARD(BNO, TITLE, CONTENT, ID, CATEGORY) VALUES ( ?, ?, ?, ?, ?)");
			int idx = 1;
			pstmt.setLong(idx++, bno);
			pstmt.setString(idx++, board.getTitle());
			pstmt.setString(idx++, board.getContent());
			pstmt.setString(idx++, board.getId());
			pstmt.setLong(idx++, board.getCategory());
			
			pstmt.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
			pstmt.close();
			conn.close();
			// select : executeQuery
			// insert, update, delete : executeUpdate
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bno;
	}

	public Board read(Long bno) {
		PreparedStatement pstmt = null;
		Board board = null;
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement("SELECT BNO, TITLE, CONTENT, REGDATE, ID, CATEGORY FROM TBL_BOARD WHERE BNO=?");
			pstmt.setLong(1, bno);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int idx = 1;
				board = new Board(
						rs.getLong(idx++),
						rs.getString(idx++),
						rs.getString(idx++),
						rs.getDate(idx++),
						rs.getString(idx++),
						rs.getLong(idx++)
						);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return board;
	}

	public List<Board> list() {
		List<Board> list = new ArrayList<Board>();
		try {
			conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"SELECT BNO, TITLE, REGDATE, ID, CATEGORY FROM TBL_BOARD WHERE BNO > 0 ORDER BY 1 DESC");
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				Long bno = rs.getLong(idx++);
				String title = rs.getString(idx++);
				Date regDate = rs.getDate(idx++);
				String id = rs.getString(idx++);
				Long category = rs.getLong(idx++);
				
				Board board = new Board(bno, title, null ,regDate, id, category);
				list.add(board);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Board> list(Criteria cri ) {
		List<Board> list = new ArrayList<Board>();
		System.out.println("DAO criteria : " + cri.toString());
		try {
			conn = dataSource.getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * "); 
			sql.append("FROM "); 
			sql.append("( "); 
			sql.append("SELECT A.*, ROWNUM AS RN "); 
			sql.append("FROM "); 
			sql.append("( "); 
			sql.append("SELECT TB.*, (SELECT COUNT(*) FROM TBL_REPLY R WHERE R.BNO = TB.BNO) REPLYCNT "); 
			sql.append("FROM TBL_BOARD TB "); 
			sql.append("WHERE 1 = 1 "); 
			sql.append("AND CATEGORY = " + cri.getCategory() + " "); 
			sql.append("ORDER BY BNO DESC "); 
			sql.append(") A "); 
			sql.append("WHERE ROWNUM <= ( " + cri.getPageNum() * cri.getAmount() +" ) "); 
			sql.append(") "); 
			sql.append("WHERE RN > " + (( cri.getPageNum() - 1) * cri.getAmount())); 
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			int idx = 1;
//			pstmt.setInt(idx++, cri.getCategory());
//			pstmt.setInt(idx++, cri.getPageNum());
//			pstmt.setInt(idx++, cri.getAmount());
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				idx = 1;
				Long bno = rs.getLong(idx++);
				String title = rs.getString(idx++);
				String content = rs.getString(idx++);
				Date regDate = rs.getDate(idx++);
				String id = rs.getString(idx++);
				Long category = rs.getLong(idx++);
				
				Board board = new Board(bno, title, "" ,regDate, id, category);
//				Board board = new Board(bno, title, "" ,regDate, id, 1l);
				board.setReplyCnt(rs.getInt(idx++));
				board.setContent(content);
				list.add(board);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void update(Board board) {
		// 글 작성
		try {
			conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("UPDATE TBL_BOARD SET TITLE=?, CONTENT=? WHERE BNO=?");
			int idx = 1;
			pstmt.setString(idx++, board.getTitle());
			pstmt.setString(idx++, board.getContent());
			pstmt.setLong(idx++, board.getBno());
			
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Long bno) {
		
		try {
			conn = dataSource.getConnection();
			// 글 삭제
			PreparedStatement pstmt = conn.prepareStatement("DELETE TBL_BOARD WHERE BNO=?");
			int idx = 1;
			pstmt.setLong(idx++, bno);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 첨부 파일
	public void writeAttach(Attach attach) {
		try {
			conn = dataSource.getConnection();
			// 첨부파일 작성
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TBL_ATTACH VALUES(?, ?, ?, ?, ?, ?)");
			int idx = 1;
			pstmt.setString(idx++, attach.getUuid());
			pstmt.setString(idx++, attach.getOrigin());
			pstmt.setLong(idx++, attach.getBno());
			pstmt.setString(idx++, attach.getPath());
			pstmt.setLong(idx++, attach.getFileSize());
			pstmt.setString(idx++, new SimpleDateFormat("HHmmssSSS").format(new Date(System.currentTimeMillis())));
			
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//첨부파일 업데이트
	public void updateAttach(Attach attach) {
		try {
			conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("UPDATE TBL_ATTACH SET UUID=?, ORIGIN=?, PATH=?, FILESIZE=? WHERE BNO=?");
			int idx = 1;
			pstmt.setString(idx++, attach.getUuid());
			pstmt.setString(idx++, attach.getOrigin());
			pstmt.setString(idx++, attach.getPath());
			pstmt.setLong(idx++, attach.getFileSize());
			pstmt.setLong(idx++, attach.getBno());
			
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Attach> readAttach(Long bno){
		List<Attach> list = new ArrayList<>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT UUID, ORIGIN, PATH, FILESIZE FROM TBL_ATTACH WHERE BNO=?");
			pstmt.setLong(1, bno);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				String uuid = rs.getString(idx++);
				String origin = rs.getString(idx++);
				String path = rs.getString(idx++);
				Long fileSize = rs.getLong(idx++);
				
				Attach attach = new Attach(uuid, origin, bno, path, fileSize);
				list.add(attach);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			/* return this.readAttach(bno); */
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Attach> readAttachByPath(String path){
		List<Attach> list = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT UUID, ORIGIN, PATH, FILESIZE FROM TBL_ATTACH WHERE PATH = ?");
			pstmt.setString(1, path);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				String uuid = rs.getString(idx++);
				String origin = rs.getString(idx++);
				idx++;
				Long fileSize = rs.getLong(idx++);
				
				Attach attach = new Attach(uuid, origin, null, path, fileSize);
				list.add(attach);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			/* return this.readAttach(bno); */
		}
		return list;
	}

	// 첨부파일의 uuid를 통해서 oring네임을 반환
	public String findOriginBy(String uuid) {
		PreparedStatement pstmt = null;
		String origin = null;
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement("SELECT ORIGIN FROM TBL_ATTACH WHERE UUID=?");
			pstmt.setString(1, uuid);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int idx = 1;
				origin = rs.getString(1);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return origin;
	}
	
	// 첨부파일 삭제(bno값을 이용)
	public void deleteAttach(Long bno) {
		try {
			conn = dataSource.getConnection();
			// 첨부파일 삭제
			PreparedStatement pstmt = conn.prepareStatement("DELETE TBL_ATTACH WHERE BNO=?");
			int idx = 1;
			pstmt.setLong(idx++, bno);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// uuid로 첨부파일 삭제
	public void deleteAttachBy(String uuid) {
		try {
			conn = dataSource.getConnection();
			// 첨부파일 삭제
			PreparedStatement pstmt = conn.prepareStatement("DELETE TBL_ATTACH WHERE UUID=?");
			int idx = 1;
			pstmt.setString(idx++, uuid);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		BoardDao dao = new BoardDao();
//		// 글 작성 테스트
//		Long bno = dao.insert(new Board(null, "dao main에서 작성된 글 제목22", "dao main에서 작성된 글내용22", null, "javaman", 1L));
//		System.out.println(bno);
//		
//		// 단일 목록 조회 테스트
////		System.out.println(dao.read(1L));
//		
//		// 목록 조회 테스트
//		dao.list().forEach(System.out::println);
		
		// 페이지 테스트
//		dao.list(new Criteria(2, 50)).forEach(System.out::println);
		
//		List<Board> list = dao.list(new Criteria(2, 10));
//		for(Board b : list) {
//			System.out.println(b);
//		}
		Criteria cri = new Criteria(1, 10, 1);
		System.out.println(dao.getCount(cri));
	}

	// 페이지 DB총 개수 가져오기
	public int getCount(Criteria cri) {
		int count = 0;
		try {
			conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"SELECT COUNT(*) FROM TBL_BOARD WHERE CATEGORY=?");
			pstmt.setInt(1, cri.getCategory());
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}
