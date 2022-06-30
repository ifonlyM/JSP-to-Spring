package net.ifonlygaram.domain;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 자바빈 규약을 따름
public class Board {
	private Long bno;
	private String title;
	private String content;
	private Date regDate;
	private String id;
	private Long category;
	 
	private List<Attach> attachs;
	private Integer replyCnt;

	public Board(String title, String content, String id, Long category) {
		super();
		this.title = title;
		this.content = content;
		this.id = id;
		this.category = category;
	}

	public Board(Long bno, String title, String content, Date regDate, String id, Long category) {
		super();
		this.bno = bno;
		this.title = title;
		this.content = content;
		this.regDate = regDate;
		this.id = id;
		this.category = category;
	}

	public Board(Long bno, String title, Date regDate, String id, Long category) {
		super();
		this.bno = bno;
		this.title = title;
		this.regDate = regDate;
		this.id = id;
		this.category = category;
	}

	public Board(Long bno, String title, String content) {
		super();
		this.bno = bno;
		this.title = title;
		this.content = content;
	}
	
	
	
	
}
