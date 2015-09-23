package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.query.Query;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.messages.Postman;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.Action;
import cz.esc.iot.cloudservice.persistance.model.Data;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.MeasureValue;
import cz.esc.iot.cloudservice.persistance.model.Parameter;
import cz.esc.iot.cloudservice.persistance.model.SensorAccessEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.siren.Actions;
import cz.esc.iot.cloudservice.support.AllSensors;
import cz.esc.iot.cloudservice.support.DataList;
import cz.esc.iot.cloudservice.support.SensorAndData;
import cz.esc.iot.cloudservice.support.WebSocketRegistry;

/**
 * Return list of registered sensors. Only sensors owned by signed in user are returned.
 */
public class RegisteredSensors extends ServerResource {
	
	/**
	 * Writes to sensor.
	 * @throws IOException
	 */
	@Post
	public void writeToSensor(Representation entity) throws IOException {
		
		// get access_token from url parameters
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String access_token = form.getFirstValue("access_token");
		if (access_token == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		}
			
		// verify user
		UserEntity user;
		if ((user = OAuth2.findUserInDatabase(access_token)) == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		}
		
		String uuid = (String)this.getRequestAttributes().get("uuid");
		
		// finds sensor in database
		SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(uuid).get();

		if ((sensor != null) && (!sensor.getUser().getId().equals(user.getId()))) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		} else if (sensor == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return;
		}
		
		// deserialization of json object
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = entity.getText();
		Action action = gson.fromJson(json, Action.class);
		
