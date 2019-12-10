package com.fekrety.fekretyonline.Model;

public class item {
	  private Integer id ;
	  private String name ;
	  private String descrpition ;
	  private String address ;
	  private double rate ;
	  private String photo ;

	public item(Integer id, String name, String descrpition, String address, double rate, String photo) {
		this.id = id;
		this.name = name;
		this.descrpition = descrpition;
		this.address = address;
		this.rate = rate;
		this.photo = photo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescrpition() {
		return descrpition;
	}

	public String getAddress() {
		return address;
	}

	public double getRate() {
		return rate;
	}

	public String getPhoto() {
		return photo;
	}
}
