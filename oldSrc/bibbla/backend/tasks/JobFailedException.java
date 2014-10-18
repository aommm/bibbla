package dat255.grupp06.bibbla.backend.tasks;

public class JobFailedException extends Exception {
	private static final long serialVersionUID = -7838958510786092023L;
	public JobFailedException() {
		super();
	}
	public JobFailedException(String detailMessage) {
		super(detailMessage);
	}
	public JobFailedException(Throwable throwable) {
		super(throwable);
	}
	public JobFailedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
