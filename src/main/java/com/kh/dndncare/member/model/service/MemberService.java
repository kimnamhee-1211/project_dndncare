package com.kh.dndncare.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kh.dndncare.member.model.vo.CareGiver;
import com.kh.dndncare.matching.model.vo.Matching;
import com.kh.dndncare.member.model.vo.Member;
import com.kh.dndncare.member.model.vo.Patient;

public interface MemberService {

	Member login(Member m);

	int idCheck(String id);

	ArrayList<Member> selectAllMember();
	int enroll(Member m);

	int enrollCareGiver(CareGiver cg);

	int enrollInfoCategory(List<Integer> infoCategory);

	int enrollPatient(Patient pt);



	Member findIdResult(Member member);

	boolean sendSms(String phoneNumber, String string);

	ArrayList<Matching> calendarEvent(Member loginUser);

	int updatePassword(HashMap<String, String> changeInfo);


}
