package dat255.grupp06.bibbla.backend;

/**
 * 
 * @author Fahad
 *
 */

public class BackendFactory {
	
	private static IBackend backendObject;
	
	/**
	 * Creates and returns Backend object(Singleton).
	 * @return Backend object
	 */
	public static IBackend getBackend(){
		if(backendObject == null){
			backendObject = new Backend();
		}
		
		return backendObject;
	}

}
