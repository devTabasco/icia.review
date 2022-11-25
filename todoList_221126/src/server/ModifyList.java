package server;

import java.util.ArrayList;

import server.beans.TodoBean;

//5주차
public class ModifyList {
	public ModifyList() {

	}

	public boolean modifyRequest(String clientData) {
		boolean result = false;
		ArrayList<TodoBean> todoBeans;

		String[] splitData = clientData.split("&");
		
		
		/* [코드 작성 구역 2]
		 * 우리는 수정을 하고 싶은 리스트를 선택해, 수정할 내용들을 입력 받았습니다.
		 * 그리고 그 내용은 clientData에 담겨있습니다.
		 * 또한, clientData에는 수정할 내용들과 더불어 원본데이터도 함께 받아오고 있습니다.
		 * 
		 * 이를 활용하기 위해 원본데이터와 수정할 데이터를 구분하는 작업을 진행해봅시다.
		 * 
		 * 원본 데이터는 origin 이라는 String 배열에,
		 * 수정 데이터는 modifyMenu 라는 String 배열에 각각 저장해보세요.
		 * 
		 * 예상코드는 2줄 입니다.
		 * 
		 *  */
		
		String[] origin = null;
		String[] modifyMenu = null;
		
		
		TodoBean todoBean = new TodoBean();
		DataAccessObject dao = new DataAccessObject();
		
		todoBeans = dao.readTodoData(2);
		
		//5주차
		/* [코드 작성 구역 3]
		 * 조금 전 2번 문제에서 우리는 원본데이터와, 수정할 데이터를 구분하여 받아왔습니다.
		 * 그리고 바로 위 코드를 통해 TODO.txt 파일의 내용을 todoBeans라는 ArrayList에 담아왔습니다.
		 * 
		 * 따라서 현재 우리가 가지고 있는 재료는
		 * 1. TODO.txt의 모든 내용이 담긴 todoBeans(ArrayList Type)
		 * 2. 수정할 row의 원본 데이터
		 * 3. 수정할 row의 수정 데이터
		 * 4. 비어있는 todoBean(TodoBean Type)
		 * 
		 * 이렇게 4가지 입니다.
		 * 
		 * 4가지 재료를 적절히 활용하여, 코드작성구역3, 4를 채워주세.
		 * 
		 * 코드작성구역3의 예상코드는 7줄,
		 * 코드작성구역4의 예상코드는 7줄 또는 14줄 입니다.(사용자의 코드작성 습관에 따라 다소 길어질수도 있습니다.)
		 * 
		 * 
		 * Hint
		 * - 코드작성구역 4에서 우리는 수정하고자 하는 특정 1줄의 데이터를 찾아내고, 그 데이터만 수정해주어야 합니다.
		 * - for문에서 사용할 수 있는 기능 중 하나를 잘 생각해보세요.
		 * 
		 *  */
		
		for (TodoBean listData : todoBeans) {
			
			//5주차
			/* [코드 작성 구역 4] 
			 * 
			 * 
			 * */
			
			if(modifyMenu[0].equals("contents")) {
				listData.setContents(modifyMenu[1]);
			}else if(modifyMenu[0].equals("date")) {
				listData.setStartDate(modifyMenu[1].split(",")[0] + "0000");
				listData.setEndDate(modifyMenu[1].split(",")[1] + "0000");			
			}else if(modifyMenu[0].equals("status")) {
				listData.setStatus(modifyMenu[1]);
			}else if(modifyMenu[0].equals("comments")) {
				listData.setComment(modifyMenu[1]);
			}else if(modifyMenu[0].equals("isEnAble")) {
				listData.setIsEnable(modifyMenu[1]);
			}
		}

		result = dao.writeModitiedTodoList(todoBeans);
		return result;
	}
}