		// do the action
		ClientResource client = new ClientResource("http://127.0.0.1:1337/servers/" + sensor.getHub().getUuid() + "/devices/" + sensor.getUuid());
		Form formular = new Form();
		//formular.set("action", action.getName());
		for (Parameter param : action.getFields()) {
			formular.set(param.getName(), param.getValue());
		}
		client.post(formular);
	}
	
	/**
	 * Deletes registered sensor.
	 * @throws IOException
	 */
	@Delete
	public void acceptRepresentation(Representation entity) throws IOException {
		
		// get access_token from url parameters
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String access_token = form.getFirstValue("access_token");
		if (access_token == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		}
			
		// verify user
		UserEntity user;
		if ((user = OAuth2.findUserInDatabase(access_token)) == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		}

		String uuid = (String)this.getRequestAttributes().get("uuid");
		
		// finds sensor in database
		SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(uuid).get();

		if ((sensor != null) && (!sensor.getUser().getId().equals(user.getId()))) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		} else if (sensor == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return;
		}
		
		// delete access control list associated with sensor
		MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().find(SensorAccessEntity.class).field("sensor").equal(sensor));
		
		// delete sensor from database
		MorfiaSetUp.getDatastore().delete(sensor);
		
		WebSocket socket = WebSocketRegistry.get(sensor.getHub().getUuid());
		
		// send message to hub
		Postman.deleteSensor(socket, sensor);
	}
	
	/**
	 * Identifies user and return his sensors.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Get("json")
	public String returnList() throws IOException, ParseException {
		
		// get access_token from url parameters
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String access_token = form.getFirstValue("access_token");
			
		// verify user
		UserEntity userEntity = null;
		
		if (access_token == null) {
		} else if ((userEntity = OAuth2.findUserInDatabase(access_token)) == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return "";
		}
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
		
		String path = this.getRequest().getResourceRef().getPath();
		switch (path) {
		// returns all user's sensors
		case "/registered_sensors" : return registeredSensors(gson, form, userEntity);
		// returns only sensor specified by its uuid
		default : return sensorAndData(gson, form, userEntity);
		}
	}
	
	/**
	 * @return Returns JSON where is info about sensor and it's measured data.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private String sensorAndData(Gson gson, Form form, UserEntity userEntity) throws IOException, ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date from = null;
		Date to = null;
		
		for (org.restlet.data.Parameter parameter : form) {
			if (parameter.getName().equals("from")) {
				if (parameter.getValue().charAt(0) == '-')
					from = new Date(new Date().getTime() + Long.parseLong(parameter.getValue()));
				else
					from = formatter.parse(parameter.getValue());
			} else if (parameter.getName().equals("to")) {
				to = formatter.parse(parameter.getValue());
			}
		}
		
		SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(this.getRequestAttributes().get("uuid")).get();
		SensorAndData ret = new SensorAndData();
		
		if (sensor == null) {
			return null;
		} else if (userEntity != null && sensor.getAccess().equals("private") && sensor.getUser().getId().equals(userEntity.getId())) {
			ret.setSensor(sensor);
			ret.setOrigin("my");
		} else if (userEntity != null && sensor.getAccess().equals("protected")) {
			if (sensor.getUser().getId().equals(userEntity.getId())) {
				ret.setSensor(sensor);
				ret.setOrigin("my");
			} else {
				SensorAccessEntity access = MorfiaSetUp.getDatastore().createQuery(SensorAccessEntity.class).field("sensor").equal(sensor).field("user").equal(userEntity).get();
				if (access == null) {
					getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
					return "";
				}
				ret.setSensor(sensor);
				ret.setOrigin("borrowed");
				ret.setPermission(access.getPermission());
			}
		} else if (sensor.getAccess().equals("public")) {
			ret.setOrigin("public");
			ret.setSensor(sensor);
		} else {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return "";
		}
		
		WebSocket socket = WebSocketRegistry.get(sensor.getHub().getUuid());
		if (socket != null) {
			// set possible actions
			ClientResource client = new ClientResource("http://127.0.0.1:1337/servers/" + sensor.getHub().getUuid() + "/devices/" + sensor.getUuid());
			Representation rep = client.get();
			String siren = rep.getText();
			Actions actions = gson.fromJson(siren, Actions.class);
			ret.setActions(actions.getActions());
		} else 
			ret.setActions(null);
		// set measured values
		// TODO get measuring values from siren not from database
		SensorTypeInfo info = MorfiaSetUp.getDatastore().createQuery(SensorTypeInfo.class).field("type").equal(sensor.getType()).get();
		for (MeasureValue value : info.getValues()) {
			
			List<Data> list;
			if (from != null && to != null)
				list = MorfiaSetUp.getDatastore().createQuery(Data.class).field("sensor").equal(sensor).field("name").equal(value.getName()).field("time").greaterThanOrEq(from).field("time").lessThanOrEq(to).asList();
			else if (from != null)
				list = MorfiaSetUp.getDatastore().createQuery(Data.class).field("sensor").equal(sensor).field("name").equal(value.getName()).field("time").greaterThanOrEq(from).asList();
			else if (to != null)
				list = MorfiaSetUp.getDatastore().createQuery(Data.class).field("sensor").equal(sensor).field("name").equal(value.getName()).field("time").lessThanOrEq(to).asList();
			else
				list = MorfiaSetUp.getDatastore().createQuery(Data.class).field("sensor").equal(sensor).field("name").equal(value.getName()).asList();
				
			String ws = "ws://mlha-139.sin.cvut.cz:1337/servers/" + sensor.getHub().getUuid() + "/" + "events?topic=" + sensor.getType() + "%2F" + sensor.getUuid() + "%2F" + value.getName();
			ret.addDataList(new DataList(value.getName(), list, ws));
		}

		return gson.toJson(ret);
	}
	
	/**
	 * Reads parameters and filter result according to them.
	 */
	private String registeredSensors(Gson gson, Form form, UserEntity userEntity) {
		
		String hubID = null;
		String origin = null;
		int type = -1;
		
		HubEntity hub = null;
		
		for (org.restlet.data.Parameter parameter : form) {
			if (parameter.getName().equals("hubID")) {
				hubID = parameter.getValue();
				hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(userEntity).field("uuid").equal(hubID).get();
				if (hubID == null) {
					getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
					return "";
				}
			} else if (parameter.getName().equals("origin")) {
				origin = parameter.getValue();
			} else if (parameter.getName().equals("type")) {
				type = Integer.parseInt(parameter.getValue());
			}
		}
		
		AllSensors sensors = new AllSensors();
		if (userEntity == null) {
			List<SensorEntity> _public = publicQuery(hub, userEntity, type).asList();
			sensors.set_public(_public);
			return gson.toJson(sensors);
		}
		if (origin != null) {
			if (origin.equals("my"))
				sensors.setMy(myQuery(hub, userEntity, type).asList());
			else if (origin.equals("borrowed"))
				sensors.setBorrowed(borrowedQuery(hub, userEntity, type).asList());
			else if (origin.equals("public"))
				sensors.set_public(publicQuery(hub, userEntity, type).asList());
		} else {
			sensors.setMy(myQuery(hub, userEntity, type).asList());
			sensors.setBorrowed(borrowedQuery(hub, userEntity, type).asList());
			sensors.set_public(publicQuery(hub, userEntity, type).asList());
		}
		return gson.toJson(sensors);
	}
	
	private Query<SensorEntity> myQuery(HubEntity hub, UserEntity userEntity, int type) {
		Query<SensorEntity> myQuery = null;
		if (hub == null && type == -1)
			myQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity);
		else if (hub != null && type == -1)
			myQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).field("hub").equal(hub);
		else if (hub == null && type != -1)
			myQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).field("type").equal(type);
		else
			myQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).field("type").equal(type).field("hub").equal(hub);
		return myQuery;
	}
	
	private Query<SensorAccessEntity> borrowedQuery(HubEntity hub, UserEntity userEntity, int type) {
		List<SensorEntity> protectedSensors = null;
		if (hub == null && type == -1)
			protectedSensors = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("access").equal("protected").asList();
		else if (hub != null && type == -1)
			protectedSensors = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("access").equal("protected").field("hub").equal(hub).asList();
		else if (hub == null && type != -1)
			protectedSensors = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("access").equal("protected").field("type").equal(type).asList();
		else
			protectedSensors = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("access").equal("protected").field("type").equal(type).field("hub").equal(hub).asList();
		return MorfiaSetUp.getDatastore().createQuery(SensorAccessEntity.class).field("user").equal(userEntity).field("sensor").in(protectedSensors);
	}
	
	private Query<SensorEntity> publicQuery(HubEntity hub, UserEntity userEntity, int type) {
		Query<SensorEntity> publicQuery = null;
		if (hub == null && type == -1)
			publicQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").notEqual(userEntity).field("access").equal("public");
		else if (hub != null && type == -1)
			publicQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").notEqual(userEntity).field("access").equal("public").field("hub").equal(hub);
		else if (hub == null && type != -1)
			publicQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").notEqual(userEntity).field("access").equal("public").field("type").equal(type);
		else
			publicQuery = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").notEqual(userEntity).field("access").equal("public").field("type").equal(type).field("hub").equal(hub);
		return publicQuery;
	}
}
