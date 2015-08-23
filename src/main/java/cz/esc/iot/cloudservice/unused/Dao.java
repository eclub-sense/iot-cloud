package cz.esc.iot.cloudservice.unused;

import cz.esc.iot.cloudservice.unused.sensors.SensorType;

public interface Dao<T> {

    public int create(T zettaDriver);

    public T read(SensorType id);

    public boolean update(T zettaDriver);

    public boolean delete(T zettaDriver);
}
