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
	
//	<로그아웃>
//	1. id 담긴 clientData를 받아온다. -> split해서 사용
//	2. 로그아웃 기록을 남기기위해 AccessHistoryBean에 id와 각종 정보를 등록
//	3. 로그아웃기록(로그) 생성
//	4. client에 결과 통보

	public boolean accessCtl(String clientData) {

		MemberBean memberBean = (MemberBean) this.setBean(clientData); // 내가 적은 id, pw가 담긴 memberBean.
		DataAccessObject dao = new DataAccessObject();

		memberBean.setFileIndex(0);
		ArrayList<MemberBean> memberList = dao.readDatabase(memberBean.getFileIndex());
		AccessHistoryBean accessHistoryBean;
		boolean accessResult = false;

		if (compareAccessCode(memberBean.getAccessCode(), memberList)) {
			if (isAuth(memberBean, memberList)) {

				accessHistoryBean = new AccessHistoryBean();
				accessHistoryBean.setFileIndex(1);
				accessHistoryBean.setAccessCode(memberBean.getAccessCode());
				accessHistoryBean.setAccessDate(getDate(false));
				accessHistoryBean.setAccessType(1);

				// 로그인 결과 넘겨주기
				accessResult = dao.writeAccessHistory(accessHistoryBean);

			} else {
				// 로그인 불가
			}
		}

		return accessResult;
	}

	public boolean logoutCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		AccessHistoryBean accessHistoryBean = (AccessHistoryBean) this.setBean(clientData);
		boolean logoutResult = false;

		accessHistoryBean.setFileIndex(1);
		accessHistoryBean.setAccessCode(accessHistoryBean.getAccessCode());
		accessHistoryBean.setAccessDate(getDate(false));
		accessHistoryBean.setAccessType(-1);

		logoutResult = dao.writeAccessHistory(accessHistoryBean);

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

		for (MemberBean member : memberList) {
			if (accessCode.equals(member.getAccessCode())) {
				result = true;
				break;
			}
		}

		return result;
	}

	// AccessCode와 SecretCode의 비교
	private boolean isAuth(MemberBean memberBean, ArrayList<MemberBean> memberList) {
		boolean result = false;

		for (MemberBean member : memberList) {
			if (memberBean.getAccessCode().equals(member.getAccessCode())) {
				if (memberBean.getSecretCode().equals(member.getSecretCode())) {
					result = true;
					break;
				}
			}
		}

		return result;
	}

}