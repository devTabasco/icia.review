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
		}

		return result;
	}
}