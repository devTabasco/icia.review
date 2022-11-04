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
	
	private String convertServerData(ArrayList<TodoBean> list) {
		int count = 0;
		StringBuffer serverData = new StringBuffer();
		
		/* [코드 작성 구역 5]
		 * 해당 Method에서는 DAO(DataAccessObject)로부터 우리가 선택한 캘린더 날짜에
		 * 일정이 있는 날들의 정보를 TODO.txt로 추출해 담은 ArrayList를 파라미터로 전달받아옵니다.
		 * 향상된 for문을 활용하여 ArrayList에 담긴 Bean의 StartDate를
		 * serverData에 년(yyyy),월(mm),일(dd) 중 일(dd) 정보만 담고,
		 * 각각의 일(dd) 사이에는 ":" 을 추가하여
		 * 
		 * 1:2:3:4:5:6 과 같은 형태의 String이 되도록 append 하세요.
		 * 
		 * 이때, 맨 뒤에는 : 가 아닌 숫자로 마무리되어야 합니다.
		 * count를 적절히 활용해보세요.
		 * 
		 * 예상 코드는 4~5줄 입니다.
		 * 
		 * */
		
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
			
			/* [코드 작성 구역 4]
			 * clientData로부터 받아온 정보(String type)를 split하여
			 * todoBean의 AccessCode에는 계정정보를 StartDate에는 출력하고자하는 날짜정보를 입력합니다.
			 * 그리고 database의 TODO.txt의 fileIndex도 todoBean에 입력합니다.
			 * 
			 * 코드를 작성하세요.
			 * 예상 코드는 3줄 입니다.
			 * */
			
			break;
		}

		return todoBean;
	}
}