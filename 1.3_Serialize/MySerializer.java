import java.io.*;

public class MySerializer {
	private MySerializableClass mySerializableClass;
	
	MySerializer(MySerializableClass serializableClass) {
		mySerializableClass=serializableClass;
	}
	
	private String readFilename() throws IOException {
		String filename;
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in )); 
		
		System.out.print("filename> ");
		filename=reader.readLine();
		
		return filename;
	}
	
	public void write(String text) {
		mySerializableClass.set(text);
		try {
			String filename = readFilename();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(mySerializableClass);
			oos.close();
		} catch (Exception e) {
			System.out.println("Fehler");
		}
		
	}
	
	public String read() throws IOException, ClassNotFoundException {
		String filename=readFilename();

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
		mySerializableClass = (MySerializableClass)ois.readObject();
		ois.close();
		
		return mySerializableClass.toString();
	}
} 
	