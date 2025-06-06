package application.model;

import org.springframework.data.annotation.Id;

import view.PatientView;

public class Patient {
	
	@Id
	private int id;  // unique id  
	private String lastName;
	private String firstName;
	private String birthdate;  // yyyy-mm-dd
	private String ssn;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String primaryName; 
	
	public static Patient fromView(PatientView pv) {
		Patient pd = new Patient();
		pd.setBirthdate(pv.getBirthdate());
		pd.setCity(pv.getCity());
		pd.setFirstName(pv.getFirstName());
		pd.setId(pv.getId());
		pd.setLastName(pv.getLastName());
		pd.setPrimaryName(pv.getPrimaryName());
		pd.setSsn(pv.getSsn());
		pd.setState(pv.getState());
		pd.setStreet(pv.getStreet());
		pd.setZipcode(pv.getZipcode());
		return pd;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getPrimaryName() {
		return primaryName;
	}
	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}

	@Override
	public String toString() {
		return "PatientData [id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", birthdate="
				+ birthdate + ", ssn=" + ssn + ", street=" + street + ", city=" + city + ", state=" + state
				+ ", zipcode=" + zipcode + ", primaryName=" + primaryName + "]";
	}
	
	
}
