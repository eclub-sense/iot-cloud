package cz.esc.iot.cloudservice.persistance.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;

@Entity
public class RefreshToken {

    @Id
    private ObjectId id;
	@Expose private String refresh_token;
	@Indexed(expireAfterSeconds = 90) // 14 days
	@Expose private Date time;
	@Expose(deserialize = false) private int refreshCounter = 0; // refresh_token is deleted from database when refreshCounter == 5
	@Reference
	private UserEntity user;
	
	public RefreshToken() {
		super();
	}
	public RefreshToken(String rt, Date t) {
		refresh_token = rt;
		time = t;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String access_token) {
		this.refresh_token = access_token;
	}
	public int getRefreshCounter() {
		return refreshCounter;
	}
	public void setRefreshCounter(int refreshCounter) {
		this.refreshCounter = refreshCounter;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
}