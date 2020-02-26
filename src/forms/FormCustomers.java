//Customers Form

package forms;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import bl.CLkCustomer;
import bl.CLkCustomers;
import linkar.MainClass.StatusTypes;
import linkar.DBMV_Mark;
//import linkar.GenericError;
import linkar.LinkarClt;

public class FormCustomers extends JDialog {
	
	CLkCustomers clkcustomers = null;
	CLkCustomer clkcustomer = null;

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
	private JTextField txtPhone;
	private JTextArea txtAddress;
	private JLabel lblName;
	private JTextField txtName;
	private JTextArea txtHelp;

	public FormCustomers(LinkarClt linkarClt) {
		setTitle("Customers");
		clkcustomers = new CLkCustomers(linkarClt);
		setModal(true);
		setBounds(100, 100, 837, 587);
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
			
			txtCode = new JTextField();
			txtCode.setEnabled(false);
			txtCode.setBounds(107, 8, 153, 20);
			panel.add(txtCode);
			txtCode.setColumns(10);
			
			JLabel lblAddress = new JLabel("Address");
			lblAddress.setBounds(10, 76, 46, 14);
			panel.add(lblAddress);
			
			JLabel lblPhone = new JLabel("Phone");
			lblPhone.setBounds(10, 165, 46, 14);
			panel.add(lblPhone);
			
			txtPhone = new JTextField();
			txtPhone.setEnabled(false);
			txtPhone.setColumns(10);
			txtPhone.setBounds(107, 162, 153, 20);
			panel.add(txtPhone);
			
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(107, 76, 513, 75);
			panel.add(scrollPane_1);
			
			txtAddress = new JTextArea();
			scrollPane_1.setViewportView(txtAddress);
			
			lblName = new JLabel("Name");
			lblName.setBounds(10, 42, 46, 14);
			panel.add(lblName);
			
			txtName = new JTextField();
			txtName.setEnabled(false);
			txtName.setColumns(10);
			txtName.setBounds(107, 39, 153, 20);
			panel.add(txtName);
			
			JPanel panel_1 = new JPanel();
			panel_1.setBounds(10, 11, 801, 36);
			contentPanel.add(panel_1);
			
			btnSelect = new JButton("Select");
			btnSelect.setBounds(462, 5, 61, 23);
			btnSelect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					
					//Call to FindAll method of CLkCustomers class
					Exception error = clkcustomers.FindAll("", txtSelectClause.getText(), 0, 0, "", "");
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
						//Draw customers data
						if (clkcustomers != null)
						{
							if (clkcustomers.LstErrors.size() > 0)
							{
								javax.swing.JOptionPane.showMessageDialog(null, String.join("\r\n", clkcustomers.LstErrors), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
							}
							if (clkcustomers.size() > 0)
							{					
								for (int i = 0; i < clkcustomers.size(); i++)
								{
									String[] fila = new String[1];
									fila[0] = clkcustomers.get(i).getCode();
									((DefaultTableModel)table.getModel()).addRow(fila);
								}
								
								clkcustomer = clkcustomers.get(0);
								txtCode.setText(clkcustomer.getCode());
								txtName.setText(clkcustomer.getName());
								txtAddress.setText(clkcustomer.getAddress());
								txtPhone.setText(clkcustomer.getPhone());
								
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
					
					//Create new CLkCustomer object
					clkcustomer = new CLkCustomer(clkcustomers.getLinkarClt(), true);
					Clear();
					ChangeBarStatus(true,true);
					
				}
			});
			btnNew.setBounds(10, 5, 53, 23);
			panel_1.add(btnNew);
			
			btnModify = new JButton("Modify");
			btnModify.setEnabled(false);
			btnModify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//Get current customer
					if (clkcustomer == null)
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
					
					//Get current customer
					GetCurrentItem(false);

					//Call DeleteItem method from CLkCustomer
					Exception error = clkcustomer.DeleteRecord("");
					
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
							if (id.equals(clkcustomer.getCode()))
							{
								dm.removeRow(i);
								break;
							}				    
						}
						
