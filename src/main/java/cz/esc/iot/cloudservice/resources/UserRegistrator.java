package cz.esc.iot.cloudservice.resources;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

public class UserRegistrator {
	
	public static void registerUser(String username) {
		UserEntity user = new UserEntity();
		user.setIdentifier(username);
		MorfiaSetUp.getDatastore().save(user);
	}
}
