package test.refactor;

public class Group {
	
	private String userName;
	private String passWord;
	public String getUserName() {
		return userName;
	}
	/*
	 * case 1
	 */
	public void Change(String userName) {
		setUserName(userName);
		int k =0;
		setPassWord("");
		
	}
	
	
	/*
	 * Case 2
	 */
	public boolean authenticate(String userName,String password){
		if(this.userName.equals(userName) && this.passWord.equals(password))return true;
		return false;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

}
