package server.beans;

public class MemberBean {

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