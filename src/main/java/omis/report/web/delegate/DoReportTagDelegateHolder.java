package omis.report.web.delegate;

/**
 * Holder for delegate to do report tag.
 * 
 * <p>Only a single instance of this class can exist.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Nov 1, 2017)
 * @since OMIS 3.0
 */
public final class DoReportTagDelegateHolder {

	private static DoReportTagDelegateHolder instance;
	
	private final DoReportTagDelegate doReportTagDelegate;
	
	/**
	 * Instantiates holder for delegate to do report tag.
	 * 
	 * <p>Only a single instance of holder can exist - if the instance exists,
	 * an exception will be thrown.
	 * 
	 * @param doReportTagDelegate delegate to hold
	 */
	public DoReportTagDelegateHolder(
			final DoReportTagDelegate doReportTagDelegate) {
		if (DoReportTagDelegateHolder.instance != null) {
			throw new IllegalStateException("Instance exists");
		}
		this.doReportTagDelegate = doReportTagDelegate;
	}
	
	/**
	 * Returns instance.
	 * 
	 * <p>If instance does not exist, throw exception.
	 * 
	 * @return instance
	 */
	public static DoReportTagDelegateHolder getInstance() {
		if (DoReportTagDelegateHolder.instance == null) {
			throw new IllegalStateException("Instance does not exist");
		}
		return DoReportTagDelegateHolder.instance;
	}
	
	/**
	 * Returns do report tag delegate.
	 * 
	 * @return do report tag delegate
	 */
	public DoReportTagDelegate getDoReportTagDelegate() {
		return this.doReportTagDelegate;
	}
}