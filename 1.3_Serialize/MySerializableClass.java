import java.io.Serializable;

public class MySerializableClass implements Serializable{
	private static final long serialVersionUID=1;
	private int id;
	private String string;
	private transient MyNonSerializableClass myNonSerializableClass;

	MySerializableClass(MyNonSerializableClass myNonSerializableClass) {
		id=1234;
		this.myNonSerializableClass = myNonSerializableClass;
	}
	
	public void set(String string) {
		this.string=string;
	}

	@Override
	public String toString() {
		return "MySerializableClass{" +
				"id=" + id +
				", string='" + string + '\'' +
				", myNonSerializableClass=" + myNonSerializableClass +
				'}';
	}
}
	