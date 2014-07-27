package com.example.message;

public class ListItem {
	private long id = 0;
	private String question = null;
	private String hash = null;
	private String aid = null;
	private String answer = null;
	private String datetime = null;
	private String thumb_up_count = null;
	private String thumb_down_count = null;
	private String up_icon_color = null;
	private String down_icon_color = null;
	private int thumb_up_icon = 0;
	private int thumb_down_icon = 0;
	
	public long getId() { return id; }
	public String getQuestion() { return question; }
	public String getHash() { return hash; }
	public String getAid() { return aid; }
	public String getAnswer() { return answer; }
	public String getDatetime() { return datetime; }
	public String getThumbUpCount() { return thumb_up_count; }
	public String getThumbDownCount() { return thumb_down_count; }
	public String getUpIconColor() { return up_icon_color; }
	public String getDownIconColor() { return down_icon_color; }
	public int getThumbUpIcon() { return thumb_up_icon; }
	public int getThumbDownIcon() { return thumb_down_icon; }


	public void setId(long id) { this.id = id; }
	public void setQuestion(String question) { this.question = question; }
	public void setHash(String hash) { this.hash = hash; }
	public void setAid(String aid) { this.aid = aid; }
	public void setAnswer(String answer) { this.answer = answer; }
	public void setDatetime(String datetime) { this.datetime = datetime; }
	public void setThumbUpCount(String thumb_up_count) { this.thumb_up_count = thumb_up_count; }
	public void setThumbDownCount(String thumb_down_count) { this.thumb_down_count = thumb_down_count; }
	public void setUpIconColor(String up_icon_color) { this.up_icon_color = up_icon_color; }
	public void setDownIconColor(String down_icon_color) { this.down_icon_color = down_icon_color; }
	public void setThumbUpIcon(int thumb_up_icon) { this.thumb_up_icon = thumb_up_icon; }
	public void setThumbDownIcon(int thumb_down_icon) { this.thumb_down_icon = thumb_down_icon; }

}
