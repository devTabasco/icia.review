package server;

//클라이언트 요청에 따른 서비스 분기
public class ServerController {

	public ServerController() {

	}

	public String controller(String clientData) { //serviceCode=1&id=changyong&password=1234
		String accessResult = null;
		String serviceCode = clientData.split("&")[0].split("=")[1];
		
		//코드작성구역
		//만약 serviceCode가 1이면, auth의 accessCtl을 호출해서 clientData를 그대로 전송하고,
		//accessCtl은 true/false로 값을 return하는데, true면 accessResult에 "1", false면 "0"을 저장하세요.
		
		//또, 만약 serviceCode가 -1이면, auth의 logoutCtl을 호출해서 clientData를 그대로 전송,
		//이 경우 accessResult에는 "로그아웃 완료" 라는 결과를 저장.

		return accessResult;
	}
}
