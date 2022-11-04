package client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import server.ServerController;

public class UserApp {

	public UserApp() {
		frontController();
	}

	private void frontController() {
		Scanner sc = new Scanner(System.in);
		String mainTitle = this.mainTitle(this.getToday(true));
		String mainMenu = this.getMainMenu();
		String[] accessInfo = new String[2];
		boolean isLoop = true;
		boolean accessResult;
		ServerController ctl = new ServerController();
		TaskManagement taskManagement = null;
		String[] itemName = { "id", "pw" };

		while (isLoop) {

			for (int idx = 0; idx < accessInfo.length; idx++) {
				this.display(mainTitle);
				this.display(this.getAccessLayer(true, accessInfo[0]));
				accessInfo[idx] = this.userInput(sc);
			}
			this.display(this.getAccessLayer(false, null));

			/* 서버에 로그인 정보 전달 */
			accessResult = ctl.controller(this.makeClientData("1", itemName, accessInfo)).equals("1") ? true : false;

			/* 서버로부터 받은 로그인 결과에 따른 화면 출력 */
			this.display(this.accessResult(accessResult));
			if (!accessResult) {
				/* 로그인 실패 */
				// n 누르면 종료
				if (this.userInput(sc).toUpperCase().equals("N")) {
					isLoop = false;
				} else {
					accessInfo[0] = null;
					accessInfo[1] = null;
				}
			} else {
				/* 로그인 성공 */
				accessInfo[1] = null;
				while (isLoop) {
					String menuSelection = new String();

					this.display(mainTitle);
					this.display(mainMenu);
					menuSelection = this.userInput(sc);

					/* 0번 선택시 서버에 로그아웃 통보 후 프로그램 종료 */
					if (menuSelection.toUpperCase().equals("0")) {
						ctl.controller(this.makeClientData("-1", itemName, accessInfo)); // 로그아웃
						isLoop = false;
					} else {
						if (menuSelection.equals("1")) {
							taskManagement = new TaskManagement();
							String[] userInput = new String[2];
							String[] selectMonth = new String[2];
							int addMonth = 0;
							int step = 0;

							// Start Date
							while (step <= 1) {
								this.display(taskManagement.taskController(11, accessInfo[0], addMonth).toString());
								if (userInput[step] == null) {
									this.printSelectMenu(step);
									userInput[step] = this.userInput(sc);
									selectMonth[step] = (LocalDate.now().plusMonths(addMonth).getYear() + "")
											+ ((LocalDate.now().plusMonths(addMonth).getMonthValue() < 10)
													? "0" + (LocalDate.now().plusMonths(addMonth).getMonthValue())
													: (LocalDate.now().plusMonths(addMonth).getMonthValue()) + "");

									/* [코드 작성 구역 1]
									 * 우리는 캘린더 입력메뉴에서
									 * Q를 누를 경우 캘린더 종료,
									 * P를 누를 경우 이전 달 캘린더, N을 누를 경우 다음 달 캘린더를 출력시켜야 합니다.
									 * 
									 * 아래 3가지 method와 addMonth 변수를 활용해 기능을 완성해보세요.
									 * 코드는 대략 9~10 줄 정도 작성됩니다.
									 * 
									 * isBreak : Q를 눌렀을 때, 반복문 나가게 함.
									 * isReStart : N 또는 P를 클릭했을 때 다시 while문을 반복하도록 함.
									 * isMonthNext : N 또는 P를 클릭함에 따라 addMonth를 조절함.
									 * */
									
									/* 해답 */
									if (this.isBreak(userInput[step])) break;
									if (this.isReStart(userInput[step])) {
										if (this.isMonthNext(userInput[step])) {
											addMonth++;
										} else {
											addMonth--;
										}
										userInput[step] = null;
										continue;
									}

									if (this.isInteger(userInput[step])) {
										if (!this.isIntegerRange(this.convertToInteger(userInput[step]), 1,
												this.getLengthOfMonth(addMonth))) {
											this.display("\n[Message : 해당 월의 날짜를 입력해주세요.  ]\n");
											userInput[step] = null;
											continue;
										}
									} else {
										
										userInput[step] = null;
										this.display("\n[Message : 숫자, P, N 중에 입력해주세요.  ]\n");
										continue;
									}

									step++;
								}
							}
						}

					}
				}
			}
		}

		this.display("\n\n  x-x-x-x-x-x-x-x-x-x- 프로그램을 종료합니다 -x-x-x-x-x-x-x-x-x-x");
		sc.close();

	}
	
