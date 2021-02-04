package favodelix;

import java.io.Serializable;

public class Data implements Serializable {

	private String number;
	private String channelid;
	private String channelname;

	public Data() {
	}

	public Data(String number, String channelid, String channelname) {
		super();
		this.number = number;
		this.channelid = channelid;
		this.channelname = channelname;

	}


	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Data [number=");
		builder.append(number);
		builder.append(", channelid=");
		builder.append(channelid);
		builder.append(", channelname=");
		builder.append(channelname);
		builder.append("]");
		return builder.toString();
	}

}
