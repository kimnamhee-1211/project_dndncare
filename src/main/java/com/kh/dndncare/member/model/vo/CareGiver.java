package com.kh.dndncare.member.model.vo;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CareGiver {
	private int memberNo;
	private String careImg;
	private String careIntro;
	private int minMoney;
	private int maxMoney;
	private String careJoinStatus;
	private String careService;
	private Date careUpdateDate;
	
	private List<Integer> infoCategory;
	
	
	// ai추천용 필드 (시작)
	private int caregiverRealAge;
	private String caregiverNational;
	private String haveLicense;
	private String caregiverAddress;
	private String haveDisease;
	private String memberGender;
	private String wantService;
	private String haveService;
	private String career;
	private String memberName;
	// ai추천용 필드 (끝)
	
	
	
	
	
}
