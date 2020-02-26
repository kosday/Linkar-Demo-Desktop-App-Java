package forms;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import bl.CLkOrder;
import bl.MV_LstItems_CLkOrder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class FormOrdersViewMV extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;


	/**
	 * Create the dialog.
	 */
	public FormOrdersViewMV(CLkOrder item) {
		setTitle("MultiValue");
		setModal(true);
		setBounds(100, 100, 796, 472);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 11, 760, 411);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
							"Item", "Item Description", "Item Stock", "Quantity", "Price", "Total Line"
						}
					));
				ListSelectionModel listSelectionModel = table.getSelectionModel();
				class SharedListSelectionHandler implements ListSelectionListener {
			        public void valueChanged(ListSelectionEvent e) { 
			        	if (e.getValueIsAdjusting()) return;	        	
			            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			            if (table.getModel() != null && table.getModel().getRowCount() > 0 && lsm != null && lsm.getMinSelectionIndex() > -1)
			            {
			            	System.out.println("table SELECTIONCHANGE");
				            String id = String.valueOf(table.getModel().getValueAt(lsm.getMinSelectionIndex(), 0));
				            
				            for (int i = 0; i < item.getLstLstItems().size(); i++)
					        {
				            	MV_LstItems_CLkOrder mv = item.getLstLstItems().get(i);
					        	if (id.equals(mv.getItem()))
					        	{ 
					        		FormOrdersViewSV window = new FormOrdersViewSV(mv);
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
				
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int rowCount = dm.getRowCount();

				for (int i = rowCount - 1; i >= 0; i--) {
				    dm.removeRow(i);
				}
				if (item.getLstLstItems() != null && item.getLstLstItems().size() > 0)
				{					
					for (int i = 0; i < item.getLstLstItems().size(); i++)
					{
						MV_LstItems_CLkOrder mv = item.getLstLstItems().get(i);		
						((DefaultTableModel)table.getModel()).addRow(new Object[]{
								mv.getItem(),
								mv.getIItemDescription(),
								mv.getIItemStock(),
								mv.getQty(),								
								mv.getPrice(),
								mv.getITotalLine()
								});						
					}									
				}	
			}
		}
	}

}
