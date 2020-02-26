package forms;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bl.MV_LstItems_CLkOrder;
import bl.SV_LstPartial_CLkOrder;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class FormOrdersViewSV extends JDialog {
	
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

	private final JPanel contentPanel = new JPanel();
	private JTable table;


	/**
	 * Create the dialog.
	 */
	public FormOrdersViewSV(MV_LstItems_CLkOrder mv) {
		setTitle("SubValue");
		setModal(true);
		setBounds(100, 100, 837, 491);
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
						"Delivery Date", "Partial Quantity"
					}
				) {
					boolean[] columnEditables = new boolean[] {
						false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
				scrollPane.setViewportView(table);
				
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int rowCount = dm.getRowCount();

				for (int i = rowCount - 1; i >= 0; i--) {
				    dm.removeRow(i);
				}
				if (mv.getLstLstPartial() != null && mv.getLstLstPartial().size() > 0)
				{					
					for (int i = 0; i < mv.getLstLstPartial().size(); i++)
					{
						SV_LstPartial_CLkOrder sv = mv.getLstLstPartial().get(i);		
						((DefaultTableModel)table.getModel()).addRow(new Object[]{
								sv.getDeliveryDate() != null ? df.format(sv.getDeliveryDate()) : "",
								sv.getPartialQuantity(),
								});						
					}									
				}	
			}
		}
	}

}
