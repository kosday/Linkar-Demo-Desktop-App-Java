package forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import bl.CLkOrder;
import bl.CLkOrders;
import bl.MV_LstItems_CLkOrder;
import bl.SV_LstPartial_CLkOrder;
import linkar.DBMV_Mark;
//import linkar.GenericError;
import linkar.LinkarClt;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JTextArea;

public class FormOrdesDetails extends JDialog {
	
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
	CLkOrders clkorders = null;
	CLkOrder currentOrder = null;

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCode;
	private JTextField txtCustomer;
	private JTextField txtCustomerName;
	private JTable table;
	private JTextField txtSelect;
	private JTable table_LstItems;
	private JTable table_LstPartial;
	private JSpinner txtTotalOrder;
	private JSpinner txtDate;
	private JLabel lblSelectedItemValue;
	

	/**
	 * Create the dialog.
	 */
	public FormOrdesDetails(LinkarClt linkarClt) {
		clkorders = new CLkOrders(linkarClt);
		setModal(true);
		setTitle("Orders Details");
		setBounds(100, 100, 885, 604);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 849, 33);
		contentPanel.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		txtSelect = new JTextField();
		txtSelect.setToolTipText("Insert Code or Empty");
		txtSelect.setColumns(50);
		panel.add(txtSelect);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultTableModel dm_LstPartial = (DefaultTableModel) table_LstPartial.getModel();
				int rowCount_LstPartial = dm_LstPartial.getRowCount();
		
				for (int i = rowCount_LstPartial - 1; i >= 0; i--) {
				    dm_LstPartial.removeRow(i);
				}
				
				lblSelectedItemValue.setText("");
				txtCustomer.setText("");				
				txtDate.setValue(new Date());
				txtTotalOrder.setValue(0);
        		txtCustomerName.setText("");
				
				DefaultTableModel dm_LstItems = (DefaultTableModel) table_LstItems.getModel();
				int rowCount_LstItems = dm_LstItems.getRowCount();
		
				for (int i = rowCount_LstItems - 1; i >= 0; i--) {
				    dm_LstItems.removeRow(i);
				}
				
				txtCode.setText("");
				txtSelect.setText("");
				
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int rowCount = dm.getRowCount();
		
