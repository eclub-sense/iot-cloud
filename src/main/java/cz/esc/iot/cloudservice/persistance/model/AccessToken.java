package cz.esc.iot.cloudservice.persistance.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;

@Entity
public class AccessToken {

    @Id
    private ObjectId id;
	@Expose private String access_token;
	@Indexed(expireAfterSeconds = 3600) // one hour
	@Expose private Date time;
	@Reference
	private UserEntity user;
	
	public AccessToken(String at, Date d) {
		access_token = at;
		time = d;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
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
