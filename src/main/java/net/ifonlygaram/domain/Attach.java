package net.ifonlygaram.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attach {
	// UUID
	// Origin
	// Bno
	
	private String uuid;
	private String origin;
	private Long bno;
	private String path;
	private Long fileSize;
}
