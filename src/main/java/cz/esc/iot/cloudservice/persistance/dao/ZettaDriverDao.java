package cz.esc.iot.cloudservice.persistance.dao;

import cz.esc.iot.cloudservice.persistance.model.ZettaDriver;

public interface ZettaDriverDao {

    public int create(ZettaDriver zettaDriver);

    public ZettaDriver read(int id);

    public boolean update(ZettaDriver zettaDriver);

    public boolean delete(ZettaDriver zettaDriver);
}