				for (int i = rowCount - 1; i >= 0; i--) {
				    dm.removeRow(i);
				}
				
			}
		});
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String sel = txtSelect.getText();
				Exception err = null;
				//Call to FindAll or SelectAll method of CLkOrders class
				if (sel != null && !sel.isEmpty())
					err = clkorders.FindAll("", txtSelect.getText(), 0, 0, "", "", true, false, false);
				else
					err = clkorders.SelectAll("","", 0, 0, true, false, false);
				if (err != null)
				{
					javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(err), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					DefaultTableModel dm = (DefaultTableModel) table.getModel();
					int rowCount = dm.getRowCount();
	
					for (int i = rowCount - 1; i >= 0; i--) {
					    dm.removeRow(i);
					}
					
					//Draw customers data
					if (clkorders != null)
					{		
						if (clkorders.LstErrors.size() > 0)
						{
							javax.swing.JOptionPane.showMessageDialog(null, String.join("\r\n", clkorders.LstErrors), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
						}
						if (clkorders.size() > 0)
						{
							for (int i = 0; i < clkorders.size(); i++)
							{
								String[] fila = new String[1];
								fila[0] = clkorders.get(i).getCode();
								((DefaultTableModel)table.getModel()).addRow(fila);
							}
							
							CLkOrder clkorder = new CLkOrder(linkarClt);
							clkorder.setCode(clkorders.get(0).getCode());
	
				            Exception error = clkorder.ReadRecord(true, "");
				            
				            if (error != null)
							{
								javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(error), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
							}
							else
							{
								currentOrder = clkorder;
								txtCode.setText(clkorder.getCode());
								txtCustomer.setText(clkorder.getCustomer());
								txtDate.setValue(clkorder.getDate());
								txtTotalOrder.setValue(clkorder.getITotalOrder());
				        		txtCustomerName.setText(clkorder.getICustomerName());
				        		
				        		DefaultTableModel dm_LstItems = (DefaultTableModel) table_LstItems.getModel();
								int rowCount_LstItems = dm_LstItems.getRowCount();
			
								for (int i = rowCount_LstItems - 1; i >= 0; i--) {
								    dm_LstItems.removeRow(i);
								}
			
								if (clkorder.getLstLstItems() != null && clkorder.getLstLstItems().size() > 0)
								{					
									for (int i = 0; i < clkorder.getLstLstItems().size(); i++)
									{
										MV_LstItems_CLkOrder mv = clkorder.getLstLstItems().get(i);		
										((DefaultTableModel)table_LstItems.getModel()).addRow(new Object[]{
												mv.getItem(),
												mv.getIItemDescription(),
												mv.getIItemStock(),
												mv.getQty(),								
												mv.getPrice(),
												mv.getITotalLine()
												});	
									}
									
									MV_LstItems_CLkOrder mvclkorder = clkorder.getLstLstItems().get(0);
									lblSelectedItemValue.setText(mvclkorder.getItem());
															
									DefaultTableModel dm_LstPartial = (DefaultTableModel) table_LstPartial.getModel();
									int rowCount_LstPartial = dm_LstPartial.getRowCount();
							
									for (int i = rowCount_LstPartial - 1; i >= 0; i--) {
									    dm_LstPartial.removeRow(i);
									}
			
									if (mvclkorder.getLstLstPartial() != null && mvclkorder.getLstLstPartial().size() > 0)
									{					
										for (int i = 0; i < mvclkorder.getLstLstPartial().size(); i++)
										{
											SV_LstPartial_CLkOrder sv = mvclkorder.getLstLstPartial().get(i);		
											((DefaultTableModel)table_LstPartial.getModel()).addRow(new Object[]{
													sv.getDeliveryDate() != null? df.format(sv.getDeliveryDate()) : "",
													sv.getPartialQuantity(),
													});	
										}													
									}
								}
				            }
						}
					}
				}
			}
		});
		panel.add(btnSelect);
		panel.add(btnClear);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(156, 55, 703, 408);
		contentPanel.add(panel_1);
		
		JLabel label = new JLabel("Code");
		label.setBounds(10, 11, 46, 14);
		panel_1.add(label);
		
		txtCode = new JTextField();
		txtCode.setEnabled(false);
		txtCode.setColumns(10);
		txtCode.setBounds(107, 8, 153, 20);
		panel_1.add(txtCode);
		
		JLabel label_1 = new JLabel("Date");
		label_1.setBounds(10, 76, 46, 14);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("Total Order");
		label_2.setBounds(334, 14, 65, 14);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("Customer");
		label_3.setBounds(10, 42, 46, 14);
		panel_1.add(label_3);
		
		txtCustomer = new JTextField();
		txtCustomer.setEnabled(false);
		txtCustomer.setColumns(10);
		txtCustomer.setBounds(107, 39, 153, 20);
		panel_1.add(txtCustomer);
		
		JLabel label_4 = new JLabel("Customer Name");
		label_4.setBounds(334, 42, 87, 14);
		panel_1.add(label_4);
		
		txtCustomerName = new JTextField();
		txtCustomerName.setEnabled(false);
		txtCustomerName.setColumns(10);
		txtCustomerName.setBounds(431, 39, 153, 20);
		panel_1.add(txtCustomerName);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 127, 683, 113);
		panel_1.add(scrollPane_1);
		
		table_LstItems = new JTable();
		table_LstItems.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Item", "Item Description", "Item Stock", "Quantity", "Price", "Total Line"
				}
			){
			boolean[] columnEditables = new boolean[] {
					false, false,false,false,false,false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		ListSelectionModel listSelectionModel_LstItems = table_LstItems.getSelectionModel();
		class SharedListSelectionHandler_LstItems implements ListSelectionListener {
	        public void valueChanged(ListSelectionEvent e) { 
	        	if (e.getValueIsAdjusting()) return;	        	
	            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	            if (currentOrder != null && table_LstItems.getModel() != null && table_LstItems.getModel().getRowCount() > 0 && lsm != null && lsm.getMinSelectionIndex() > -1)
	            {
	            	String item = String.valueOf(table_LstItems.getModel().getValueAt(lsm.getMinSelectionIndex(), 0));
		            MV_LstItems_CLkOrder mvclkorder = null;
		    		for (int i = 0; i < currentOrder.getLstLstItems().size(); i++)
		            {
		    			mvclkorder = currentOrder.getLstLstItems().get(i);
		            	if (mvclkorder.getItem().equals(item))
		            		break;
		            	else
		            		mvclkorder = null;
		        	}
		            if (mvclkorder != null)
		            {	   
		            	lblSelectedItemValue.setText(mvclkorder.getItem());
		            	DefaultTableModel dm_LstPartial = (DefaultTableModel) table_LstPartial.getModel();
						int rowCount_LstPartial = dm_LstPartial.getRowCount();
				
						for (int i = rowCount_LstPartial - 1; i >= 0; i--) {
						    dm_LstPartial.removeRow(i);
						}
	
						if (mvclkorder.getLstLstPartial() != null && mvclkorder.getLstLstPartial().size() > 0)
						{					
							for (int i = 0; i < mvclkorder.getLstLstPartial().size(); i++)
							{
								SV_LstPartial_CLkOrder sv = mvclkorder.getLstLstPartial().get(i);		
								((DefaultTableModel)table_LstPartial.getModel()).addRow(new Object[]{
										sv.getDeliveryDate() != null? df.format(sv.getDeliveryDate()) : "",
										sv.getPartialQuantity(),
										});	
							}													
						}
		            }	            
	            }	            
	        }
	    }
		listSelectionModel_LstItems.addListSelectionListener(new SharedListSelectionHandler_LstItems());
        table_LstItems.setSelectionModel(listSelectionModel_LstItems);
		scrollPane_1.setViewportView(table_LstItems);
		
		JLabel lblLstitems = new JLabel("LstItems (MultiValue Group)");
		lblLstitems.setBounds(10, 102, 155, 14);
		panel_1.add(lblLstitems);
		
		JLabel lblLstpartialsv = new JLabel("LstPartial (SubValue Group)");
		lblLstpartialsv.setBounds(10, 251, 142, 14);
		panel_1.add(lblLstpartialsv);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 274, 683, 120);
		panel_1.add(scrollPane_2);
		
		table_LstPartial = new JTable();
		table_LstPartial.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Delivery Date", "Partial Quantity"
				}
			){
			boolean[] columnEditables = new boolean[] {
					false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		scrollPane_2.setViewportView(table_LstPartial);
		
		txtTotalOrder = new JSpinner();
		txtTotalOrder.setEnabled(false);
		txtTotalOrder.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		txtTotalOrder.setBounds(429, 11, 97, 20);
		txtTotalOrder.setEditor(new JSpinner.NumberEditor(txtTotalOrder, "#.#"));
		panel_1.add(txtTotalOrder);
		
		txtDate = new JSpinner();

		txtDate.setModel(new SpinnerDateModel(new Date(1511996400000L), null, null, Calendar.DAY_OF_YEAR));
		txtDate.setEnabled(false);
		txtDate.setBounds(107, 73, 108, 20);
		txtDate.setEditor(new JSpinner.DateEditor(txtDate, "MM/dd/yyyy"));
		panel_1.add(txtDate);
		
		JLabel lblSelecteditem = new JLabel("-  SelectedItem:");
		lblSelecteditem.setBounds(153, 251, 80, 14);
		panel_1.add(lblSelecteditem);
		
		lblSelectedItemValue = new JLabel("");
		lblSelectedItemValue.setBounds(237, 251, 370, 14);
		panel_1.add(lblSelectedItemValue);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 55, 136, 408);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Code"
				}
			){
			boolean[] columnEditables = new boolean[] {
					false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		ListSelectionModel listSelectionModel = table.getSelectionModel();
		class SharedListSelectionHandler implements ListSelectionListener {
	        public void valueChanged(ListSelectionEvent e) { 
	        	if (e.getValueIsAdjusting()) return;	        	
	            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	            if (table.getModel() != null && table.getModel().getRowCount() > 0 && lsm != null && lsm.getMinSelectionIndex() > -1)
	            {
		            String id = String.valueOf(table.getModel().getValueAt(lsm.getMinSelectionIndex(), 0));
		            CLkOrder clkorder = new CLkOrder(linkarClt);
		            clkorder.setCode(id);
		            //GenericError ge = new GenericError();
		            Exception error = clkorder.ReadRecord(true, "");
		            if (error != null)
					{
						javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(error), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
					}
					else
					{
		            	currentOrder = clkorder;
		            	txtCode.setText(clkorder.getCode());
						txtCustomer.setText(clkorder.getCustomer());
						txtDate.setValue(clkorder.getDate());
						txtTotalOrder.setValue(clkorder.getITotalOrder());
		        		txtCustomerName.setText(clkorder.getICustomerName());
		            	
		            	DefaultTableModel dm_LstItems = (DefaultTableModel) table_LstItems.getModel();
						int rowCount_LstItems = dm_LstItems.getRowCount();

						for (int i = rowCount_LstItems - 1; i >= 0; i--) {
						    dm_LstItems.removeRow(i);
						}

						if (clkorder.getLstLstItems() != null && clkorder.getLstLstItems().size() > 0)
						{					
							for (int i = 0; i < clkorder.getLstLstItems().size(); i++)
							{
								MV_LstItems_CLkOrder mv = clkorder.getLstLstItems().get(i);		
								((DefaultTableModel)table_LstItems.getModel()).addRow(new Object[]{
										mv.getItem(),
										mv.getIItemDescription(),
										mv.getIItemStock(),
										mv.getQty(),								
										mv.getPrice(),
										mv.getITotalLine()
										});	
							}
							
							MV_LstItems_CLkOrder mvclkorder = clkorder.getLstLstItems().get(0);
							lblSelectedItemValue.setText(mvclkorder.getItem());
													
							DefaultTableModel dm_LstPartial = (DefaultTableModel) table_LstPartial.getModel();
							int rowCount_LstPartial = dm_LstPartial.getRowCount();
					
							for (int i = rowCount_LstPartial - 1; i >= 0; i--) {
							    dm_LstPartial.removeRow(i);
							}

							if (mvclkorder.getLstLstPartial() != null && mvclkorder.getLstLstPartial().size() > 0)
							{					
								for (int i = 0; i < mvclkorder.getLstLstPartial().size(); i++)
								{
									SV_LstPartial_CLkOrder sv = mvclkorder.getLstLstPartial().get(i);		
									((DefaultTableModel)table_LstPartial.getModel()).addRow(new Object[]{
											sv.getDeliveryDate() != null? df.format(sv.getDeliveryDate()) : "",									
											sv.getPartialQuantity(),
											});	
								}													
							}
						}
		            }	            
	            }	            
	        }
	    }
		listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        table.setSelectionModel(listSelectionModel);
		scrollPane.setViewportView(table);
		
		JTextArea txtHelp = new JTextArea();
		txtHelp.setEnabled(false);
		txtHelp.setEditable(false);
		txtHelp.setBounds(10, 471, 849, 83);
		contentPanel.add(txtHelp);
		
		txtHelp.setText("In this form the \"Select\" method is used with \"onlyitems\" option to load the grid on the left.\r\nRecords are read every time they are focused using the \"Read\" method with the \"calculated\" option\r\nIn the select box you can write an order code to go directly to it\r\nTo load the data, a standard class is used that collects the MV buffer and assigns it to each property.");
	}
}
