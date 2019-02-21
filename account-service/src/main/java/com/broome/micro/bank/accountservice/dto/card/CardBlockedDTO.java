package com.broome.micro.bank.accountservice.dto.card;

public class CardBlockedDTO {
	
	private boolean isBlocked;

	public CardBlockedDTO() {}
	public CardBlockedDTO(boolean isBlocked) {
		super();
		this.isBlocked = isBlocked;
	}
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	
	
	
	

}