						//Remove customer from customers list
						for (int i = 0; i < clkcustomers.size(); i++)
				        {
				        	CLkCustomer c = clkcustomers.get(i);
				        	if (c.getCode().equals(clkcustomer.getCode()))
				        	{	  
				        		clkcustomers.remove(c);
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
			txtSelectClause.setToolTipText("Insert Code or Name");
			txtSelectClause.setBounds(276, 6, 176, 20);
			panel_1.add(txtSelectClause);
			txtSelectClause.setColumns(10);
			
			btnConfirm = new JButton("Confirm");
			btnConfirm.setEnabled(false);
			btnConfirm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Save or create new item
					if (clkcustomer.getStatus() != StatusTypes.NEW)
					{
						if (clkcustomer != null)
						{
							//Get the changes
							GetChanges(clkcustomer);

							//Call the WriteItem method from CLkCustomer
							Exception error = clkcustomer.WriteRecord("");
							
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
						GetChanges(clkcustomer);
	
						//Call the WriteItem method from CLkCustomer
						Exception error = clkcustomer.NewRecord("");
						
						if (error != null)
						{
							javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(error), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							if (clkcustomer != null)
							{								
								clkcustomers.add(clkcustomer);
								String[] fila = new String[1];
								fila[0] = clkcustomer.getCode();
								((DefaultTableModel)table.getModel()).addRow(fila);
								txtCode.setText(clkcustomer.getCode());
								txtName.setText(clkcustomer.getName());
								txtAddress.setText(clkcustomer.getAddress());
								txtPhone.setText(clkcustomer.getPhone());	
							}
							ChangeBarStatus(false,false);	
						}
					}															
				}
			});
			btnConfirm.setBounds(718, 5, 69, 23);
			panel_1.add(btnConfirm);
			
			btnCancel = new JButton("Cancel");
			btnCancel.setEnabled(false);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Reject the changes in the class and the form								
					if (clkcustomer != null && clkcustomer.getStatus() != StatusTypes.NEW)
					{
						//Call the method Reject changes of CLkCustomer
						clkcustomer.RejectChanges();
						txtCode.setText(clkcustomer.getCode());
						txtName.setText(clkcustomer.getName());
						txtAddress.setText(clkcustomer.getAddress());
						txtPhone.setText(clkcustomer.getPhone());						
					}
					else
						Clear();
					ChangeBarStatus(false,false);
					
				}
			});
			btnCancel.setBounds(643, 5, 65, 23);
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
		txtHelp.setBounds(10, 454, 801, 83);
		contentPanel.add(txtHelp);
		
		txtHelp.setText("In this form the \"Select\" method is used with \"calculated\" option to load all data selected.\r\nIn the select box you can write a customer name or id.\r\nTo load the data, a standard class is used that collects the MV buffer and assigns it to each property.         \r\nThis class has CRUD methods to see how they work.");
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
			txtName.setEnabled(true);
			txtAddress.setEnabled(true);
			txtPhone.setEnabled(true);
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
			txtName.setEnabled(false);
			txtAddress.setEnabled(false);
			txtPhone.setEnabled(false);
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
		for (int i = 0; i < clkcustomers.size(); i++)
        {
        	CLkCustomer c = clkcustomers.get(i);
        	if (c.getCode().equals(id))
        	{	  
        		if (refillData)
        		{
	        		txtCode.setText(c.getCode());
	        		txtName.setText(c.getName());
	        		txtAddress.setText(c.getAddress());
	        		txtPhone.setText(c.getPhone());
        		}
        		clkcustomer = c;
        		break;
        	}
        }
	}
	
	private void GetChanges(CLkCustomer item) {
		item.setCode(txtCode.getText());
		item.setName(txtName.getText());
		item.setAddress(txtAddress.getText());
		item.setPhone(txtPhone.getText());
	}
	
	private void Clear() {
		txtCode.setText("");
		txtName.setText("");
		txtAddress.setText("");
		txtPhone.setText("");
	}
}
