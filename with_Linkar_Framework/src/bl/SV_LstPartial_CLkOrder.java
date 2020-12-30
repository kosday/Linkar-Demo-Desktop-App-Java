package bl;

import java.util.Calendar;
import java.util.Date;

import linkar.functions.persistent.mv.LinkarClient;

public class SV_LstPartial_CLkOrder {
	
    // --- Properties
	
	// DELIVERYDATE Porperties
	private String _DeliveryDate;
	
	public Date getDeliveryDate()
	{
		if(this._DeliveryDate != null && this._DeliveryDate != "")
        {
            try {
            	int d = Integer.valueOf(this._DeliveryDate);
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
	
	public void setDeliveryDate(Date value)
	{
		if (value!=null)
        {
			Calendar c = Calendar.getInstance();
        	c.set(1967, 11, 31, 0, 0); 
			long startTime = c.getTime().getTime();
            long endTime = value.getTime();
            long diffTime = endTime - startTime;
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
             
			this._DeliveryDate = String.valueOf(diffDays);    
        }
        else
            this._DeliveryDate = "";		
	}
	
	public static String getDICT_DeliveryDate()
	{
		return "DELIVERYDATE";
	}
	// ---
	
	// QTYPARTIAL Porperties
	private double _PartialQuantity;
	
	public double getPartialQuantity() 
	{
		return _PartialQuantity;
	}
	
	public void setPartialQuantity(double value) {
		_PartialQuantity = value;
	}
	
	public static String getDICT_PartialQuantity()
	{
		return "QTYPARTIAL";
	}       
	// ---

   //----------
	
	// ---- Contructors
	
	public SV_LstPartial_CLkOrder() {

	}
	
	//------------
	
	// -- Import / Export


    // Fill the item from QM raw Strings
    public void GetItem(LinkarClient _LinkarClt, String[] reg, String[] regI, int mvNumber, int svNumber) //NEWFRAMEWORK: Replace LinkarClt for LinkarClient
    {
    	this._DeliveryDate = LinkarDataTypes.GetDateTime(reg[5],mvNumber,svNumber);
    	setPartialQuantity(LinkarDataTypes.GetDecimal(reg[6],mvNumber,svNumber));	    	
	}

    // Export the item to a QM raw Strings
    public String[] SetItem(String[] reg, int mvNumber, int svNumber)
    {
    	reg[5] = LinkarDataTypes.SetDateTime(reg[5], this._DeliveryDate,mvNumber,-svNumber);
        reg[6] = LinkarDataTypes.SetDecimal(reg[6], getPartialQuantity(),-1,mvNumber,svNumber);
		
		return reg;
	}
	
	//---------
}
