package dat255.grupp06.bibbla.utils;

import java.io.Serializable;

public abstract class SerializableCallback extends Callback
implements Serializable {
	private static final long serialVersionUID = 4544768712188843966L;
	public abstract void handleMessage(Message msg);
}
