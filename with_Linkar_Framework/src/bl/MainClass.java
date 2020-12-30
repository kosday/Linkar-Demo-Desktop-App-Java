package bl;

import java.util.ArrayList;

// Is a Base Class for the data classes. Define some common features
public class MainClass {
	
	public ArrayList<String> LstErrors;

		// The status property
		public enum StatusTypes { READED, NEW, MODIFY, DELETED, NONE };
		private StatusTypes _status = StatusTypes.NONE;
		
		public StatusTypes getStatus() {
			return _status;
		}
		public void setStatus(StatusTypes value) {
			_status = value;
		}		
		// --

	    // Original Item content for use in optimistic blocks		    
	    private String _ItemOriginalContent;

	    public String getItemOriginalContent()
	    {
	        return _ItemOriginalContent;
	    }
	    public void setItemOriginalContent(String value)
	    {
	        _ItemOriginalContent = value;
	    }
	    // --


	    // Original Item Itypes content for use in cancel operations
	    private String _ItemOriginalContentItypes;

	    public String getItemOriginalContentItypes()
	    {
	        return _ItemOriginalContentItypes;
	    }
	    public void setItemOriginalContentItypes(String value)
	    {
	        _ItemOriginalContentItypes = value;
	    }
	    // --
	
}
