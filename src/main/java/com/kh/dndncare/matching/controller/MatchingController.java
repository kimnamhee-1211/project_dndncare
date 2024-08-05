package com.kh.dndncare.matching.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.dndncare.matching.model.service.MatchingService;
import com.kh.dndncare.matching.model.vo.Hospital;
import com.kh.dndncare.matching.model.vo.MatMatptInfo;
import com.kh.dndncare.matching.model.vo.MatPtInfo;
import com.kh.dndncare.matching.model.vo.Matching;
import com.kh.dndncare.member.model.Exception.MemberException;
import com.kh.dndncare.member.model.vo.InfoCategory;
import com.kh.dndncare.member.model.vo.Member;
import com.kh.dndncare.member.model.vo.Patient;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MatchingController {
	
	@Autowired
	private MatchingService mcService;
	
	@GetMapping("publicMatching.mc")
	public String publicMatchingView() {
		return "publicMatching";
	}
	
	
	@GetMapping("joinMatchingMainView.jm")
	public String joinMatchingMainView() {
		
		return "joinMatchingMain";
	}
	
	//공동간병 병원 선택
	@GetMapping("joinMatching.jm")
	public String joinMatchinjmain(@RequestParam("hospitalName") String hospitalName, @RequestParam("hospitalAddress") String hospitalAddress,  Model model) {
		
		//병원 정보 전달
		Hospital hospital = new Hospital();
		hospital.setHospitalName(hospitalName);
		hospital.setHospitalAddress(hospitalAddress);
		model.addAttribute("hospital", hospital);
		
		//병원으로 list 뽑기 
		ArrayList<MatMatptInfo> list = mcService.getJmList(hospitalName);
		System.out.println(list);
		
		model.addAttribute("list", list);
		return "joinMatching";
	}

	
	@GetMapping("joinMatchingEnrollView.jm")
	public String joinMatchingEnrollView(@RequestParam("hospitalName") String hospitalName, 
										@RequestParam("hospitalAddress") String hospitalAddress, Model model) {
		Hospital hospital = new Hospital();
		hospital.setHospitalName(hospitalName);
		hospital.setHospitalAddress(hospitalAddress);
		model.addAttribute("hospital", hospital);
	
		return "joinMatchingEnroll.jm";
	}
	
	
	
	//공동간병 등록
	@PostMapping("enrollJoinMatching.jm")
	public String enrollJoinMatching(@ModelAttribute Matching jm, @ModelAttribute MatPtInfo jmPt, @ModelAttribute Hospital ho,
									HttpSession session, Model model) {
		
		//병원등록
		int result1 = mcService.enrollHospital(ho);
		
		//매칭 등록
		jm.setMoney(jmPt.getAntePay() * jm.getPtCount());
		jm.setMatType(2);
		jm.setHospitalNo(ho.getHospitalNo());		
		System.out.println("등록" + jm);
		int result2 = mcService.enrollMatching(jm);
		
		//매칭 환자 등록
		jmPt.setMatNo(jm.getMatNo());
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		Patient pt = mcService.getPatient(memberNo);		
		jmPt.setPtNo(pt.getPtNo());
		jmPt.setService("공동간병");
		jmPt.setMatAddressInfo(jmPt.getMatAddressInfo());
		jmPt.setGroupLeader("Y");
		System.out.println("등록" + jmPt);
		int result3 = mcService.enrollMatPtInfo(jmPt);
		
		
		if(result1>0 && result2>0 && result3>0) {
			return "redirect:joinMatching.jm";
		}
		throw new MemberException("공동간병 그룹 등록 실패");
	}
	
	
	//공동간병 상세 정보 
	@PostMapping(value="getJmContent.jm", produces="application/json; charset=UTF-8")
	@ResponseBody
	public void getjmMatMatptInfo(@RequestParam("matNo") int matNo, HttpServletResponse response) {
		
		//get matching & ptinfo
		MatMatptInfo jmMatMatptInfo = mcService.getMatMatptInfo(matNo);
		
		//MatNo로 get 공동간병 Patient
		ArrayList<Patient> jmPts = mcService.getPatientToMatNo(matNo);		
		
		//공동간병 Patient에 member info set
		for(Patient jmPt : jmPts) {
			
			//get member info (대분류 : 소분류)
			ArrayList<InfoCategory> jmPtInfos =  mcService.getInfo(jmPt.getMemberNo());
			System.out.println(jmPtInfos);
			
			ArrayList<String> disease =  new ArrayList<>();
			String diseaseLevel = null;
			if(jmPtInfos != null) {
				for(InfoCategory jmPtInfo : jmPtInfos) {
					if(jmPtInfo.getLCategory().equals("disease")) {
						disease.add(jmPtInfo.getSCategory());					
					}else if(jmPtInfo.getLCategory().equals("diseaseLevel")) {
						
						diseaseLevel = jmPtInfo.getSCategory();				
					}	
				}	
				//공동 간병 참여자들 Patient에 member info set
				jmPt.setDisease(disease);
				jmPt.setDiseaseLevel(diseaseLevel);
			}		

		}		
		
		System.out.println("jmMatMatptInfo" + jmMatMatptInfo);
		System.out.println("jmPts" + jmPts);
		HashMap<String, Object> jmMacPt = new HashMap<String, Object>();
		jmMacPt.put("jmMatMatptInfo", jmMatMatptInfo);
		jmMacPt.put("jmPts", jmPts);
				
		response.setContentType("application/json; charset=UTF-8");
		GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");
		Gson gson = gb.create();
		
		try {
			gson.toJson(jmMacPt, response.getWriter());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
		
	
	}

	//공동간병 참여
	@PostMapping("joinJoinMatching.jm")
	public String joinJoinMatching(@RequestParam("matNo") int matNo, @RequestParam("matRequest") String matRequest,
									HttpSession session) {
		
		MatPtInfo joinMatptInfo = new MatPtInfo();
		
		MatMatptInfo jmMatMatptInfo = mcService.getMatMatptInfo(matNo);	
		joinMatptInfo.setMatNo(jmMatMatptInfo.getMatNo());
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		Patient joinPt = mcService.getPatient(memberNo);
		joinMatptInfo.setPtNo(joinPt.getPtNo());
		joinMatptInfo.setAntePay(jmMatMatptInfo.getAntePay());
		joinMatptInfo.setService("공동간병");
		joinMatptInfo.setMatAddressInfo(jmMatMatptInfo.getMatAddressInfo());
		joinMatptInfo.setMatRequest(matRequest);
		joinMatptInfo.setGroupLeader("N");
		System.out.println(joinMatptInfo);
		
		int result = mcService.enrollMatPtInfo(joinMatptInfo);

		if(result > 0) {
			return "redirect:joinMatching.jm";
		}
		throw new MemberException("공동간병 그룹 등록 실패");
	}
	
	
	
	@GetMapping("reviewDetail.mc")
	public String getMethodName() {
		return "reviewDetail";
	}
	
	
	
	
	
	
	
	
	
	
	
}
