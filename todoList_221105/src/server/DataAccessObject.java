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

}