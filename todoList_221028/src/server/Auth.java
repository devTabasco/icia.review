package server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;

//Login, Logout, Log history
public class Auth {

	public Auth() {

	}

//	Auth의 역할
//	
//	<로그인>
//	1. id, pw가 담긴 clientData를 받아온다. -> split해서 사용
//	2. id가 DB에 존재하는지 확인
//		2-1. DAO로 부터 데이터를 받아와 client가 보낸 데이터와 확인하여 True/False 리턴.
//		2-1. true : 3으로 이동
//		2-2. false : serverController에 false리턴
//	3. id와 pw를 db와 비교
//		3-1. true : 4로 이동
//		3-2. false : serverController에 false리턴
//	4. 접속기록(로그) 생성
//	5. client에 True리턴
//	
//	<로그아웃>
//	1. id 담긴 clientData를 받아온다. -> split해서 사용
//	2. 로그아웃 기록을 남기기위해 AccessHistoryBean에 id와 각종 정보를 등록
//	3. 로그아웃기록(로그) 생성
//	4. client에 결과 통보

	public boolean accessCtl(String clientData) { // serviceCode=1&id=changyong&password=1234

		MemberBean memberBean = (MemberBean) this.setBean(clientData); // 내가 적은 id, pw가 담긴 memberBean.
		DataAccessObject dao = new DataAccessObject();

		memberBean.setFileIndex(0);
		ArrayList<MemberBean> memberList = dao.readDatabase(memberBean.getFileIndex());
		AccessHistoryBean accessHistoryBean;
		boolean accessResult = false;

		// 코드작성 구역
		// Hint
		// accessHistoryBean에 FileIndex, AccessCode, Date, AccessType을 setting하세요!
		// 그리고 accessHistoryBean을 dao의 writeAccessHistory method에 전달해 ACCESSHISTORY.txt에
		// write하도록 하세요!
		// 그리고 그 결과를 logoutResult에 저장해 리턴합니다.
		// file index는 database의 ACCESSHISTORY.txt의 주소를 위한 index를 제공해야합니다.
		// AccessType은 로그인의 경우 1입니다.

		return accessResult;
	}

	public boolean logoutCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		AccessHistoryBean accessHistoryBean = (AccessHistoryBean) this.setBean(clientData);
		boolean logoutResult = false;

		// historybean에 값을 저장
		// fileIndex(1), accessCode, date, accessType(-1)
		// 코드작성 구역
		// Hint
		// accessHistoryBean에 FileIndex, AccessCode, Date, AccessType을 setting하세요!
		// 그리고 accessHistoryBean을 dao의 writeAccessHistory method에 전달해 ACCESSHISTORY.txt에
		// write하도록 하세요!
		// 그리고 그 결과를 logoutResult에 저장해 리턴합니다.
		// file index는 database의 ACCESSHISTORY.txt의 주소를 위한 index를 제공해야합니다.
		// AccessType은 로그아웃의 경우 -1입니다.

		return logoutResult;
	}

	private String getDate(boolean isDate) {
		String pattern = (isDate) ? "yyyyMMdd" : "yyyyMMddHHmmss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	private Object setBean(String clientData) {
		Object object = null;
		String[] splitData = clientData.split("&");

		if (splitData[0].split("=")[1].equals("-1")) {
			object = new AccessHistoryBean();
			((AccessHistoryBean) object).setAccessCode(splitData[1].split("=")[1]);
		} else if (splitData[0].split("=")[1].equals("1")) {
			object = new MemberBean();
			((MemberBean) object).setAccessCode(splitData[1].split("=")[1]);
			((MemberBean) object).setSecretCode(splitData[2].split("=")[1]);

		} else {
		}

		return object;
	}

	// AccessCode 존재여부 확인
	private boolean compareAccessCode(String accessCode, ArrayList<MemberBean> memberList) {
		boolean result = false;

		// 코드작성 구역
		// ArrayList타입인 memberList에서 향상된 for문을 활용해 MemberBean을 받아와 해당 MemberBean의
		// AccessCode와
		// parameter로 받아온 accessCode가 같은지 비교하여 같다면 result를 true로 변경한 뒤, for문을 break하세요!

		return result;
	}

	// AccessCode와 SecretCode의 비교
	private boolean isAuth(MemberBean memberBean, ArrayList<MemberBean> memberList) {
		boolean result = false;

		// 코드작성 구역
		// ArrayList타입인 memberList에서 향상된 for문을 활용해 MemberBean을 받아와 해당 MemberBean의
		// AccessCode와
		// parameter로 받아온 memberBean에 담긴 accessCode가 같은지 비교하여 같다면 result를 true로 변경한 뒤,
		// for문을 break하세요!

		return result;
	}

}
