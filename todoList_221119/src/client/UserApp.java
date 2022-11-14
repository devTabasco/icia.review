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
						
						//3주차
						if (menuSelection.equals("1")) {

							this.display(this.getChoiceListMenu());
							
							menuSelection = this.userInput(sc);

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

								while (true) {
									menuSelection = this.userInput(sc);
									if (!menuSelection.equals("0")) {
										this.display("돌아가기를 원하시면, 0을 눌러주세요 : ");
									} else {
										break;
									}
								}

							}
						}
						
						if (menuSelection.equals("2")) {
							int addMonth = 0;
							// 일정 등록
							// 캘린더 띄우기(토탈기준)
							taskManagement = new TaskManagement();
							String[] inputSetData = new String[3];
							String tmp;
							int step = 0;
							while (true) {

								while (step <= 2) {
									this.display(taskManagement
											.taskController(11, accessInfo[0], addMonth, inputConditions("1"))
											.toString());
									this.display("\t+++++++++++++++++++++++++++++++++++++++++++++++++\n");
									if (step == 0) {
										this.display("\t시작일을 입력해주세요. ex) 2022/10/21 : ");
									} else if (step == 1) {
										this.display("\t마감일을 입력해주세요. ex) 2022/10/30 : ");
									} else {
										this.display("\t내용을 입력해주세요. (내용은 30자 이내입니다.) : ");
									}
									if (step != 2) {
										inputSetData[step] = this.userInput(sc);
									} else {
										sc.nextLine();
										inputSetData[step] = sc.nextLine();
									}

									if (this.isBreak(inputSetData[step]))
										break;
									if (this.isReStart(inputSetData[step])) {
										if (this.isMonthNext(inputSetData[step])) {
											addMonth++;
										} else {
											addMonth--;
										}
										inputSetData[step] = null;
										continue;
									}

									if (step != 2) {
										tmp = inputSetData[step].replace("/", "");
										inputSetData[step] = tmp;
									} else {
										tmp = "";
									}

									/* [코드 작성 구역 1]
									 * TODOLIST를 등록할 때 우리는 사용자로부터 3가지 정보(시작일, 마감일, 내용)를 입력받습니다.
									 * 
									 * 현재는 시작일, 마감일, 내용에서 사용자 에러를 거르지 않고 있습니다.
									 * 그래서 우리는 아래 3가지의 사용자 에러를 막아보려고 합니다.
									 * 
									 * 1. 시작일을 입력할 때, 시작일은 오늘날짜 이전으로 등록할 수 없습니다.
									 * 2. 마감일을 입력할 때, 마감일은 시작일 이전으로 등록할 수 없습니다.
									 * 3. 내용을 입력할 때, 내용은 30자 이내로 작성되어야 합니다.
									 * 
									 * 기능완성 + 사용자 에러발생 시 에러메시지도 출력해주어야 합니다.
									 * 
									 * 코드를 작성해보세요.
									 * 예상코드는 15 ~ 18줄 내외 입니다.
									 * 
									 * 문제가 어렵다면, 해당 Class 맨 마지막줄에 힌트를 읽어보세요!!
									 * 
									 *  */
									
									step++;

								}

								// 여기서 inputSetData[]를 사용해 server에 전달.
								ctl.controller(this.makeClientData("2", inputSetData, accessInfo[0]));

								this.display("\t등록이 완료되었습니다.\n");
								this.display("\t1. 새로운 일정 등록		2. 나가기\n");
								this.display("\t+++++++++++++++++++++++++++++++++++++++++++Select : ");

								while(true) {
									menuSelection = this.userInput(sc);
									if(!(menuSelection.equals("2") || menuSelection.equals("1"))) {
										this.display("\t올바른 번호를 선택해주세요.");
									}else {
										break;
									}
								}

								if (menuSelection.equals("2")) {
									break;
								} else if (menuSelection.equals("1")) {
									addMonth = 0;
									step = 0;
									inputSetData = null;
									continue;
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
	
	private String makeClientData(String[] userInput, String[] selectMonth, String inputConditions, String accessCode) {
		for (int i = 0; i < userInput.length; i++) {
			if (userInput[i].length() == 1) {
				userInput[i] = "0" + userInput[i];
			}
		}

		return "serviceCode=12&accessCode=" + accessCode + "&startDate=" + selectMonth[0] + userInput[0] + "&endDate="
				+ selectMonth[1] + userInput[1] + "&status=" + inputConditions.split("&")[0].split("=")[1]
				+ "&isEnable=" + inputConditions.split("&")[1].split("=")[1] + "&isAll="
				+ inputConditions.split("&")[2].split("=")[1];
	}
	
	//4주차
	private String makeClientData(String serviceCode, String[] inputSetData, String accessCode) {
		// serviceCode=2&id=changyong&startDate=202211020000&endDate=202211020000&contents=dd
		String[] item = { "startDate", "endDate", "contents" };
		StringBuffer clientData = new StringBuffer();
		clientData.append("serviceCode=" + serviceCode);
		clientData.append("&id=" + accessCode);
		for (int i = 0; i < inputSetData.length; i++) {
			if (inputSetData[i] != null) {
				clientData.append("&");
				clientData.append(item[i] + "=" + ((i < 2) ? (inputSetData[i] + "0000") : inputSetData[i]));
			}
		}
//		System.out.println(clientData.toString());
		return clientData.toString();
	}
	
	//3주차
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

/*
 * 코드 작성 구역 1 힌트
 * - if문을 활용하세요.
 * - step을 적절히 활용해보세요.
 * - getToday Method를 활용해보세요.
 * - inputSetData[2]의 length()를 활용해보세요.
 * 
 * */

