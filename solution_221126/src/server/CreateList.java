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
		
		todoBean = new TodoBean();
		todoBean.setFileIndex(2);
		todoBean.setAccessCode(splitData[1].split("=")[1]);
		todoBean.setStartDate(splitData[2].split("=")[1]);
		todoBean.setEndDate(splitData[3].split("=")[1]);
		todoBean.setContents(splitData[4].split("=")[1]);
		todoBean.setStatus("0");
		todoBean.setIsEnable("1");
		todoBean.setIsAll(false);
		todoBean.setComment("null");

		result = dao.writeTodoList(todoBean);

		return result;
	}

}