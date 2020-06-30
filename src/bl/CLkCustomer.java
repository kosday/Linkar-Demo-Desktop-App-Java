package bl;
import linkar.LinkarDataTypes;
import linkar.LkException;
import linkar.MainClass;

import java.util.ArrayList;
import java.util.Arrays;

import linkar.ASCII_Chars;
import linkar.DBMV_Mark;
import linkar.DataFunctions;
import linkar.DeleteOptions;
//import linkar.GenericError;
import linkar.LinkarClt;
import linkar.LinkarClt.DATAFORMATCRU_TYPE;
import linkar.LinkarClt.DATAFORMAT_TYPE;
import linkar.RecordIdType;
import linkar.NewOptions;
import linkar.ReadOptions;
import linkar.UpdateOptions;

public class CLkCustomer extends MainClass {
	
	// --- Properties

	//The copy of the client for operations
	private LinkarClt _LinkarClt = null;
	
	//The name for the file in the data base
    public final static String FILE_CLkCustomer = "LK.CUSTOMERS";

    // CODE Porperties
	private String _Code;
	
	public String getCode() 
	{
		return _Code;
	}
	
	public void setCode(String value) 
	{
		_Code = value;
	}

	public static String getDICT_Code()
	{
		return "CODE";
	}
	// ---
	
	// NAME Porperties
	private String _Name;

	public String getName ()
	{
		return _Name;
	}
	
	public void setName(String value) {
		_Name = value;
	}

	public static String getDICT_Name()
	{
		return "NAME";
	}
	// ---
	
	// ADDRESS Porperties
	private String _Address;

	public String getAddress() 
	{
		return _Address;
	}
	
	public void setAddress(String value) {
		_Address = value;
	}

	public static String getDICT_Address()
	{
		return "ADDR";
	}
	// ---
	
	// PHONE Porperties
	private String _Phone;

	public String getPhone() 
	{
		return _Phone;
	}
	
	public void setPhone(String value) {
		_Phone = value;
	}

	public static String getDICT_Phone()
	{
		return "PHONE"; 
	}
	// ---

	//-----------
	
	// ---- Contructors
	
	//Build the object with a copy of client
	public CLkCustomer(LinkarClt linkarClt) {
		_LinkarClt = linkarClt;
	}
	
	//Build the object with a copy of client and set their status
	public CLkCustomer(LinkarClt linkarClt,boolean isNew) {
		_LinkarClt = linkarClt;
		if (isNew)
            setStatus(StatusTypes.NEW);
	}
	
	//------------
		
	// -- Import / Export

    // Fill the record from QM raw Strings
    public void GetRecord(String recordID, String record, String recordCalculated)
    {
		String[] reg = new String[3];
		for (int j = 0; j < reg.length; j++)
		    reg[j] = "";
		if (record != null && record.length() > 0)
		{
			String[] aux = record.split(DBMV_Mark.AM_str);
		            for (int j = 0; j < aux.length; j++)
		                reg[j] = aux[j];
		}
		
		String[] regI =  new String[0];
		            for (int k = 0; k < regI.length; k++)
		                regI[k] = "";
		if (recordCalculated != null && recordCalculated.length() > 0)
		{
			String[] auxI = recordCalculated.split(DBMV_Mark.AM_str);
		            int k = 0;
		            for (; k < auxI.length; k++)
		                regI[k] = auxI[k];
		}
		
		
		setCode(recordID);
		setName(LinkarDataTypes.GetAlpha(reg[0],-1,-1));
		setAddress(LinkarDataTypes.GetAlpha(reg[1],-1,-1));
		setPhone(LinkarDataTypes.GetAlpha(reg[2],-1,-1));
		
	}

    // Export the record to a QM raw Strings
    public String SetRecord()
    {
    	String record = "";
        String[] reg = new String[3];

        reg[0] = LinkarDataTypes.SetAlpha(reg[0], getName(),-1,-1);
        reg[1] = LinkarDataTypes.SetAlpha(reg[1], getAddress(),-1,-1);
        reg[2] = LinkarDataTypes.SetAlpha(reg[2], getPhone(),-1,-1);
		
        record = String.join((DBMV_Mark.AM_str), reg);
		return record;
	}
	
	//---------
		
    // -- CRUD       

    // READ item
    public Exception ReadRecord(boolean calculated, String fileName)
    {    
    	Exception error = null;
		try {
    	//Fill the Read options
    	if (fileName == null || fileName == "")
			fileName = CLkCustomer.FILE_CLkCustomer;
    	
        boolean conversion = false;
        boolean formatSpec =  false;
        boolean originalRecords = false;
    	ReadOptions readOptions = new ReadOptions(calculated, conversion, formatSpec, originalRecords);
    	String customVars = "";
    	
    	//Call to sincronous session version of Read function
    	String lkstring;

		lkstring = _LinkarClt.Read_Text(fileName, _Code, "", readOptions, DATAFORMATCRU_TYPE.MV, customVars, 0);
    	
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
			fileName = CLkCustomer.FILE_CLkCustomer;        		

    	boolean readAfter = true;
        boolean calculated = false;
        boolean conversion = false;
        boolean formatSpec = false;
        boolean originalRecords = false;
        
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
        
        NewOptions NewOptions = new NewOptions(recordIdType ,readAfter, calculated, conversion, formatSpec, originalRecords);
		
        String customVars = "";
        String inputData = this.SetRecord();
    	
        //Call to sincronous session version of New function
        String lkstring = _LinkarClt.New_Text(fileName, _Code, inputData, NewOptions, LinkarClt.DATAFORMAT_TYPE.MV, LinkarClt.DATAFORMATCRU_TYPE.MV, customVars, 0);
    	
    	char delimiter = ASCII_Chars.FS_chr;
    	char delimiterThisList = DBMV_Mark.AM;
    	String recordIds = "";
    	String records = "";
    	String recordCalculateds = "";
    	String[] parts = lkstring.split(String.valueOf(delimiter), -1);
        if (parts.length >= 1)
        {
        	String[] ThisList = parts[0].split(String.valueOf(delimiterThisList), -1);
            int numElements = ThisList.length;
            for (int i = 1; i < numElements; i++)
            {  
            	if (ThisList[i].equals("RECORD_ID"))
            	{
            		recordIds = parts[i];
            	}
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
        	this.GetRecord(recordIds != null && !recordIds.isEmpty() ? recordIds : this._Code, records, recordCalculateds);
        
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
			fileName = CLkCustomer.FILE_CLkCustomer;        		

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

    // DELETE item        
    public Exception DeleteRecord(String fileName)
    {  
    	Exception error = null;
    	try
    	{
    	//Fill the Delete options
    	if (fileName == null || fileName == "")
			fileName = CLkCustomer.FILE_CLkCustomer;        		

    	boolean optimisticLock = false;

        DeleteOptions deleteOptions = new DeleteOptions(optimisticLock);    	
		
        String customVars = "";
        
        //Call to sincronous session version of Delete function
    	String lkstring = _LinkarClt.Delete_Text(fileName, _Code, deleteOptions, this.getItemOriginalContent(), DATAFORMAT_TYPE.MV, customVars, 0);
    	
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
    	if (getStatus() != StatusTypes.NEW)
        {
    		//Fill the class with the original data
            this.GetRecord(this.getCode(), this.getItemOriginalContent(),this.getItemOriginalContentItypes());
            setStatus(StatusTypes.READED);
		}
    }
	
}
