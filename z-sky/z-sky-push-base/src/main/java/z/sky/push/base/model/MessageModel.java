package z.sky.push.base.model;

import java.io.Serializable;
import java.util.List;

/**
 * 消息体
 * @author jianming.zhou
 *
 */
public class MessageModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String msg;
	private List<String> users;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	@Override
	public String toString() {
		return "MessageModel [msg=" + msg + ", users=" + users + "]";
	}

}
