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
import linkar.LinkarClt;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class FormOrdersView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	int currentPage = 1;
	int numRegforPag = 100;
	
	CLkOrders clkorders = null;
	private JTextField txtSelect;
	private JTable table;
	

	/**
	 * Create the dialog.
	 */
	public FormOrdersView(LinkarClt linkarClt) {
		setTitle("Orders Lists");
		setModal(true);
		clkorders = new CLkOrders(linkarClt);
		setBounds(100, 100, 864, 597);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 828, 33);
		contentPanel.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		txtSelect = new JTextField();
		txtSelect.setToolTipText("Insert Code or Empty");
		panel.add(txtSelect);
		txtSelect.setColumns(50);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtSelect.setText("");
				
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int rowCount = dm.getRowCount();
		
				for (int i = rowCount - 1; i >= 0; i--) {
				    dm.removeRow(i);
				}
				
				clkorders.clear();
			}
		});
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				Select(1);
										
			}
		});
		panel.add(btnSelect);
		panel.add(btnClear);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 55, 828, 371);
		contentPanel.add(scrollPane);
		
		table = new JTable();		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Code", "Customer", "Customer Name", "Date", "Total Order"
			}
		));
		table.getColumnModel().getColumn(2).setPreferredWidth(163);
		ListSelectionModel listSelectionModel = table.getSelectionModel();
		class SharedListSelectionHandler implements ListSelectionListener {
	        public void valueChanged(ListSelectionEvent e) { 
	        	if (e.getValueIsAdjusting()) return;	        	
	            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	            if (table.getModel() != null && table.getModel().getRowCount() > 0 && lsm != null && lsm.getMinSelectionIndex() > -1)
	            {
	            	System.out.println("table SELECTIONCHANGE");
		            String id = String.valueOf(table.getModel().getValueAt(lsm.getMinSelectionIndex(), 0));
		            
		            for (int i = 0; i < clkorders.size(); i++)
			        {
		            	CLkOrder item = clkorders.get(i);
			        	if (id.equals(item.getCode()))
			        	{ 
			        		FormOrdersViewMV window = new FormOrdersViewMV(item);
							window.setVisible(true);
			        		break;
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
		txtHelp.setBounds(10, 464, 828, 83);
		contentPanel.add(txtHelp);
		
		txtHelp.setText("In this form the \"Select\" method is used with \"calculated\" option to load the grid.\r\nIn the select box you can write an order code to go directly to it\r\nTo load the data, a standard class is used that collects the MV buffer and assigns it to each property.\r\nClicking on each line, you can view the Multivalue and Subvalue groups.");
		
		JButton btnLeft = new JButton("<--");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (currentPage -1 > 0)
					Select(currentPage -1);
				
			}
		});
		btnLeft.setBounds(10, 430, 397, 23);
		contentPanel.add(btnLeft);
		
		JButton btnRight = new JButton("-->");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				double t =  (clkorders.totalRecords / numRegforPag);
				if (clkorders != null && (currentPage +1) <= t)
					Select(currentPage +1);
				
			}
		});
		btnRight.setBounds(417, 430, 421, 23);
		contentPanel.add(btnRight);
	}
	
	private void Select(int numPag) {
		
		String sel = txtSelect.getText();
		currentPage = numPag;
		Exception error = null;
		//Call to FindAll or SelectAll method of CLkOrders class
		if (sel != null && !sel.isEmpty())
			error = clkorders.FindAll("", txtSelect.getText(), numRegforPag, numPag, "", "", false, true, true);
		else
			error = clkorders.SelectAll("","", numRegforPag, numPag, false, true, true);
		
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
						CLkOrder item = clkorders.get(i);
						((DefaultTableModel)table.getModel()).addRow(new Object[]{
								item.getCode(),
								item.getCustomer(),
								item.getICustomerName(),
								item.getDate() != null ? df.format(item.getDate()) : "",								
								String.valueOf(item.getITotalOrder())
								});
					}	
				}
			}	
		}
	}
}
