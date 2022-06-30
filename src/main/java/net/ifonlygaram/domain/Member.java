package net.ifonlygaram.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Member {
	@NonNull
	private String id;
	
	@NonNull
	private String pwd;
	
	private String email;
	private String name;
}
