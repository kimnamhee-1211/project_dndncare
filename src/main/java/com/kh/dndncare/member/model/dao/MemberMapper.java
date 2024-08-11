package com.kh.dndncare.member.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.dndncare.board.model.vo.PageInfo;
import com.kh.dndncare.member.model.vo.CalendarEvent;
import com.kh.dndncare.member.model.vo.CareGiver;
import com.kh.dndncare.member.model.vo.MatPtInfo;
import com.kh.dndncare.member.model.vo.Matching;
import com.kh.dndncare.member.model.vo.Member;
import com.kh.dndncare.member.model.vo.Patient;

@Mapper
public interface MemberMapper {

	Member login(Member m);

	int noInfomemberdle();
	
	int idCheck(String id);
	
	int nickNameCheck(String nickName);

	ArrayList<CalendarEvent> caregiverCalendarEvent(Integer memberNo);

	ArrayList<Member> selectAllMember();
	
	int enroll(Member m);

	int enrollCareGiver(CareGiver cg);

	int enrollInfoCategory(Object ob);

	int enrollPatient(Patient pt);


	Member findIdResult(Member member);

	HashMap<String, String> getCaregiverInfo(int memberNo);

	ArrayList<HashMap<String, String>> getCaregiverExp(int memberNo);

	ArrayList<Patient> selectPatientList(HashMap<String, Object> condition);

	ArrayList<HashMap<String, String>> getPatientExp(ArrayList<Integer> pNoList);

	ArrayList<Patient> choicePatientList(ArrayList<Integer> choiceNoList);

	ArrayList<HashMap<String, String>> getCaregiverWant(int memberNo);

	ArrayList<HashMap<String, Object>> getPatientInfo(ArrayList<Integer> mNoList);

	HashMap<String, String> getPatientMyInfo(int memberNo);

	ArrayList<HashMap<String, String>> getPatientMyExp(int memberNo);

	ArrayList<HashMap<String, String>> getCaregiverMyWant(int memberNo);

	ArrayList<HashMap<String, Object>> selectCaregiverList(HashMap<String, Object> condition);

	ArrayList<HashMap<String, Object>> selectCaregiverInfo(ArrayList<Integer> mNoList);

	ArrayList<CareGiver> choiceCaregiverList(ArrayList<Integer> choiceNoList);

	ArrayList<HashMap<String, Integer>> getPatientEvent(int memberNo);

	ArrayList<CalendarEvent> patientCalendarEvent(ArrayList<Integer> matNoList);

	ArrayList<Member> selectMemberList(ArrayList<Integer> memberNoList);

	ArrayList<Matching> selectMatchingList(RowBounds rowBounds, ArrayList<Integer> resultMatNoList);

	int getMatchingListCount(HashMap<String, Object> searchOption);

	ArrayList<Member> selectMatchingMemberList(ArrayList<Integer> matNoList);

	ArrayList<MatPtInfo> selectMatchingPTInfoList(ArrayList<Integer> matNoList);

	ArrayList<HashMap<String, Integer>> searchDefaultMatNoList(HashMap<String, Object> searchDefaultMap);

	ArrayList<Integer> searchTermMatNoList(HashMap<String, Object> termMap);

	ArrayList<Integer> searchTimeMatNoList(HashMap<String, Object> termMap);

	ArrayList<HashMap<String, Integer>> searchCategoryMatNoList(ArrayList<Integer> tempMatNoList);

	ArrayList<Matching> searchMatchingList(RowBounds rowBounds, ArrayList<Integer> resultMatNoList);

	ArrayList<CareGiver> selectAllCaregiver(RowBounds rowBounds, Object obj);

	int getCaregiverListCount();

	ArrayList<HashMap<String, Integer>> getCaregiverScoreList(ArrayList<Integer> cNoList);

	ArrayList<CareGiver> searchDefaultCaregiverNoList(HashMap<String, Object> searchDefaultMap);

	ArrayList<HashMap<String, Integer>> searchCaregiverCategoryMNoList(ArrayList<Integer> cNoList);

	ArrayList<CareGiver> searchCaregiverList(RowBounds rowBounds, ArrayList<Integer> resultCaregiverNoList);



	



}
