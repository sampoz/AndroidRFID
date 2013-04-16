package com.example.arfid;

public class IDCard {
	int id;
	String cardid;
	String userid;
	public IDCard(int i, String cardid, String userid){
		this.id=i;
		this.cardid=cardid;
		this.userid=userid;
	}
	public IDCard(){
		
	}
	protected int getId() {
		return id;
	}
	protected void setId(int id) {
		this.id = id;
	}
	protected String getCardid() {
		return cardid;
	}
	protected void setCardid(String cardid) {
		this.cardid = cardid;
	}
	protected String getUserid() {
		return userid;
	}
	protected void setUserid(String userid) {
		this.userid = userid;
	}
	

}
