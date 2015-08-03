package cz.esc.iot.cloudservice.persistance.dao.imp;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.ExampleA;
import cz.esc.iot.cloudservice.persistance.model.ExampleB;
import cz.esc.iot.cloudservice.persistance.model.ExampleC;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorfiaExampleTest {

    public MorfiaExampleTest() {
    }

    @Ignore
    public void testInsert() {

        final Morphia morphia = MorfiaSetUp.getMorphia();
        final Datastore datastore = MorfiaSetUp.getDatastore();

        ExampleA exampleA = new ExampleA();
        ExampleB exampleB = new ExampleB();
        ExampleC exampleC_0 = new ExampleC();
        ExampleC exampleC_1 = new ExampleC();
        exampleB.list = new LinkedList<>();

        exampleB.list.add(exampleC_0);
        exampleB.list.add(exampleC_1);
        datastore.save(exampleC_0);
        datastore.save(exampleC_1);

        System.out.println("exampleA.id: " + exampleA.id);
        datastore.save(exampleA);
        System.out.println("exampleA.id: " + exampleA.id);
        System.out.println("exampleB.id: " + exampleA.id);
        datastore.save(exampleB);
        System.out.println("exampleB.id: " + exampleA.id);
    }

    @Ignore
    public void testFind() {
        final Morphia morphia = MorfiaSetUp.getMorphia();
        final Datastore datastore = MorfiaSetUp.getDatastore();

//        List<ExampleA> eas = datastore.createQuery(ExampleA.class).asList();
        List<ExampleB> ebs = datastore.createQuery(ExampleB.class).asList();
//        List<ExampleC> ecs = datastore.createQuery(ExampleC.class).asList();
        System.out.println("Result0: " + ebs);
        System.out.println("Result1: " + datastore.createQuery(ExampleB.class).filter("string =", "text").get());
    }
    
    @Test
    public void test(){
        final Morphia morphia = MorfiaSetUp.getMorphia();
        final Datastore datastore = MorfiaSetUp.getDatastore();
        
        UserEntity userEntity0 = new UserEntity();
        userEntity0.setUsername("user0");
        userEntity0.setPassword("pass0");
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUsername("user1");
        userEntity1.setPassword("pass1");
        
        datastore.save(userEntity0, userEntity1);
        
        SensorEntity sensorEntity = new SensorEntity();
        sensorEntity.setUuid(1);
        sensorEntity.setType(1);
        
        datastore.save(sensorEntity);
        
        UserEntity tmp = datastore.createQuery(UserEntity.class).field("username").equal("user0").get();
        if(tmp == null){
            System.err.println("Error 0.");
            return;
        }
        
        Collection<SensorEntity> sensorEntities = tmp.getSensorEntities();
        if(sensorEntities == null){
            System.err.println("Error 1.");
            return;
        }
        sensorEntities.add(sensorEntity);
        
        datastore.update(tmp, datastore.createUpdateOperations(UserEntity.class).add("sensorEntities", sensorEntity));
    }
}
