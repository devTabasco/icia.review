package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import server.beans.TodoBean;

public class TaskManager {

	public TaskManager() {

	}

	/* 특정 계정의 특정 월의 할일이 등록되어 있는 날짜 리스트 가져오기 */
	public String getTodoDateCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		return this.convertServerData(dao.getToDoList(this.setBean(clientData)));
	}
	
	//3주차
	public String getTodoListCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		return this.convertListData(dao.getList((TodoBean)this.setBean(clientData)));
	}
	
	//3주차
	private String convertListData(ArrayList<TodoBean> list) {
		StringBuffer serverData = new StringBuffer();
		
		/* [코드 작성 구역 3]
		 * 우리는 지금 사용자가 선택한 날짜와 기준에 맞춰 DB에서 TODO데이터를 가져와 ArrayList에 저장했습니다.
		 * 저장한 ArrayList를 해체하기 위해 향상된 for문을 활용하려합니다.
		 * 
		 * 향상된 for문을 활용해 최종적으로 아래와 같이 column(열)간에는 ','를 구분자로 / 데이터(record, row)간에는 ;를 구분자로 사용해
		 * String type으로 return하려 합니다.
		 * 
		 * 예시 : 202211011100,202211301000,정현우,1,1,null;202211021100,202211051000,영어단어 외우기,0,1,null
		 * 
		 * 코드를 작성해보세요.
		 * 
		 * 힌트 : 마지막 ;를 제외하는 코드는 작성되어 있습니다.
		 * 
		 * 예상 코드는 8줄 내외 입니다.
		 * */
		
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
			
		}

		return todoBean;
	}
}