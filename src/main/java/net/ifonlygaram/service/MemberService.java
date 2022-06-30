package net.ifonlygaram.service;

import java.util.List;

import net.ifonlygaram.domain.Member;

public interface MemberService {

	// 로그인
	boolean login(String id, String pwd);
	
	// 로그아웃

	// 회원가입
	void join(Member member);
	
	// 탈퇴
	void signOut(String id);
	
	// 정보수정
	void modify(Member member);
	
	// id/pwd 찾기
	
	
	// 회원 목록 조회
	List<Member> getMembers();
	
	// 단일 회원 조회
	Member findBy(String SELECT, String id);
}
