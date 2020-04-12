# Linkar Demo Desktop App Java

executable in https://kosday.com/resources/

This demo shows the working of a persistent client feeding some Linkar classes and showing the resultant information on some forms

# MainWindow 
This window contains the information to make the LinkarClient login and allows making the client logout. If we already have done the login we will have access to the rest of forms

# Customers (FormCustomers) 
This form works with the CLkCustomer and CLkCustomers included in the BL folder
The select button calls the selection method from the (ClkCustomers) plural class that is used with the "calculate" option to charge all the selected data. In the selection dialog you can write a client name or its id.
Before placing in the wished record the rest of buttons will be enabled to call CRUD operations (Create, Read, Update y Delete) located in the singular class (ClkCustomer)

# Items (FormItems) 
This form works with the CLKItems and CLKItem classes included in the BL folder.
The select button calls the selection method from the (ClkItems) plural class that is used with the "calculate" option to charge all the selected data. In the selection dialog you can write a client name or its id.
Before placing in the wished record the rest of buttons will be enabled to call CRUD operations (Create, Read, Update y Delete) located in the singular class (ClkItem)

# Orders Details (FormOrdersDetails) 
This form works with the CLkOrder, MV_LstItems_CLkOrder, SV_LstPartial_ClkOrder and CLkOrders classes included in the BL folder.
The select button calls the selection method from the (ClkOrders) plural class that is used with the "onlyitems" option to charge he left part grid. Each record information will be read when you locate in the mentioned record through a Read with the "calculate" option. In the selection dialog you can write an Order code to directly go to that record.
CRUD methods are not available (Create, Read, Update y Delete) located in the class (LkOrder)
The treatment of the multivalue and subvalues is property of the  MV_LstItems_ClkOrder y SV_LstPartial_ClkOrder y ClkOrders classes. 

# Orders Details (FormOrdersDetails) 
This form works with the CLkOrder, MV_LstItems_CLkOrder, SV_LstPartial_ClkOrder and CLkOrders classes included in the BL folder.
The select button calls the selection method from the (ClkOrders) plural class that is used with the "calculate" option to charge all the data. In the selection dialog you can write an Order code to directly go to that record.
Locating on a multivalue all its subvalues will charge automatically charge on the grid located below.
Since it's about an only Read form you don't have the CRUD methods (Create, Read, Update and Delete) in the ClkOrder class.
The treatment of the multivalues and subvalues is property of the MV_LstItems_ClkOrder y SV_LstPartial_ClkOrder and ClkOrders classes.
