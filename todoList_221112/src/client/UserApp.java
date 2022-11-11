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

							/* [코드 작성 구역 1]
							 * 현우네 조에서는 일정을 조회할 때,
							 * 날짜를 선택하기 전 일정의 타입을 먼저 선택하게 합니다.
							 * getChoiceListMenu()는 일정 타입을 선택하는 view를 string 타입으로 return 합니다.
							 * display method를 이용해 getChoiceListMenu()를 출력해보세요.
							 * 
							 * 예상 코드는 1줄 입니다.
							 * */
							
							menuSelection = this.userInput(sc);

							// conditions = {status, isEnable, isAll}

							/* status 선택 화면 */
							taskManagement = new TaskManagement();
							String[] userInput = new String[3];
							String[] selectMonth = new String[3];
							int addMonth = 0;

							int step = 0;

							// Start Date
							while (step <= 2) {
								if (step != 2) {
									this.display(taskManagement
											.taskController(11, accessInfo[0], addMonth, inputConditions(menuSelection))
											.toString());
								}
								if (userInput[step] == null) {
									this.printSelectMenu(step);
									if (step != 2) {
										userInput[step] = this.userInput(sc); // 사용자입력
									} else {
										userInput[step] = "T";
									}
									selectMonth[step] = (LocalDate.now().plusMonths(addMonth).getYear() + "")
											+ ((LocalDate.now().plusMonths(addMonth).getMonthValue() < 10)
													? "0" + (LocalDate.now().plusMonths(addMonth).getMonthValue())
													: (LocalDate.now().plusMonths(addMonth).getMonthValue()) + "");

									if (this.isBreak(userInput[step]))
										break;
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
										if (step == 2 && userInput[2].equals("T")) {
											step++;
											break;
										}

										userInput[step] = null;
										this.display("\n[Message : 숫자, P, N 중에 입력해주세요.  ]\n");
										continue;
									}

									step++;
								}
							}
							
							if (step > 2) {
								this.display(taskManagement.taskController(this.makeClientData(userInput, selectMonth,
										inputConditions(menuSelection), accessInfo[0])).toString());

								/* [코드 작성 구역 5]
								 * 날짜와 일정 타입을 선택하면, 일정리스트를 불러옵니다.
								 * 일정리스트를 보고난 뒤, 사용자는 0을 클릭해서 첫 메뉴선택 화면으로
								 * 넘어가야 합니다.
								 * 
								 * userInput은 menuSelection 변수로 담아 활용하세요.
								 * 해당 기능을 구현해보세요.
								 * 
								 * 예상 코드는 8줄 내외 입니다.
								 * */

							}
						}

					}
				}
			}
		}

		this.display("\n\n  x-x-x-x-x-x-x-x-x-x- 프로그램을 종료합니다 -x-x-x-x-x-x-x-x-x-x");
		sc.close();

	}
	
	private String makeClientData(String[] userInput, String[] selectMonth, String inputConditions, String accessCode) {
		/* [코드 작성 구역 2]
		 * 우리는 userInput 배열에 사용자가 선택한 날짜 정보를 가져오고 있습니다.
		 * 날짜는 1~31까지 등록되고 있지만 해당 Method에서 만들어지는 ClientData에서는
		 * 20221103과 같이 8자리의 날짜 데이터로 변환하고 있습니다.
		 * 따라서 1~9일을 선택한 경우 userInput의 값을 01~09로 변경해주어야 합니다.
		 * 해당 코드를 작성해보세요.
		 * 
		 * 예상 코드는 5줄 내외 입니다.
		 * */
		
		return "serviceCode=12&accessCode=" + accessCode + "&startDate=" + selectMonth[0] + userInput[0] + "&endDate="
				+ selectMonth[1] + userInput[1] + "&status=" + inputConditions.split("&")[0].split("=")[1]
				+ "&isEnable=" + inputConditions.split("&")[1].split("=")[1] + "&isAll="
				+ inputConditions.split("&")[2].split("=")[1];
	}
	
	private String inputConditions(String menuSelection) {
		String[] conditions = new String[3];
		if (menuSelection.equals("1")) {
			conditions[1] = "1";
			conditions[2] = "1";
		} else if (menuSelection.equals("2")) {
			conditions[0] = "0";
			conditions[1] = "1";
		} else if (menuSelection.equals("3")) {
			conditions[0] = "1";
			conditions[1] = "1";
		} else if (menuSelection.equals("4")) {
			conditions[0] = "-1";
			conditions[1] = "1";
		} else {
			conditions[1] = "0";
		}

		return "status=" + conditions[0] + "&isEnable=" + conditions[1] + "&isAll=" + conditions[2];

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
	
	private String getChoiceListMenu() {
		StringBuffer choiceMenu = new StringBuffer();

		choiceMenu.append("\n");
		choiceMenu.append("     [ MENU SELECTION ] __________________________________\n\n");
		choiceMenu.append("       1. All Condition		2. Preparing\n");
		choiceMenu.append("       3. In progress		4. Done\n");
		choiceMenu.append("       5. Temporary deletion\n");
		choiceMenu.append("     ________________________________________________ : ");

		return choiceMenu.toString();
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