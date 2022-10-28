package server.beans;

public class MemberBean {
	// AccessHistoryBean 은 ACCESSHISTORY.txt파일에 저장할 정보를 입력하기 위한 bean입니다.
	// bean의 모든 field는 private으로 설정해야하며, 데이터의 저장은 set~, 데이터를 불러올때는 get~을 활용합니다.
	// get~ 와 set~ method를 만들어보세요.

	private int fileIndex;
	private String accessCode;
	private String secretCode;
	private String name;
	private String phoneNumber;
	private int activation;

	public int getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(int fileindex) {
		this.fileIndex = fileindex;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getActivation() {
		return activation;
	}

	public void setActivation(int activation) {
		this.activation = activation;
	}

}
