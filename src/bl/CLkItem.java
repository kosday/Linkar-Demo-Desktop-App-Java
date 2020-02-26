package bl;

import linkar.LinkarDataTypes;
import linkar.LkException;
import linkar.MainClass;

import java.util.ArrayList;
import java.util.Arrays;

import linkar.ASCII_Chars;
import linkar.DBMV_Mark;
import linkar.DeleteOptions;
import linkar.RecoverIdType;
import linkar.LinkarClt;
import linkar.RecordIdType;
import linkar.NewOptions;
import linkar.LinkarClt.DATAFORMAT_TYPE;
import linkar.ReadOptions;
import linkar.UpdateOptions;


public class CLkItem extends MainClass {	

	// --- Properties

	//The copy of the client for operations
	private LinkarClt _LinkarClt = null;
	
	//The name for the file in the data base
	public final static String FILE_CLkItem = "LK.ITEMS";

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
	
	// DESCRIPTION Porperties
	private String _Description;
	
	public String getDescription ()
	{
		return _Description;
	}
	
	public void setDescription (String value)
	{
		_Description = value;		
	}

	public static String getDICT_Description()
	{
		return "DESCRIPTION";
	}	
	// ---
	
	// STOCK Porperties
	private double _Stock;
	
	public double getStock()
	{
		return _Stock;
	}
	
	public void setStock(double value) 
	{
		_Stock = value;			
	}


	public static String getDICT_Stock()
	{
		return "STOCK";
	}
	// ---

	//----------
	
	// ---- Constructors
	
	//Build the object with a copy of client
	public CLkItem(LinkarClt linkarClt) {
		_LinkarClt = linkarClt;
	}
	
	//Build the object with a copy of client and set their status
	public CLkItem(LinkarClt linkarClt,boolean isNew) {
		_LinkarClt = linkarClt;
		if (isNew)
            setStatus(StatusTypes.NEW);
	}
	
	//------------
	
	// -- Import / Export

    // Fill the record from QM raw Strings
    public void GetRecord(String recordID, String record, String recordCalculated)
    {
		String[] reg = new String[2];
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
		setDescription(LinkarDataTypes.GetAlpha(reg[0],-1,-1));
		setStock(LinkarDataTypes.GetDecimal(reg[1],-1,-1));			
	}

    // Export the record to a QM raw Strings
    public String SetRecord()
    {
    	String record = "";
        String[] reg = new String[2];

        reg[0] = LinkarDataTypes.SetAlpha(reg[0], getDescription(),-1,-1);
        reg[1] = LinkarDataTypes.SetDecimal(reg[1], getStock(),-1,-1,-1);
		
        record = String.join((DBMV_Mark.AM_str), reg);
		return record;
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
			fileName = CLkItem.FILE_CLkItem;
    	
        boolean dictionaries = false;
        boolean conversion = false;
        boolean formatSpec =  false;
        boolean originalRecords = false;
    	ReadOptions readOptions = new ReadOptions(calculated, conversion, formatSpec, originalRecords, dictionaries);
    	String customVars = "";
    	//Call to sincronous session version of Read function
    	String lkstring = _LinkarClt.Read_Text(fileName, _Code, "", readOptions, LinkarClt.DATAFORMAT_TYPE.MV, customVars, 0);
    	
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
    	Exception error=null;
    	try 
    	{
    	//Fill the New options
    	if (fileName == null || fileName == "")
			fileName = CLkItem.FILE_CLkItem;        		

    	boolean readAfter = true;
        boolean calculated = false;
        boolean dictionaries = false;
        boolean conversion = false;
        boolean formatSpec = false;
        boolean originalRecords = false;
        
        boolean active_RecordIdTypeLinkar = false;
        boolean active_RecordIdTypeRandom = false;
        boolean active_RecordIdTypeCustom = false;

        String prefix = "";
        String separator = "";
        String formatSpecTxt = "";
        boolean numeric = false;
        int Length = 0;

        RecordIdType recordIdType;
        if (active_RecordIdTypeLinkar)
            recordIdType = new RecordIdType(prefix, separator, formatSpecTxt);
        else if (active_RecordIdTypeRandom)
            recordIdType = new RecordIdType(numeric, Length);
        else if (active_RecordIdTypeCustom)
            recordIdType = new RecordIdType(true);
        else
            recordIdType = new RecordIdType();
        NewOptions NewOptions = new NewOptions(recordIdType, readAfter, calculated, conversion, formatSpec, originalRecords, dictionaries);
		
        String customVars = "";
        String inputData = this.SetRecord();
        //Call to sincronous session version of New function
        String lkstring = _LinkarClt.New(fileName, _Code, inputData, NewOptions, LinkarClt.DATAFORMAT_TYPE.MV, LinkarClt.DATAFORMAT_TYPE.MV, customVars, 0);
    	
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
			fileName = CLkItem.FILE_CLkItem;        		

		boolean optimisticLockControl = false;
        boolean readAfter = false;
        boolean calculated = false;
        boolean dictionaries = false;
        boolean conversion = false;
        boolean formatSpec = false;
        boolean originalRecord = false;
        UpdateOptions UpdateOptions = new UpdateOptions(optimisticLockControl, readAfter, calculated, conversion, formatSpec, originalRecord, dictionaries);
		
        String customVars = "";
        String inputData = this.SetRecord();
        //Call to sincronous session version of Update function
    	String lkstring = _LinkarClt.Update_Text(fileName, _Code, inputData, UpdateOptions, this.getItemOriginalContent(), LinkarClt.DATAFORMAT_TYPE.MV, LinkarClt.DATAFORMAT_TYPE.MV, customVars, 0);
    	
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
			fileName = CLkItem.FILE_CLkItem;        		

        boolean optimisticLock = false;
        boolean activeRecoverLinkar = false;
        String prefixRecoverLinkar = "";
        String separatorRecoverLinkar = "";
        boolean activeRecoverCustom = false;

        RecoverIdType recoverIdType;
        if (activeRecoverLinkar)
            recoverIdType = new RecoverIdType(prefixRecoverLinkar, separatorRecoverLinkar);
        else if (activeRecoverCustom)
            recoverIdType = new RecoverIdType(true);
        else
            recoverIdType = new RecoverIdType();
        DeleteOptions deleteOptions = new DeleteOptions(optimisticLock, recoverIdType);
		
        String customVars = "";
        
        //Call to sincronous session version of Delete function
    	String lkstring = _LinkarClt.Delete_Text(fileName, _Code, deleteOptions, this.getItemOriginalContent(), LinkarClt.DATAFORMAT_TYPE.MV,  customVars, 0);
    	
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
