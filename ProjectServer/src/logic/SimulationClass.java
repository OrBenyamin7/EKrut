package logic;

public class SimulationClass {
	
	
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String role;
	private String email;
	private String phoneNumber;
	private String id;
	private String location;
	private String status;
	private String memberNumber;
	private String isAMember;
	private String creditCardNumber;
	private String expirationDate;
	private String cvv;
	private String debt;
	
	public SimulationClass(String userName, String password, String firstName, String lastName, String role,
			String email, String phoneNumber, String id, String location, String status, String memberNumber,
			String isAMember, String creditCardNumber, String expirationDate, String cvv, String debt) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.id = id;
		this.location = location;
		this.status = status;
		this.memberNumber = memberNumber;
		this.isAMember = isAMember;
		this.creditCardNumber = creditCardNumber;
		this.expirationDate = expirationDate;
		this.cvv = cvv;
		this.debt = debt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getIsAMember() {
		return isAMember;
	}

	public void setIsAMember(String isAMember) {
		this.isAMember = isAMember;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getDebt() {
		return debt;
	}

	public void setDebt(String debt) {
		this.debt = debt;
	}

	@Override
	public String toString() {
		return "SimulationClass [userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", role=" + role + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", id=" + id + ", location=" + location + ", status=" + status + ", memberNumber=" + memberNumber
				+ ", isAMember=" + isAMember + ", creditCardNumber=" + creditCardNumber + ", expirationDate="
				+ expirationDate + ", cvv=" + cvv + ", debt=" + debt + "]";
	}
	
	
	
	
}
