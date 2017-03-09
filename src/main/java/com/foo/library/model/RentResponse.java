package com.foo.library.model;

public class RentResponse {
	private Rent rent;
	private Boolean isSuccess;
	private String message;

	public RentResponse()
	{
		super();
	}
	
	public RentResponse(Rent rent)
	{
		this.rent = rent;
		this.isSuccess = true;
	}
	
	public void markAsFailed(String message)
	{
		this.message = message;
		this.isSuccess = false;
	}
	
	public Rent getRent() {
		return rent;
	}

	public void setRent(Rent rent) {
		this.rent = rent;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
