package cz.esc.iot.cloudservice.support;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * New UserEntity object is stored into database after Google user's
 * first visit of /login url.
 */
public class UserRegistrator {
	
	public static void registerUser(String id, String username) {
		UserEntity user = new UserEntity();
		user.setIdentifier(id);
		user.addEmail(username);
		MorfiaSetUp.getDatastore().save(user);
	}
}
