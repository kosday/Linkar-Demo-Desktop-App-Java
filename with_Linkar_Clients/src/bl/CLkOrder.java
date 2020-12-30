package bl;
import java.util.*;

import linkar.LinkarDataTypes;
import linkar.LkException;
import linkar.MainClass;
import linkar.ASCII_Chars;
import linkar.DBMV_Mark;
import linkar.DeleteOptions;
//import linkar.GenericError;
import linkar.LinkarClt;
import linkar.LinkarClt.DATAFORMATCRU_TYPE;
import linkar.LinkarClt.DATAFORMAT_TYPE;
import linkar.MvFunctions;
import linkar.NewOptions;
import linkar.ReadOptions;
import linkar.RecordIdType;
import linkar.UpdateOptions;

public class CLkOrder extends MainClass {

	// --- Properties

	//The copy of the client for operations
	LinkarClt _LinkarClt = null;
	
	//The name for the file in the data base
    public final static String FILE_CLkOrder = "LK.ORDERS";

    // CODE Porperties
	private String _Code;

	public String getCode()
	{
		return _Code;	
	}

	public void setCode(String value) {
		_Code = value;
	}

	public static String getDICT_Code()
	{
		return "CODE";
	}
    // --
		
	// CUSTOMER Porperties
	private String _Customer;

	public String getCustomer()
	{
		return _Customer;
	}

	public void setCustomer(String value) {
		_Customer = value;
	}

	public static String getDICT_Customer()
	{
		return "CUSTOMER";	
	}
	// --
	
	// DATE Porperties
	private String _Date;
	
	public Date getDate()
	{
		if(this._Date != null && this._Date != "")
        {
            try {
            	int d = Integer.valueOf(this._Date);
            	Calendar c = Calendar.getInstance();
            	c.set(1967, 11, 31, 0, 0); 
            	c.add(Calendar.DATE, d);
            	Date dt = c.getTime();
            	return dt;
            }
            catch (Exception ex)
            {
            	return null;
            }
        }
        else
            return null;
	}
	
	public void setDate(Date value)
	{
		if (value!=null)
        {
			Calendar c = Calendar.getInstance();
        	c.set(1967, 11, 31, 0, 0); 
        	long startTime = c.getTime().getTime();
            long endTime = value.getTime();
            long diffTime = endTime - startTime;
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            
			this._Date = String.valueOf(diffDays);
        }
        else
            this._Date = "";
	}

	public static String getDICT_Date()
	{
		return "DATE";
	}
	// --
	
	// ITOTALORDER Properties
	private double _ITotalOrder;

	public double getITotalOrder() 
	{
		return _ITotalOrder;
	}

	public void setITotalOrder(double value) {
			_ITotalOrder = value;
	}

	public static String getDICT_ITotalOrder()
	{
		return "ITOTALORDER";
	}
	// --
	
	// ICUSTOMERNAME Properties
	private String _ICustomerName;

	public String getICustomerName()
	{
		return _ICustomerName;
	}

	public void setICustomerName(String value) {
		_ICustomerName = value;
	}

	public static String getDICT_ICustomerName()
	{
		return "ICUSTOMERNAME";
	}
	// --
	
	// LSTITEMS Multivalue list
	private List<MV_LstItems_CLkOrder> _LstLstItems;
	public List<MV_LstItems_CLkOrder> getLstLstItems()
	{
		return _LstLstItems;
	}
	
	public void setLstLstItems(List<MV_LstItems_CLkOrder> value)
	{
		if (_LstLstItems != null)
        {	            
            _LstLstItems.clear();
            _LstLstItems = null;
        }
        _LstLstItems = value;
	}
	// --

	//------------	
	
	// ---- Constructors
	
	//Build the object with a copy of client
	public CLkOrder(LinkarClt linkarClt) {
		_LinkarClt = linkarClt;
		_LstLstItems = new ArrayList<MV_LstItems_CLkOrder>();
	}
	
	//Build the object with a copy of client and set their status
	public CLkOrder(LinkarClt linkarClt,boolean isNew) {
		_LinkarClt = linkarClt;
		_LstLstItems = new ArrayList<MV_LstItems_CLkOrder>();
		if (isNew)
            setStatus(StatusTypes.NEW);
	}
	
	//------------
	
	// -- Import / Export

