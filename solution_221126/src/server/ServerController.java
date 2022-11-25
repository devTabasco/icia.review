package server;

//클라이언트 요청에 따른 서비스 분기
public class ServerController {

	public ServerController() {

	}

	public String controller(String clientData) {
		TaskManager taskManager;
		String result = null;
		String serviceCode = clientData.split("&")[0].split("=")[1];
		
		if (serviceCode.equals("1")) {
			Auth auth = new Auth();
			result = auth.accessCtl(clientData) ? "1" : "0";
		}else if(serviceCode.equals("-1")) {
			Auth auth = new Auth();
			result = auth.logoutCtl(clientData) ? "1" : "0";
		}else if(serviceCode.equals("9")) {
			taskManager = new TaskManager();
			result = taskManager.getTodoDateCtl(clientData);
		}else if(serviceCode.equals("12")) {//리스트 생성
			taskManager = new TaskManager();
			result = taskManager.getTodoListCtl(clientData);
		}else if(serviceCode.equals("2")) {//등록
			CreateList createList = new CreateList();
			result = createList.createListCtl(clientData)?"일정등록성공":"일정등록실패";
		}else if(serviceCode.equals("13")) {
			//serviceCode=13&id=changyong&startDate=202211010000&endDate=202211120000
			taskManager = new TaskManager();
			//202211011100,202211301000,쓰레기통테스트,1,0,null;202211021100,202211051000,코딩하기,1,1,null;
			result = taskManager.getModifyListCtl(clientData);
		}else if(serviceCode.equals("14")) {
			ModifyList modifyList = new ModifyList();
			result = modifyList.modifyRequest(clientData)?"수정완료":"수정불가";
		}

		return result;
	}
}