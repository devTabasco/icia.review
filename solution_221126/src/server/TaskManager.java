package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import server.beans.TodoBean;

public class TaskManager {

	public TaskManager() {

	}
	
	public String getModifyListCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		//1. clientData : serviceCode=13&id=changyong&startDate=202211010000&endDate=202211120000
		
		//5주차
		/* [코드 작성 구역 1]
		 * 우리는 지금 "3. MODIFY TASK" 를 사용자가 선택한 뒤, 날짜를 입력받아
		 * 해당 날자의 리스트를 출력해주려고 합니다.
		 * 
		 * 그런데 리스트 출력은 우리가 "1.TASK LIST" 를 클릭했을 때, 이미 만들어두었습니다.
		 * 어디에 있는 어떤 메소드를 활용했는지 다시 한 번 생각해보고,
		 * 파라미터와 리턴타입을 고려하여, 리스트를 전달하는 코드를 만들어보세요!
		 * 
		 * return 뒤에 ""을 지우고 답을 입력하세요!
		 * 
		 * 예상코드는 1줄 입니다.
		 * 
		 *  */
		
		//202211011100,202211301000,쓰레기통테스트,1,0,null;202211021100,202211051000,코딩하기,1,1,null;
		/* 해답 */
		return this.convertListData(dao.getList(this.setBean(clientData)));
	}

	/* 특정 계정의 특정 월의 할일이 등록되어 있는 날짜 리스트 가져오기 */
	public String getTodoDateCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		return this.convertServerData(dao.getToDoList(this.setBean(clientData)));
	}
	
	public String getTodoListCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		return this.convertListData(dao.getList(this.setBean(clientData)));
	}
	
	private String convertListData(ArrayList<TodoBean> list) {
		StringBuffer serverData = new StringBuffer();
		
		for(TodoBean todo : list) {
			serverData.append(todo.getStartDate() + ",");
			serverData.append(todo.getEndDate() + ",");
			serverData.append(todo.getContents() + ",");
			serverData.append(todo.getStatus() + ",");
			serverData.append(todo.getIsEnable() + ",");
			serverData.append(todo.getComment() + ";");
		}
		
		/* 마지막으로 추가된 항목(;) 삭제 */
		if(serverData.length() != 0){
			serverData.charAt((serverData.length()-1));
		}
		return serverData.toString();
	}
	
	private String convertServerData(ArrayList<TodoBean> list) {
		int count = 0;
		StringBuffer serverData = new StringBuffer();
		
		for (TodoBean todo : list) {
			if(count != 0) serverData.append(":");
			serverData.append(todo.getStartDate().substring(6, 8));
			count++;
		}

		/******************아래 코드는 참고하지 않아도 됩니다.**********************/
		/* 중복값 제거 */
		String[] arr = serverData.toString().split(":");       
		HashSet <String> hashSet = new HashSet<>(Arrays.asList(arr));         
		String[] resultArr = hashSet.toArray(new String[0]);
		StringBuffer str = new StringBuffer();
		
		/*중복 제거 후 다시 양식 맞추기*/
		for(int i=0;i<resultArr.length;i++) {
			if(i!=0) str.append(":");
			str.append(resultArr[i]);
		}
		/*******************위 코드는 참고하지 않아도 됩니다.***********************/
		
		return str.toString();
	}

	private TodoBean setBean(String clientData) {
		TodoBean todoBean = null;
		String[] splitData = clientData.split("&");
		switch (splitData[0].split("=")[1]) {
		case "9":
			
			todoBean = new TodoBean();
			
			todoBean.setFileIndex(2);
			todoBean.setAccessCode(splitData[1].split("=")[1]);
			todoBean.setStartDate(splitData[2].split("=")[1]);
			
			break;
			
		case "12":
			todoBean = new TodoBean();
			todoBean.setFileIndex(2);
			todoBean.setServiceCode("12");
			todoBean.setAccessCode(splitData[1].split("=")[1]);
			todoBean.setStartDate(splitData[2].split("=")[1]);
			todoBean.setEndDate(splitData[3].split("=")[1]);
			todoBean.setStatus(splitData[4].split("=")[1]);
			todoBean.setIsEnable(splitData[5].split("=")[1]);
			todoBean.setIsAll((splitData[6].split("=")[1].equals("1")?true:false));
			
			break;
			
		case "13":
			todoBean = new TodoBean();
			todoBean.setFileIndex(2);
			todoBean.setServiceCode("13");
			todoBean.setAccessCode(splitData[1].split("=")[1]);
			todoBean.setStartDate(splitData[2].split("=")[1].substring(0, 8));
			todoBean.setEndDate(splitData[3].split("=")[1].substring(0, 8));
			
		}

		return todoBean;
	}
}