package net.ifonlygaram.domain;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
	private Long rno;
	private String title;
	private String content;
	private String regDate;
	private String id;
	private String name;
	private Long bno;
}
