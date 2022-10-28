package server;

import java.io.*;

import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;

//To access database
public class DataAccessObject {
	//***중요***
	//******파일 경로는 환경에 따라 변경하여 사용하세요.*******
	private String[] fileInfo = { "C:\\java\\data\\review\\src\\database\\MEMBERS.txt",
			"C:\\java\\data\\review\\src\\database\\ACCESSHISTORY.txt",
			"C:\\java\\data\\review\\src\\database\\TODO.txt" };

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

}