	private boolean isMonthNext(String str) {
		return str.toUpperCase().equals("N") ? true : false;

	}

	// 입력값에 따라 while-loop안에 남을지 체크
	private boolean isBreak(String str) {
		return str.toUpperCase().equals("Q") ? true : false;
	}

	// while문 다시 실행 여부
	private boolean isReStart(String str) {
		return (str.toUpperCase().equals("P") || str.toUpperCase().equals("N")) ? true : false;
	}

	/* 정수 변환여부 체크 */
	private boolean isInteger(String value) {
		boolean isResult = true;
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			isResult = false;
		}
		return isResult;
	}

	/* 문자 >> 정수 변환 */
	private int convertToInteger(String value) {
		return Integer.parseInt(value);
	}

	/* 정수의 범의 체크 */
	private boolean isIntegerRange(int value, int starting, int last) {
		return (value >= starting && value <= last) ? true : false;
	}

	// makeClinetData
	private String makeClientData(String serviceCode, String[] item, String[] userData) {
		StringBuffer clientData = new StringBuffer();
		clientData.append("serviceCode=" + serviceCode);
		for (int i = 0; i < userData.length; i++) {
			if (userData[i] != null) {
				clientData.append("&");
				clientData.append(item[i] + "=" + userData[i]);
			}
		}
		return clientData.toString();
	}

	private void printSelectMenu(int step) {
		if (step == 0) {
			System.out.print("\t+++++++++++++++++++++++++++++++++++++ Start Date : ");
		} else if (step == 1) {
			System.out.print("\t+++++++++++++++++++++++++++++++++++++ End Date : ");
		}
	}

	// ID, PW 존재여부를 받아 text 출력
	private String mainTitle(String today) {
		StringBuffer title = new StringBuffer();
		title.append("------------------------------------------------------\n\n");
		title.append("\t[현우네 To-do List]\n\n");
		title.append("\t\t\t\t");
		title.append(today + "\n");
		title.append("\t\t\t\tdesigned by Changyong\n\n");
		title.append("------------------------------------------------------\n");
		return title.toString();
	}

	private String getAccessLayer(boolean isItem, String accessCode) {
		StringBuffer accessLayer = new StringBuffer();

		if (isItem) {
			accessLayer.append("\n");
			accessLayer.append("     ----------------------------------------------\n");
			accessLayer.append("     |        AccessCode          SecretCode\t  |\n");
			accessLayer.append("      --------------------------------------------\n");
			accessLayer.append("     |         " + ((accessCode != null) ? accessCode + "\t\t" : ""));
		} else {
			accessLayer.append("     -------------------------------- Connecting...\n");
		}
		return accessLayer.toString();
	}

	private String accessResult(boolean isAccess) {
		StringBuffer accessResult = new StringBuffer();

		accessResult.append("\n     >>>>>>>>>>>>>>>>>>>>>>>>> ");
		if (isAccess) {
			accessResult.append("Successful Connection\n");
			accessResult.append("     Move after 2 sceonds...\n");
		} else {
			accessResult.append("Connection Failed\n");
			accessResult.append("     _______________________________ Retry(y/n) ? ");
		}

		return accessResult.toString();
	}

	// 메인 메뉴 출력
	private String getMainMenu() {
		StringBuffer mainPage = new StringBuffer();

		mainPage.append("\n");
		mainPage.append("     [ MENU SELECTION ] __________________________________\n\n");
		mainPage.append("       1. TASK LIST		2. TASK REGISTRATION\n");
		mainPage.append("       3. MODIFY TASK		4. TASK STATS\n");
		mainPage.append("       0. DISCONNECT    \n");
		mainPage.append("     ________________________________________________ : ");

		return mainPage.toString();
	}

	/* 날짜시간 출력 : LocalDateTime Class + DateTimeFormatter Class */
	private String getToday(boolean isDate) {
		String pattern = (isDate) ? "yyyy. MM. dd." : "yyyy-MM-dd HH:mm:ss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/* 사용자 입력 */
	private String userInput(Scanner scanner) {
		return scanner.next();
	}

	/* 화면 출력 */
	private void display(String text) {
		System.out.print(text);
	}

	/* 선택한 달의 일수 구하기 */
	private int getLengthOfMonth(int addMonth) {
		return LocalDate.now().plusMonths(addMonth).lengthOfMonth();
	}
}