    // Fill the record from QM raw Strings
    public void GetRecord(String itemID, String record, String recordCalculated)
    {
		String[] reg = new String[7];
		for (int j = 0; j < reg.length; j++)
		    reg[j] = "";
		if (record != null && record.length() > 0)
		{
			String[] aux = record.split(DBMV_Mark.AM_str);
		            for (int j = 0; j < aux.length; j++)
		                reg[j] = aux[j];
		}
		
		String[] regI =  new String[6];
		            for (int k = 0; k < regI.length; k++)
		                regI[k] = "";
		if (recordCalculated != null && recordCalculated.length() > 0)
		{
			String[] auxI = recordCalculated.split(DBMV_Mark.AM_str);
		            int k = 0;
		            for (; k < auxI.length; k++)
		                regI[k] = auxI[k];
		}
		
		
		setCode(itemID);
		if (record == null || record == "")
			return;
		setCustomer(LinkarDataTypes.GetAlpha(reg[0],-1,-1));
		this._Date = LinkarDataTypes.GetDateTime(reg[1],-1,-1);
		setITotalOrder(LinkarDataTypes.GetDecimal(regI[4],-1,-1));
		setICustomerName(LinkarDataTypes.GetAlpha(regI[0],-1,-1));
		
		// Multivalues

		int numMV_LstItems = 0;
		int tmpCountMV_LstItems = 0;
		tmpCountMV_LstItems = MvFunctions.LkDCount(reg[2], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		tmpCountMV_LstItems = MvFunctions.LkDCount(reg[3], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		tmpCountMV_LstItems = MvFunctions.LkDCount(reg[4], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		tmpCountMV_LstItems = MvFunctions.LkDCount(regI[3], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		tmpCountMV_LstItems = MvFunctions.LkDCount(regI[1], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		tmpCountMV_LstItems = MvFunctions.LkDCount(regI[2], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		tmpCountMV_LstItems = MvFunctions.LkDCount(reg[5], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		tmpCountMV_LstItems = MvFunctions.LkDCount(reg[6], DBMV_Mark.VM_str);
		if (tmpCountMV_LstItems > numMV_LstItems) numMV_LstItems = tmpCountMV_LstItems;
		     for (int i = 0; i < numMV_LstItems; i++)
		            {
		                if (i < this._LstLstItems.size())
		                    this._LstLstItems.get(i).GetItem(this._LinkarClt, reg, regI, i);
		                else
		                {
		                    MV_LstItems_CLkOrder regmv = new MV_LstItems_CLkOrder();
		                    regmv.GetItem(this._LinkarClt, reg, regI, i);
		                    this._LstLstItems.add(regmv);
		                }
		            }
		            if (numMV_LstItems < this._LstLstItems.size())
		            {
		                int offset = this._LstLstItems.size() - numMV_LstItems;
		                for (int i = 0; i < offset; i++)
		                {
		                     this._LstLstItems.remove(_LstLstItems.size() - 1);
		                }
		            }
		//--						
		
	}

    // Export the record to a QM raw Strings
    public String SetRecord()
    {
    	String bufferQM = "";
        String[] reg = new String[7];

        reg[0] = LinkarDataTypes.SetAlpha(reg[0], getCustomer(),-1,-1);
        reg[1] = LinkarDataTypes.SetDateTime(reg[1], this._Date,-1,-1);
        
        // Multivalues
        
        if (this._LstLstItems != null)
        {
            int numMV_LstItems = this._LstLstItems.size();
            for (int i = 0; i < numMV_LstItems; i++)
            reg = this._LstLstItems.get(i).SetItem(reg, i);
        }
        
        //--
		
		bufferQM = String.join((DBMV_Mark.AM_str), reg);
		return bufferQM;
	}
	
	//---------
	
    // -- CRUD      

    // READ item
    public Exception ReadRecord(boolean calculated, String fileName)
    {     
    	Exception error = null;
    	try
    	{
    	//Fill the Read options
    	if (fileName == null || fileName == "")
			fileName = CLkOrder.FILE_CLkOrder;
    	
        boolean conversion = false;
        boolean formatSpec =  false;
        boolean originalRecords = false;
    	ReadOptions readOptions = new ReadOptions(calculated, conversion, formatSpec, originalRecords);
    	String customVars = "";
    	//Call to sincronous session version of Read function
    	String lkstring = _LinkarClt.Read_Text(fileName, _Code, "", readOptions, DATAFORMATCRU_TYPE.MV, customVars, 0);
    	
    	char delimiter = ASCII_Chars.FS_chr;
    	char delimiterThisList = DBMV_Mark.AM;
    	String records = "";
    	String recordCalculateds = "";
    	String[] parts = lkstring.split(String.valueOf(delimiter), -1);
        if (parts.length >= 1)
        {
        	String[] ThisList = parts[0].split(String.valueOf(delimiterThisList), -1);
            int numElements = ThisList.length;
            for (int i = 1; i < numElements; i++)
            {                	
            	if (ThisList[i].equals("RECORD"))
            	{
            		records = parts[i];
            	}
            	if (ThisList[i].equals("CALCULATED"))
            	{
            		recordCalculateds = parts[i];
            	}
            	if (ThisList[i].equals("ERRORS"))
            	{
            		if (parts[i] != null && parts[i].length() > 0)
            			this.LstErrors = new ArrayList<>(Arrays.asList(parts[i].split(DBMV_Mark.AM_str, -1)));
            		else
            			this.LstErrors = new ArrayList<String>();
            	}
            }
        }
    	
      //Fill the class with response data
      if (records != null && records != "")
    	this.GetRecord(this._Code, records, recordCalculateds);
      
    	} catch (LkException e) {
    		error = e;
    	} catch (Exception e) {
    		error = e;
    	}
    	return error;
    }


    // NEW item
    public Exception NewRecord(String fileName)
    {     
    	Exception error = null;
    	try
    	{
    	//Fill the New options
    	if (fileName == null || fileName == "")
			fileName = CLkOrder.FILE_CLkOrder;        		

    	boolean readAfter = false;
        boolean calculated = false;
        boolean conversion = false;
        boolean formatSpec = false;
        boolean originalBuffer = false;
        
        boolean active_RecordIdTypeLinkar = true;
        boolean active_RecordIdTypeRandom = false;
        boolean active_RecordIdTypeCustom = false;
        
        RecordIdType recordIdType = new RecordIdType();
        if (active_RecordIdTypeLinkar)
        {
            String prefix = "";
            String separator = "";
            String formatSpecTxt = "";
            recordIdType = new RecordIdType(prefix,separator,formatSpecTxt);
        }
        else if (active_RecordIdTypeRandom)
        {
            boolean numeric = false;
            int length = 0;
        	recordIdType = new RecordIdType(numeric,length);
        }
        else if (active_RecordIdTypeCustom)
        {
        	recordIdType = new RecordIdType(true);
        } 
        NewOptions NewOptions = new NewOptions(recordIdType ,readAfter, calculated, conversion, formatSpec, originalBuffer);
		
        String customVars = "";
        String inputData = this.SetRecord();
    	
        //Call to sincronous session version of New function
        String lkstring = _LinkarClt.New_Text(fileName, _Code, inputData, NewOptions, DATAFORMAT_TYPE.MV, DATAFORMATCRU_TYPE.MV, customVars, 0);
    	
    	char delimiter = ASCII_Chars.FS_chr;
    	char delimiterThisList = DBMV_Mark.AM;
    	String records = "";
    	String recordCalculateds = "";
    	String[] parts = lkstring.split(String.valueOf(delimiter), -1);
        if (parts.length >= 1)
        {
        	String[] ThisList = parts[0].split(String.valueOf(delimiterThisList), -1);
            int numElements = ThisList.length;
            for (int i = 1; i < numElements; i++)
            {                	
            	if (ThisList[i].equals("RECORD"))
            	{
            		records = parts[i];
            	}
            	if (ThisList[i].equals("CALCULATED"))
            	{
            		recordCalculateds = parts[i];
            	}
            	if (ThisList[i].equals("ERRORS"))
            	{
            		if (parts[i] != null && parts[i].length() > 0)
            			this.LstErrors = new ArrayList<>(Arrays.asList(parts[i].split(DBMV_Mark.AM_str, -1)));
            		else
            			this.LstErrors = new ArrayList<String>();
            	}
            }
        }
    	
        //Fill the class with response data
        if (records != null && records != "")
        	this.GetRecord(this._Code, records, recordCalculateds);
        
    	} catch (LkException e) {
    		error = e;
    	} catch (Exception e) {
    		error = e;
    	}
    	return error;
    }

    // WRITE item
    public Exception WriteRecord(String fileName)
    {   
    	Exception error = null;
    	try
    	{
    	//Fill the Update options
    	if (fileName == null || fileName == "")
			fileName = CLkOrder.FILE_CLkOrder;        		

		boolean optimisticLockControl = false;
        boolean readAfter = false;
        boolean calculated = false;
        boolean conversion = false;
        boolean formatSpec = false;
        boolean originalRecords = false;
        UpdateOptions UpdateOptions = new UpdateOptions(optimisticLockControl, readAfter, calculated, conversion, formatSpec, originalRecords);
		
        String customVars = "";
        String inputData = this.SetRecord();
        //Call to sincronous session version of Update function
    	String lkstring = _LinkarClt.Update_Text(fileName, _Code, inputData, UpdateOptions, this.getItemOriginalContent(), DATAFORMAT_TYPE.MV, DATAFORMATCRU_TYPE.MV, customVars, 0);
    	
    	char delimiter = ASCII_Chars.FS_chr;
    	char delimiterThisList = DBMV_Mark.AM;
    	String records = "";
    	String recordsCalculated = "";
    	String[] parts = lkstring.split(String.valueOf(delimiter), -1);
        if (parts.length >= 1)
        {
        	String[] ThisList = parts[0].split(String.valueOf(delimiterThisList), -1);
            int numElements = ThisList.length;
            for (int i = 1; i < numElements; i++)
            {                	
            	if (ThisList[i].equals("RECORD"))
            	{
            		records = parts[i];
            	}
            	if (ThisList[i].equals("CALCULATED"))
            	{
            		recordsCalculated = parts[i];
            	}
            	if (ThisList[i].equals("ERRORS"))
            	{
            		if (parts[i] != null && parts[i].length() > 0)
            			this.LstErrors = new ArrayList<>(Arrays.asList(parts[i].split(DBMV_Mark.AM_str, -1)));
            		else
            			this.LstErrors = new ArrayList<String>();
            	}
            }
        }
    	
        //Fill the class with response data
        if (records != null && records != "")
        	this.GetRecord(this._Code, records, recordsCalculated);
        
    	} catch (LkException e) {
    		error = e;
    	} catch (Exception e) {
    		error = e;
    	}
    	return error;
    }


    // DELETE item       
    public Exception DeleteRecord(String fileName)
    {     
    	Exception error = null;
    	try
    	{
    	//Fill the Delete options
    	if (fileName == null || fileName == "")
			fileName = CLkOrder.FILE_CLkOrder;        		

    	boolean optimisticLock = false;

        DeleteOptions deleteOptions = new DeleteOptions(optimisticLock);    	
		
        String customVars = "";
        
        //Call to sincronous session version of Delete function
    	String lkstring = _LinkarClt.Delete_Text(fileName, _Code, deleteOptions, this.getItemOriginalContent(), DATAFORMAT_TYPE.MV,  customVars, 0);
    	
    	char delimiter = ASCII_Chars.FS_chr;
    	char delimiterThisList = DBMV_Mark.AM;
    	String records = "";
    	String recordCalculateds = "";
    	String[] parts = lkstring.split(String.valueOf(delimiter), -1);
        if (parts.length >= 1)
        {
        	String[] ThisList = parts[0].split(String.valueOf(delimiterThisList), -1);
            int numElements = ThisList.length;
            for (int i = 1; i < numElements; i++)
            {                	
            	if (ThisList[i].equals("RECORD"))
            	{
            		records = parts[i];
            	}
            	if (ThisList[i].equals("CALCULATED"))
            	{
            		recordCalculateds = parts[i];
            	}
            	if (ThisList[i].equals("ERRORS"))
            	{
            		if (parts[i] != null && parts[i].length() > 0)
            			this.LstErrors = new ArrayList<>(Arrays.asList(parts[i].split(DBMV_Mark.AM_str, -1)));
            		else
            			this.LstErrors = new ArrayList<String>();
            	}
            }
        }
        
        //Fill the class with response data
        if (records != null && records != "")
        	this.GetRecord(this._Code, records, recordCalculateds);
        
    	} catch (LkException e) {
    		error = e;
    	} catch (Exception e) {
    		error = e;
    	}
    	return error;
    }

    // Cancel item changes
    public void RejectChanges()
    {
        if (getStatus() == StatusTypes.MODIFY || getStatus() == StatusTypes.DELETED)
        {
        	//Fill the class with the original data
            this.GetRecord(this.getCode(), this.getItemOriginalContent(),this.getItemOriginalContentItypes());
            setStatus(StatusTypes.READED);
		}
    }

}
