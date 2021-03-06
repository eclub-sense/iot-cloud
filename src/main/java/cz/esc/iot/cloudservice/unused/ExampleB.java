package cz.esc.iot.cloudservice.unused;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class ExampleB extends ExampleA implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String string = "text";
    public String[] array = {"1", "2", "3"};
    @Reference
    public List<ExampleC> list;

    @Override
    public String toString() {
        return "ExampleB{" + "string=" + string + ", array=" + Arrays.deepToString(array) + ", list=" + list + '}';
    }
}
