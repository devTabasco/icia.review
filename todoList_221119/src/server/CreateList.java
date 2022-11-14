package server;

import server.beans.TodoBean;

//4주차
public class CreateList {

	public CreateList() {
	}

	public boolean createListCtl(String clientData) {
		System.out.println(clientData);
		TodoBean todoBean = null;
		DataAccessObject dao = new DataAccessObject();
		boolean result = false;
		String[] splitData = clientData.split("&");
		
		/* [코드 작성 구역 2]
		 * createListCtl에서는 ServerController에서 전달받은 clientData를 활용해,
		 * 사용자가 등록한 Todo 정보를 Bean에 저장하여 DAO의 writeTodoList에 전달하는 역할을 합니다.
		 * 
		 * 그리고 writeTodoList에서 TODO.txt에 데이터 저장 성공/실패 결과를 boolean으로 return하고,
		 * createListCtl에서는 그 값을 그대로 다시 serverController에 return하게 됩니다.
		 * 
		 * 이 과정을 위해 우리는 todoBean에 clientData에서 전달받은 데이터와 default(기본값)로 저장해야하는 값들을
		 * 입력한 뒤, DAO에게 전달해주어야 합니다.
		 * 
		 * default값으로는 status = "0", isEnable = "1", isAll = false, comment = "null" 입니다.
		 * 그 외의 값은 clientData를 활용하거나, 각자 입력해주시면 됩니다.
		 * 
		 * 그럼 todoBean에 값을 입력해봅시다!
		 * 
		 * 예상코드는 10줄 입니다.
		 * 
		 *  */
		
		// 로그인 결과 넘겨주기
		result = dao.writeTodoList(todoBean);

		return result;
	}

}