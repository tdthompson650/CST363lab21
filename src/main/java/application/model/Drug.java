package application.model;

import org.springframework.data.annotation.Id;

public class Drug {
	
	@Id
	private int id;
	private String name;
	
	
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

	@Override
	public String toString() {
		return "Drug [id=" + id + ", name=" + name + "]";
	}

}
