package cz.esc.iot.cloudservice.persistance.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.esc.iot.cloudservice.persistance.dao.imp.AppZip;
import cz.esc.iot.cloudservice.persistance.dao.imp.SensorTypeInfoDaoImpl;

public class SensorTypeInfo {
    
    private int type;
    private String type_name;
    private List<MeasureValue> values;
    private String driver_path;
    private String driver_name;
    private transient ZettaDriver zettaDriver;
    
    public SensorTypeInfo() {
    	super();
    }
    
    public SensorTypeInfo(int type, String type_name, List<MeasureValue> values, String path, String driver_name) {
		super();
		this.type = type;
		this.type_name = type_name;
		this.values = values;
		this.driver_path = path;
		this.driver_name = driver_name;
	}
    
    public int getType() {
        return type;
    }

    public String getTypeName() {
		return type_name;
	}

	public List<MeasureValue> getValues() {
		return values;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setTypeName(String type_name) {
		this.type_name = type_name;
	}

	public void setValues(List<MeasureValue> values) {
		this.values = values;
	}

	public String getPath() {
		return driver_path;
	}

	public void setPath(String path) {
		this.driver_path = path;
	}

	public String getDriverName() {
		return driver_name;
	}

	public void setDriverName(String driver_name) {
		this.driver_name = driver_name;
	}

	public ZettaDriver getDriver() {
		return zettaDriver;
	}

	public boolean setDriver() {

        Path zipFile = Paths.get(driver_path);
        zettaDriver = new ZettaDriver();
        if (Files.isRegularFile(zipFile)) {
            try {
                zettaDriver.setData(Files.readAllBytes(zipFile));
                return true;
            } catch (IOException ex) {
                Logger.getLogger(SensorTypeInfoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    Files.delete(zipFile);
                } catch (IOException ex1) {
                    Logger.getLogger(SensorTypeInfoDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
                    return false;
                }
            }
        }

        AppZip appZip = new AppZip();
        File file = Paths.get(driver_path).toFile();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(SensorTypeInfoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        appZip.generateFileList(new File(driver_path.substring(0, driver_path.length() - 4)));
        appZip.zipIt(driver_path);

        try {
            zettaDriver.setData(Files.readAllBytes(file.toPath()));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SensorTypeInfoDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
	}

	@Override
	public String toString() {
		return "SensorTypeInfo [type=" + type + ", type_name=" + type_name + ", values=" + values + ", path=" + driver_path
				+ ", driver_name=" + driver_name + "]";
	}
}
