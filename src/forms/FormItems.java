package forms;
import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bl.CLkItem;
import bl.CLkItems;
import linkar.MainClass.StatusTypes;
import linkar.DBMV_Mark;
//import linkar.GenericError;
import linkar.LinkarClt;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FormItems extends JDialog {
	
	CLkItems clkitems = null;
	CLkItem clkitem = null;

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCode;
	private JTable table;
	private JTextField txtSelectClause;
	private JButton btnNew;
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnCancel;
	private JButton btnConfirm;
	private JButton btnSelect;
	private JTextArea txtDescription;
	private JSpinner txtStock;
	private JTextArea txtHelp;

	public FormItems(LinkarClt linkarClt) {
		setTitle("Items");
		clkitems = new CLkItems(linkarClt);
		setModal(true);
		setBounds(100, 100, 837, 582);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBounds(181, 58, 630, 383);
			contentPanel.add(panel);
			panel.setLayout(null);
			
			JLabel lblCode = new JLabel("Code");
			lblCode.setBounds(10, 11, 46, 14);
			panel.add(lblCode);
			
			JLabel lblDescription = new JLabel("Description");
			lblDescription.setBounds(10, 41, 74, 14);
			panel.add(lblDescription);
			
			JLabel lblStock = new JLabel("Stock");
			lblStock.setBounds(10, 116, 46, 14);
			panel.add(lblStock);
			
			txtCode = new JTextField();
			txtCode.setEnabled(false);
			txtCode.setBounds(107, 8, 153, 20);
			panel.add(txtCode);
			txtCode.setColumns(10);
			
			txtStock = new JSpinner();
			txtStock.setEnabled(false);
			txtStock.setBounds(107, 113, 86, 20);
			txtStock.setEditor(new JSpinner.NumberEditor(txtStock, "#.#"));
			panel.add(txtStock);
			
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(107, 35, 513, 67);
			panel.add(scrollPane_1);
			
			txtDescription = new JTextArea();
			txtDescription.setEnabled(false);
			scrollPane_1.setViewportView(txtDescription);
			
			JPanel panel_1 = new JPanel();
			panel_1.setBounds(10, 11, 801, 36);
			contentPanel.add(panel_1);
			
			btnSelect = new JButton("Select");
			btnSelect.setBounds(480, 5, 61, 23);
			btnSelect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					
					//Call to FindAll method of CLkItems class
					Exception error = clkitems.FindAll("", txtSelectClause.getText(), 0, 0, "", "");
					if (error != null)
					{
						javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(error), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						DefaultTableModel dm = (DefaultTableModel) table.getModel();
						int rowCount = dm.getRowCount();
						
						for (int i = rowCount - 1; i >= 0; i--) {
						    dm.removeRow(i);
						}
						Clear();
						//Draw items data
						if (clkitems != null)
						{	
							if (clkitems.LstErrors.size() > 0)
							{
								javax.swing.JOptionPane.showMessageDialog(null, String.join("\r\n", clkitems.LstErrors), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
							}
							if (clkitems.size() > 0)
							{
								for (int i = 0; i < clkitems.size(); i++)
								{
									String[] fila = new String[1];
									fila[0] = clkitems.get(i).getCode();
									((DefaultTableModel)table.getModel()).addRow(fila);
								}
								
								clkitem = clkitems.get(0);
								txtCode.setText(clkitem.getCode());
								txtDescription.setText(clkitem.getDescription());
								txtStock.setValue((int)clkitem.getStock());
								
								ChangeBarStatus(false,false);
							}
						}
					}
				}
			});
			panel_1.setLayout(null);
			
			btnNew = new JButton("New");
			btnNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//Create new CLkItem object
					clkitem = new CLkItem(clkitems.getLinkarClt(), true);
					Clear();
					ChangeBarStatus(true, true);
					
				}
			});
			btnNew.setBounds(10, 5, 53, 23);
			panel_1.add(btnNew);
			
			btnModify = new JButton("Modify");
			btnModify.setEnabled(false);
			btnModify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//Get current item
					if (clkitem == null)
						GetCurrentItem(false);
					ChangeBarStatus(true,false);
									
				}
			});
			btnModify.setBounds(68, 5, 65, 23);
			panel_1.add(btnModify);
			
			btnDelete = new JButton("Delete");
			btnDelete.setEnabled(false);
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//Get current item
					GetCurrentItem(false);

					//Call DeleteItem method from CLkItem
					Exception error = clkitem.DeleteRecord("");
					
					if (error != null)
					{
						javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(error), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						DefaultTableModel dm = (DefaultTableModel) table.getModel();
						int rowCount = dm.getRowCount();
						
						//Remove item from grid
						for (int i = rowCount - 1; i >= 0; i--) {
							String id = String.valueOf(table.getValueAt(i, 0));
							if (id.equals(clkitem.getCode()))
							{
								dm.removeRow(i);
								break;
							}				    
						}
						
						//Remove item from customers list
						for (int i = 0; i < clkitems.size(); i++)
				        {
				        	CLkItem c = clkitems.get(i);
				        	if (c.getCode().equals(clkitem.getCode()))
				        	{	  
				        		clkitems.remove(c);
				        		break;
				        	}
				        }
						Clear();
						}
				}
			});
			btnDelete.setBounds(138, 5, 63, 23);
			panel_1.add(btnDelete);
			panel_1.add(btnSelect);
			
			txtSelectClause = new JTextField();
			txtSelectClause.setToolTipText("Insert Code or Description");
			txtSelectClause.setBounds(294, 6, 176, 20);
			panel_1.add(txtSelectClause);
			txtSelectClause.setColumns(10);
			
			btnConfirm = new JButton("Confirm");
			btnConfirm.setEnabled(false);
			btnConfirm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//Save or create new item
					if (clkitem.getStatus() != StatusTypes.NEW)
					{
						if (clkitem != null)
						{
							//Get the changes
							GetChanges(clkitem);

							//Call the WriteItem method from CLkItem
							Exception error = clkitem.WriteRecord("");
							
							if (error != null)
							{
								javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(error), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
							}
							else
							{
								ChangeBarStatus(false,false);	
							}
						}
					}
					else
					{
						//Get the changes
						GetChanges(clkitem);
		
						//Call the WriteItem method from CLkCustomer
						Exception error = clkitem.NewRecord("");
						
						if (error != null)
						{
							javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(error), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							if (clkitem != null)
							{
								clkitems.add(clkitem);
								String[] fila = new String[1];
								fila[0] = clkitem.getCode();
								((DefaultTableModel)table.getModel()).addRow(fila);
								txtCode.setText(clkitem.getCode());
								txtDescription.setText(clkitem.getDescription());
								txtStock.setValue((int)clkitem.getStock());
							}
							ChangeBarStatus(false,false);	
						}
					}						
				}
			});
			btnConfirm.setBounds(724, 5, 69, 23);
			panel_1.add(btnConfirm);
			
			btnCancel = new JButton("Cancel");
			btnCancel.setEnabled(false);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Reject the changes in the class and the form	
					if (clkitem != null && clkitem.getStatus() != StatusTypes.NEW)
					{
						//Call the method Reject changes of CLkCustomer
						clkitem.RejectChanges();
						txtCode.setText(clkitem.getCode());
						txtDescription.setText(clkitem.getDescription());
						txtStock.setValue(clkitem.getStock());
					}
					else
						Clear();
					ChangeBarStatus(false,false);
					
				}
			});
			btnCancel.setBounds(649, 5, 65, 23);
			panel_1.add(btnCancel);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setEnabled(false);
			scrollPane.setBounds(10, 58, 160, 383);
			contentPanel.add(scrollPane);
			
			table = new JTable();
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Code"
				}
			) {				
				boolean[] columnEditables = new boolean[] {
					false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			}
			);
			ListSelectionModel listSelectionModel = table.getSelectionModel();
			class SharedListSelectionHandler implements ListSelectionListener {
		        public void valueChanged(ListSelectionEvent e) { 
		        	if (e.getValueIsAdjusting()) return;
		            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		            if (table.getModel() != null && table.getModel().getRowCount() > 0 && lsm != null && lsm.getMinSelectionIndex() > -1)
		            {
			            String id = String.valueOf(table.getModel().getValueAt(lsm.getMinSelectionIndex(), 0));	           
			            GetItem(id, true);
		            }
		        }
		    }
	        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
	        table.setSelectionModel(listSelectionModel);
			scrollPane.setViewportView(table);		
		}		
		
		txtHelp = new JTextArea();
		txtHelp.setEnabled(false);
		txtHelp.setEditable(false);
		txtHelp.setBounds(10, 449, 801, 83);
		contentPanel.add(txtHelp);
		
		txtHelp.setText("In this form the \"Select\" method is used with \"calculated\" option to load all data selected.\r\nIn the select box you can write a item description or id.\r\nTo load the data, a standard class is used that collects the MV buffer and assigns it to each property.\r\nThis class has CRUD methods to see how they work. ");
	}
	
	private void ChangeBarStatus(boolean inEditMode, boolean isNew)
	{
		if (inEditMode)
		{
			btnNew.setEnabled(false);
			btnModify.setEnabled(false);
			btnDelete.setEnabled(false);
			btnSelect.setEnabled(false);
			txtSelectClause.setEnabled(false);
			table.setEnabled(false);
			
			btnConfirm.setEnabled(true);
			btnCancel.setEnabled(true);
			txtCode.setEnabled(isNew);
			txtDescription.setEnabled(true);
			txtStock.setEnabled(true);
		}
		else
		{
			btnNew.setEnabled(true);
			btnModify.setEnabled(true);
			btnDelete.setEnabled(true);
			btnSelect.setEnabled(true);
			txtSelectClause.setEnabled(true);
			table.setEnabled(true);
			
			btnConfirm.setEnabled(false);
			btnCancel.setEnabled(false);
			txtCode.setEnabled(false);
			txtDescription.setEnabled(false);
			txtStock.setEnabled(false);
		}
	}
	
	private void GetCurrentItem(boolean refillData)
	{
		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();
		String id = String.valueOf(table.getValueAt(row, column));
		GetItem(id, refillData);
	}
	
	private void GetItem(String id,boolean refillData)
	{		
		for (int i = 0; i < clkitems.size(); i++)
        {
        	CLkItem c = clkitems.get(i);
        	if (c.getCode().equals(id))
        	{	  
        		if (refillData)
        		{
	        		txtCode.setText(c.getCode());
	        		txtDescription.setText(c.getDescription());
	        		txtStock.setValue((int)c.getStock());
        		}
        		clkitem = c;
        		break;
        	}
        }
	}
	
	private void GetChanges(CLkItem item) {
		item.setCode(txtCode.getText());
		item.setDescription(txtDescription.getText());
		item.setStock(Double.valueOf((int)txtStock.getValue()));
	}
	
	private void Clear() {
		txtCode.setText("");
		txtDescription.setText("");
		txtStock.setValue(0);
	}
}
