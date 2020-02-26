package bl;
import java.util.ArrayList;
import java.util.List;

import linkar.LinkarDataTypes;
import linkar.DBMV_Mark;
import linkar.LinkarClt;
import linkar.MvFunctions;

public class MV_LstItems_CLkOrder {
	
    // --- Properties

	// ITEM Porperties
    private String _Item;

	public String getItem() 
	{
		return _Item;
	}
	
	public void setItem(String value) {
		_Item = value;
	}
	
	public static String getDICT_Item()
	{
		return "ITEM"; 
	}
	// ---
	
	// QTY Porperties
	private double _Qty;
	
	public double getQty() 
	{
		return _Qty;
	}
	
	public void setQty(double value) {
		_Qty = value;	
	}
		
	public static String getDICT_Qty()
	{
		return "QTY";
	}
	// ---
	
	// PROCE Porperties
	private double _Price;
	
	public double getPrice() 
	{
		return _Price;
	}
	
	public void setPrice(double value) {
		_Price = value;
	}
	
	public static String getDICT_Price()
	{
		return "PRICE"; 
	}
	// ---
	
	// ITOTALLINE Porperties
	private double _ITotalLine;
	
	public double getITotalLine ()
	{
		return _ITotalLine;
	}
	
	public void setITotalLine(double value) {
		_ITotalLine = value;
	}
	
	
	public static String getDICT_ITotalLine()
	{
		return "ITOTALLINE"; 
	}
	// ---
	
	// IITEMDESCRIPTION Porperties
	private String _IItemDescription;

	public String getIItemDescription()
	{
		return _IItemDescription;
	}
	
	public void setIItemDescription(String value) {
		_IItemDescription = value;
	}
	
	public static String getDICT_IItemDescription()
	{
		return "IITEMDESCRIPTION";
	}
	// ---
	
	// IITEMSTOCK Porperties
	private double _IItemStock;
	
	public double getIItemStock() 
	{
		return _IItemStock;
	}
	
	public void setIItemStock(double value) {
		_IItemStock = value;		
	}
	
	public static String getDICT_IItemStock()
	{
		return "IITEMSTOCK"; 
	}
	// ---
	
	// LSTPARTIAL Multivalue list
	private List<SV_LstPartial_CLkOrder> _LstLstPartial;
	public List<SV_LstPartial_CLkOrder> getLstLstPartial()
	{
		return _LstLstPartial;
	}
	
	public void setLstLstPartial(List<SV_LstPartial_CLkOrder> value)
	{
		if (_LstLstPartial != null)
        {
            _LstLstPartial.clear();
            _LstLstPartial = null;
        }
        _LstLstPartial = value;
	}
		
	//----------
	
	// ---- Contructors
	
	public MV_LstItems_CLkOrder() {
		_LstLstPartial = new ArrayList<SV_LstPartial_CLkOrder>();
	}
	
	//------------
	
	// -- Import / Export


    // Fill the item from QM raw Strings
    public void GetItem(LinkarClt _LinkarClt, String[] reg, String[] regI, int mvNumber)
    {
    	setItem(LinkarDataTypes.GetAlpha(reg[2],mvNumber,-1));
    	setQty(LinkarDataTypes.GetDecimal(reg[3],mvNumber,-1));
    	setPrice(LinkarDataTypes.GetDecimal(reg[4],mvNumber,-1));	
    	setITotalLine(LinkarDataTypes.GetDecimal(regI[3], mvNumber,-1));
    	setIItemDescription(LinkarDataTypes.GetAlpha(regI[1], mvNumber,-1));
    	setIItemStock(LinkarDataTypes.GetDecimal(regI[2], mvNumber,-1));
    	
    	//Subvalues

		int numSV_LstPartial = 0;
		int tmpCountSV_LstPartial = 0;
		tmpCountSV_LstPartial = MvFunctions.LkDCount(reg[5].split(DBMV_Mark.VM_str,-1)[mvNumber], DBMV_Mark.SM_str);
		if (tmpCountSV_LstPartial > numSV_LstPartial) numSV_LstPartial = tmpCountSV_LstPartial;
		tmpCountSV_LstPartial = MvFunctions.LkDCount(reg[6].split(DBMV_Mark.VM_str,-1)[mvNumber], DBMV_Mark.SM_str);		
		if (tmpCountSV_LstPartial > numSV_LstPartial) numSV_LstPartial = tmpCountSV_LstPartial;
		     for (int i = 0; i < numSV_LstPartial; i++)
		            {
		                if (i < this._LstLstPartial.size())
		                    this._LstLstPartial.get(i).GetItem(_LinkarClt, reg, regI, mvNumber, i);
		                else
		                {
		                	SV_LstPartial_CLkOrder regsv = new SV_LstPartial_CLkOrder();
		                    regsv.GetItem(_LinkarClt, reg, regI, mvNumber, i);
		                    this._LstLstPartial.add(regsv);
		                }
		            }
		            if (numSV_LstPartial < this._LstLstPartial.size())
		            {
		                int offset = this._LstLstPartial.size() - numSV_LstPartial;
		                for (int i = 0; i < offset; i++)
		                {
		                     this._LstLstPartial.remove(_LstLstPartial.size() - 1);
		                }
		            }
		//--
	}


    // Export the item to a QM raw Strings
    public String[] SetItem(String[] reg, int mvNumber)
    {
        reg[2] = LinkarDataTypes.SetAlpha(reg[0], getItem(),mvNumber,-1);
        reg[3] = LinkarDataTypes.SetDecimal(reg[1], getQty(),-1,mvNumber,-1);
        reg[4] = LinkarDataTypes.SetDecimal(reg[1], getPrice(),-1,mvNumber,-1);
        
        // Subvalues
        if (this._LstLstPartial != null)
        {
        int numSV_LstPartial = this._LstLstPartial.size();
        for (int i = 0; i < numSV_LstPartial; i++)
        reg = this._LstLstPartial.get(i).SetItem(reg, mvNumber, i);
        }
        //--
		
		return reg;
	}
	
	//---------
    
}
