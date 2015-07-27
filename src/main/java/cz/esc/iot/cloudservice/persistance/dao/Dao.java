package cz.esc.iot.cloudservice.persistance.dao;

import cz.esc.iot.cloudservice.sensors.SensorType;

public interface Dao<T> {

    public int create(T zettaDriver);

    public T read(SensorType id);

    public boolean update(T zettaDriver);

    public boolean delete(T zettaDriver);
}
