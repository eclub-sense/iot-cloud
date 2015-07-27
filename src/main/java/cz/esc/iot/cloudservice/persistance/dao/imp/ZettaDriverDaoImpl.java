package cz.esc.iot.cloudservice.persistance.dao.imp;

import cz.esc.iot.cloudservice.persistance.dao.ZettaDriverDao;
import cz.esc.iot.cloudservice.persistance.model.ZettaDriver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ZettaDriverDaoImpl implements ZettaDriverDao {

    @Override
    public int create(ZettaDriver zettaDriver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ZettaDriver read(int id) {

        ZettaDriver zettaDriver = new ZettaDriver();
        zettaDriver.setType(id);

        if (!Files.isDirectory(Paths.get("drivers/" + id))) {
            return null;
        }
        Path zipFile = Paths.get("drivers/" + id + ".zip");

        if (Files.isRegularFile(zipFile)) {
            try {
                zettaDriver.setData(Files.readAllBytes(zipFile));
                return zettaDriver;
            } catch (IOException ex) {
                Logger.getLogger(ZettaDriverDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    Files.delete(zipFile);
                } catch (IOException ex1) {
                    Logger.getLogger(ZettaDriverDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
                    return null;
                }
            }
        }
        
        //Zip folder {id}
        
        File folder = Paths.get("drivers/" + id).toFile();
        File[] files = folder.listFiles();
        
        
    }

    @Override
    public boolean update(ZettaDriver zettaDriver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(ZettaDriver zettaDriver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
