package application.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;

public class Pharmacy {
	
	@Id
	int id;
	String name;
	String address;
	String phone;
	ArrayList<DrugCost> drugCosts = new ArrayList<>();
	
	public static class DrugCost {
		String drugName;
		double cost;
		
		public String getDrugName() {
			return drugName;
		}
		public void setDrugName(String drugName) {
			this.drugName = drugName;
		}
		public double getCost() {
			return cost;
		}
		public void setCost(double cost) {
			this.cost = cost;
		}
		@Override
		public String toString() {
			return "DrugCost [drugName=" + drugName + ", cost=" + cost + "]";
		}
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public ArrayList<DrugCost> getDrugCosts() {
		return drugCosts;
	}
	public void setDrugCosts(ArrayList<DrugCost> drugCosts) {
		this.drugCosts = drugCosts;
	}
	@Override
	public String toString() {
		return "Pharmacy [id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone + ", drugCosts="
				+ drugCosts + "]";
	}
}
