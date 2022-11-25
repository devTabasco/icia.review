package server;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;
import server.beans.TodoBean;

//To access database
public class DataAccessObject {
	
	private String[] fileInfo = {
			
			/* [코드 작성 구역 0]
			 * MEMBERS.txt, ACCESSHISTORY.txt, TODO.txt
			 * 가 존재하는 경로를 fileInfo에 입력하세요.
			 * 
			 *  */
			
//			"[MEMBERS.txt 파일 경로]",
//			"[ACCESSHISTORY.txt 파일 경로]",
//			"[TODO.txt 파일 경로]" 
			
	};

	public DataAccessObject() {

	}
	
	//4주차
	public boolean writeTodoList(TodoBean accessInfo) {
		boolean result = false;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileInfo[accessInfo.getFileIndex()]), true));
			
			bufferedWriter.write(accessInfo.getAccessCode() + "," + accessInfo.getStartDate() + ","
					+ accessInfo.getEndDate() + "," + accessInfo.getContents() + "," + accessInfo.getStatus() + ","
					+ accessInfo.getIsEnable() + "," + accessInfo.getComment() + "\n");
			bufferedWriter.flush(); // write로 담은 내용 출력 후, 버퍼를 비움.

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close(); // bufferedWriter close.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	// member파일 전달
	public ArrayList<MemberBean> readDatabase(int fileIndex) {
		MemberBean memberBean;
		ArrayList<MemberBean> memberList = null;
		BufferedReader buffer = null;
		String line = null;

		if (fileIndex == 0) {
			try {
				buffer = new BufferedReader(new FileReader(new File(fileInfo[fileIndex])));
				memberList = new ArrayList<MemberBean>();

				while ((line = buffer.readLine()) != null) {
					String[] tmp = line.split(",");
					memberBean = new MemberBean();

					// bean가져와서 데이터 넣기
					memberBean.setAccessCode(tmp[0]);
					memberBean.setSecretCode(tmp[1]);
					memberBean.setName(tmp[2]);
					memberBean.setPhoneNumber(tmp[3]);
					memberBean.setActivation(Integer.parseInt(tmp[4]));

					memberList.add(memberBean);

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				memberList = null; // 참조선 제거
				e.printStackTrace();
			} finally {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return memberList;
	}

	// 접속기록 작성
	public boolean writeAccessHistory(AccessHistoryBean accessInfo) {
		boolean result = false;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileInfo[accessInfo.getFileIndex()]), true));
			bufferedWriter.write(accessInfo.getAccessCode() + "," + accessInfo.getAccessDate() + ","
					+ accessInfo.getAccessType() + "\n");
			bufferedWriter.flush(); // write로 담은 내용 출력 후, 버퍼를 비움.

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close(); // bufferedWriter close.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	// getTodoList
	public ArrayList<TodoBean> getToDoList(TodoBean searchInfo) {
		ArrayList<TodoBean> dayList = null;
		TodoBean toDo = null;
		String line;
		BufferedReader buffer = null;
		int date, recordCount = 1;
		int[] dateRange = new int[2];

		LocalDate userDate = LocalDate.of(Integer.parseInt(searchInfo.getStartDate().substring(0, 4)),
				Integer.parseInt(searchInfo.getStartDate().substring(4)), 1);

		try {
			buffer = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIndex()])));
			while ((line = buffer.readLine()) != null) {
				if (recordCount == 1)
					dayList = new ArrayList<TodoBean>();

				String[] record = line.split(",");
				/* 계정별로 추려오기 */
				if (!searchInfo.getAccessCode().equals(record[0]))
					continue;

				date = Integer.parseInt(searchInfo.getStartDate());
				dateRange[0] = Integer.parseInt(record[1].substring(0, 8));
				dateRange[1] = Integer.parseInt(record[2].substring(0, 8));

				if (date > dateRange[0] / 100)
					dateRange[0] = Integer.parseInt(date + "01");
				if (date < dateRange[1] / 100)
					dateRange[1] = Integer.parseInt(date + "" + userDate.lengthOfMonth());

				for (int idx = dateRange[0]; idx <= dateRange[1]; idx++) {

					toDo = new TodoBean();
					toDo.setStartDate(idx + "");
					dayList.add(toDo);
				}
				recordCount++;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null)
					buffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return dayList;
	}
	
	// 특정 기간의 할일 목록 가져오기
	public ArrayList<TodoBean> getList(TodoBean searchInfo) {
		ArrayList<TodoBean> todoList = null;
		String line;
		String[] record;
		BufferedReader reader = null;
		TodoBean todoBean;
		int count = 1;
		try {
			reader = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIndex()])));

			while ((line = reader.readLine()) != null) {
				if (count == 1)
					todoList = new ArrayList<TodoBean>();
				record = line.split(",");

				if (!searchInfo.getAccessCode().equals(record[0])) {
					continue;
				}
				
				
				if (!this.isCheckRange(record[1].substring(0, 8), record[2].substring(0, 8), searchInfo.getStartDate(),
						searchInfo.getEndDate())) {
					continue;
				}

				if (searchInfo.getServiceCode().equals("12")) {
					switch (searchInfo.getIsEnable()) {
					case "0": // 휴지통
						if (!record[5].equals("0"))
							continue;
						break;

					case "1": // 활성
						if (record[5].equals("0"))
							continue;
						if (!(searchInfo.isAll())) {
							if (!searchInfo.getStatus().equals(record[4]))
								continue;
						}
					default:

						break;
					}
				}
				
				todoBean = new TodoBean();
				todoBean.setStartDate(record[1]);
				todoBean.setEndDate(record[2]);
				todoBean.setContents(record[3]);
				todoBean.setStatus(record[4]);
				todoBean.setIsEnable(record[5]);
				todoBean.setComment(record[6]);

				todoList.add(todoBean);

				count++;
			}

		} catch (FileNotFoundException e) {
			System.out.println("파일 없음");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("입출력 오류");
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return todoList;
	}
	
	//3주차
	private boolean isCheckRange(String startDay, String endDay, String compareStartDay, String compareEndDay) {
		int check = 0;

		for (int i = Integer.parseInt(startDay); i <= Integer.parseInt(endDay); i++) {
			if (Integer.parseInt(compareStartDay) == i) {
				check++;
			}
			if (Integer.parseInt(compareEndDay) == i) {
				check++;
			}
			if (Integer.parseInt(compareStartDay) < Integer.parseInt(startDay)
					&& Integer.parseInt(compareEndDay) > Integer.parseInt(endDay)) {
				check++;
			}

		}

		return check >= 1;
	}
	
	//5주차
	public boolean writeModitiedTodoList(ArrayList<TodoBean> todoBeans) {
		boolean result = false;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileInfo[2])));
			
			for(TodoBean todoBean : todoBeans) {
				
				//5주차
				/* [코드 작성 구역 5]
				 * 우리는 2, 3, 4번 문제를 통해 TODO.txt 파일을 모두 읽어오면서
				 * 수정이 필요한 내용만 수정하여 ArrayList에 담아왔습니다.
				 * 
				 * 이제 ArrayList를 풀면서 TODO.txt파일에 write를 해주어야 합니다.
				 * 코드를 작성해보세요.
				 * 
				 * 예상코드는 3~5줄 입니다.
				 * 
				 *  */
				
			}
			bufferedWriter.flush();

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	//5주차
	public ArrayList<TodoBean> readTodoData(int fileIndex) {
		TodoBean todoBean;
		ArrayList<TodoBean> todoList = null;
		BufferedReader buffer = null;
		String line = null;
		
		try {
			buffer = new BufferedReader(new FileReader(new File(fileInfo[fileIndex])));
			todoList = new ArrayList<TodoBean>();

			while ((line = buffer.readLine()) != null) {
				String[] tmp = line.split(",");
				todoBean = new TodoBean();

				// bean가져와서 데이터 넣기
				todoBean.setAccessCode(tmp[0]);
				todoBean.setStartDate(tmp[1]);
				todoBean.setEndDate(tmp[2]);
				todoBean.setContents(tmp[3]);
				todoBean.setStatus(tmp[4]);
				todoBean.setIsEnable(tmp[5]);
				todoBean.setComment(tmp[6]);

				todoList.add(todoBean);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			todoList = null; // 참조선 제거
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return todoList;
	}


}