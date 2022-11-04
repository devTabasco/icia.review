package client;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import server.ServerController;

public class TaskManagement {

	private ServerController serverController;

	public TaskManagement() {

	}

	public Object taskController(int selection, String accessCode, int addMonth) {
		Object result = null;
		switch (selection) {
		case 11:
			result = this.makeTaskCalendarCtl(accessCode, addMonth);
			break;
		}

		return result;
	}

	/* 특정 달의 Task Calendar 생성하기 */
	private Object makeTaskCalendarCtl(String accessCode, int addMonth) {
		serverController = new ServerController();
		String pattern = "yyyyMM";
		LocalDate today = LocalDate.now().plusMonths(addMonth);
		String[] taskDays = this.getTaskDays(serverController.controller("serviceCode=9&accessCode=" + accessCode
				+ "&date=" + today.format(DateTimeFormatter.ofPattern(pattern))));

		return this.makeCalendar(taskDays, today);
	}

	/* 특정 달의 할일이 등록되어있는 날짜가져오기 */
	private String[] getTaskDays(String str) {
		String[] taskDays = null;
		taskDays = str.split(":");

		return taskDays;
	}

	/* 특정 달의 할일이 등록되어있는 날짜를 특정 달의 달력에 표시하기 */
	private String makeCalendar(String[] days, LocalDate today) {
		StringBuffer calendar = new StringBuffer();
		int dayOfWeek = LocalDate.of(today.getYear(), today.getMonthValue(), 1).getDayOfWeek().getValue();
		int lastDay = today.lengthOfMonth();
		String pattern = "yyyy. MM";

		dayOfWeek = (dayOfWeek == 7) ? 1 : dayOfWeek + 1;

		calendar.append("\n\t+++++++++++ Previous  [ " + today.format(DateTimeFormatter.ofPattern(pattern))
				+ " ]  Next +++++++++++\n");
		calendar.append("\tSUN\tMON\tTUE\tWED\tTHU\tFRI\tSAT\n");

		for (int dayIdx = 1; dayIdx <= lastDay; dayIdx++) {

			if (dayIdx == 1) {
				for (int i = 0; i < dayOfWeek - 1; i++) {
					calendar.append("\t");
				}
			}
			calendar.append("\t");
			calendar.append((dayIdx < 10 ? " " : "") + dayIdx);
			try {
				if (days[0] != "") {
					/* [코드 작성 구역 3]
					 * 우리는 database의 TODO.txt 파일로 부터 계정 일정정보를 얻어와,
					 * 일정이 있는 날짜를 days 라는 1차 String 배열로 전달 받아 옵니다.
					 * 해당 일정이 존재하는 날에는 +를 추가해주는 코드를 작성해보세요.
					 * 
					 * 코드는 대략 3~5줄 정도 예상됩니다.
					 * 
					 * */

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			calendar.append(dayOfWeek % 7 == 0 ? "\n" : "");

			if (dayIdx == lastDay) {
				calendar.append("\n");
			}
			
			/* [코드 작성 구역 2]
			 * 우리는 윗쪽 코드에서 dayOfWeek를 활용해 토요일에 해당하는 날짜를 append한 뒤,
			 * \n을 append하여, 한줄을 내려주고 있습니다.
			 * 그러나 실제 출력에서는 줄바꿈이 되지 않고 있습니다.
			 * 토요일 이후 줄바꿈 처리를 위해 1줄의 코드가 필요합니다.
			 * 
			 * 코드를 작성해주세요.
			 * 
			 * */

		}

		return calendar.toString();
	}
}