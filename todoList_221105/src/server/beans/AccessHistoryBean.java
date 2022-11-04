package server.beans;

public class AccessHistoryBean {
	//AccessHistoryBean 은 ACCESSHISTORY.txt파일에 저장할 정보를 입력하기 위한 bean입니다.
	//bean의 모든 field는 private으로 설정해야하며, 데이터의 저장은 set~, 데이터를 불러올때는 get~을 활용합니다.
	//get~ 와 set~ method를 만들어보세요.
	
	private int fileIndex;
	private String accessCode;
	private String accessDate;
	private int accessType;

	public int getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(int fileIndex) {
		this.fileIndex = fileIndex;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(String accessDate) {
		this.accessDate = accessDate;
	}

	public int getAccessType() {
		return accessType;
	}

	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}
}