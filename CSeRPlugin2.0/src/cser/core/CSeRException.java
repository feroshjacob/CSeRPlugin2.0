package cser.core;



public class CSeRException  extends Exception{
	
	private boolean compilationfailed = false;

	public boolean isCompilationfailed() {
		return compilationfailed;
	}
	public void setCompilationfailed(boolean compilationfailed) {
		this.compilationfailed = compilationfailed;
	}
	public CSeRException(String string) {
		 super(string);
		 printStackTrace();
	}
	public CSeRException(String string,boolean compilationfailed) {
		 super(string);
		 this.compilationfailed = compilationfailed;
	}
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

}
