package cz.esc.iot.cloudservice.persistance.model;

public class ZettaDriver {
    
    private int type;
    private byte[] data;

    public ZettaDriver() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
