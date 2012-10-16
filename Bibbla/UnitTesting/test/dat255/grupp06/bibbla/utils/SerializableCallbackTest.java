package dat255.grupp06.bibbla.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

public class SerializableCallbackTest extends TestCase {

	public static final String FILE = "SerializableCallback.test.ser";
	/**
	 * Test serialization by writing object to a file and reading it back again.
	 * File is deleted afterwards. If no exceptions are thrown, serialization
	 * works fine.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void testSerialization() throws IOException, ClassNotFoundException {
		SerializableCallback c = new SerializableCallback() {
			private static final long serialVersionUID = -4852385037064234702L;
			@Override
			public void handleMessage(Message msg) {
				callbackMethod();
			}
		};

		FileOutputStream fos = new FileOutputStream(FILE);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(c);
		out.close();
		
		FileInputStream fis = new FileInputStream(FILE);
		ObjectInputStream in = new ObjectInputStream(fis);
		Object object = in.readObject();
		@SuppressWarnings("unused")
		SerializableCallback d = (SerializableCallback) object;
	}
	
	
	public void testHandleMessage() {
		fail("Not yet implemented");
	}

	private static void callbackMethod() {}
}
