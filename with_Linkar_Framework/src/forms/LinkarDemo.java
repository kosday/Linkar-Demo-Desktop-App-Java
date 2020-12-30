//Demo main form

package forms;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import linkar.CredentialOptions;
import linkar.functions.*;
import linkar.LkException;
import linkar.functions.persistent.mv.LinkarClient;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class LinkarDemo {
	
	LinkarClient _LinkarClt = null; //NEWFRAMEWORK: Replace LinkarClt for LinkarClient
	public static String DataBaseType = "";

	private JFrame frmDemo;
	private JButton btnCustomers;
	private JButton btnItems;
	private JTextField txtUserName;
	private JTextField txtEntryPoint;
	private JTextField txtLanguage;
	private JTextField txtLinkarHost;
	private JButton btnLogout;
	private JButton btnLogin;
	private JButton btnOrdersList;
	private JButton btnOrdersDetails;
	private JPasswordField txtPassword;
	private JLabel lblFreeText;
	private JTextField txtFreeText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Windows".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		    Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH);
		} catch (Exception e) {

		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LinkarDemo window = new LinkarDemo();
					window.frmDemo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LinkarDemo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDemo = new JFrame();
		frmDemo.setTitle("Demo");
		frmDemo.setBounds(100, 100, 468, 274);
		frmDemo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDemo.getContentPane().setLayout(null);
				
		btnCustomers = new JButton("Customers");
		btnCustomers.setEnabled(false);
		btnCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Open Customers Form, the client object is passed as a parameter.
				try {
					FormCustomers window = new FormCustomers(_LinkarClt);
					window.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		btnCustomers.setBounds(10, 200, 89, 23);
		frmDemo.getContentPane().add(btnCustomers);
		
		btnItems = new JButton("Items");
		btnItems.setEnabled(false);
		btnItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Open Items Form, the client object is passed as a parameter.
				try {
					FormItems window = new FormItems(_LinkarClt);
					window.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		btnItems.setBounds(109, 200, 89, 23);
		frmDemo.getContentPane().add(btnItems);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 432, 178);
		frmDemo.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblUserName = new JLabel("Linkar User");
		lblUserName.setBounds(10, 70, 61, 14);
		panel.add(lblUserName);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(81, 67, 120, 20);
		panel.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(220, 70, 61, 14);
		panel.add(lblPassword);
		
		JLabel lblEntrypoint = new JLabel("EntryPoint");
		lblEntrypoint.setBounds(10, 42, 61, 14);
		panel.add(lblEntrypoint);
		
		txtEntryPoint = new JTextField();
		txtEntryPoint.setColumns(10);
		txtEntryPoint.setBounds(81, 39, 120, 20);
		panel.add(txtEntryPoint);
		
		JLabel lblLinkarHost = new JLabel("Linkar Host");
		lblLinkarHost.setBounds(10, 14, 61, 14);
		panel.add(lblLinkarHost);
		
		JLabel lblLinkarPort = new JLabel("EntryPoint Port");
		lblLinkarPort.setBounds(220, 42, 104, 14);
		panel.add(lblLinkarPort);
		
		JLabel lblLanguage = new JLabel("Language");
		lblLanguage.setBounds(10, 98, 61, 14);
		panel.add(lblLanguage);
		
		txtLanguage = new JTextField();
		txtLanguage.setColumns(10);
		txtLanguage.setBounds(81, 95, 120, 20);
		panel.add(txtLanguage);
		
		txtLinkarHost = new JTextField();
		txtLinkarHost.setColumns(10);
		txtLinkarHost.setBounds(81, 11, 120, 20);
		panel.add(txtLinkarHost);
		
		JSpinner txtLinkarPort = new JSpinner();
		txtLinkarPort.setBounds(334, 39, 77, 20);
		txtLinkarPort.setEditor(new JSpinner.NumberEditor(txtLinkarPort, "#"));
		panel.add(txtLinkarPort);
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String username = txtUserName.getText();
	            String password = txtPassword.getText();
	            String entrypoint = txtEntryPoint.getText();
	            String host = txtLinkarHost.getText();
	            int port = (int)txtLinkarPort.getValue();
	            String language = txtLanguage.getText();
	            String freeText = txtFreeText.getText();
	            
	            //Create CredentialsOptions object with connection data
	            CredentialOptions credentialsOptions = new CredentialOptions(host,entrypoint, (short)port, username, password,  language, freeText); //NEWFRAMEWORK: Replace CredentialsOptions for CredentialOptions
	            
	          //NEWFRAMEWORK: Replace LinkarClt for LinkarClient, add try/catch
	            
	            //Create LinkarClt client
				try {
					_LinkarClt = new LinkarClient();

					//Execute client Login
					String error = "";
					try {
						_LinkarClt.Login(credentialsOptions);
					} catch (Exception e2) {
						error = LinkarDemo.GetException(e2);
					}
					
					if (error == null || error == "")
					{
					    btnLogin.setEnabled(false);  
					    btnLogout.setEnabled(true); 
					    btnCustomers.setEnabled(true);
					    btnItems.setEnabled(true);
					    btnOrdersList.setEnabled(true);
					    btnOrdersDetails.setEnabled(true);
					    
					    txtUserName.setEnabled(false);
					    txtPassword.setEnabled(false);
					    txtEntryPoint.setEnabled(false);
					    txtLinkarHost.setEnabled(false);
					    txtLinkarPort.setEnabled(false);
					    txtLanguage.setEnabled(false);
					    txtFreeText.setEnabled(false);
					    		    
		                try {
							String lkstring = _LinkarClt.GetVersion(); //Replace GetVersion_Text for GetVersion, remove DATAFORMAT_TYPE.MV
						   	String[] parts = lkstring.split(ASCII_Chars.FS_str, -1);    	
					        if (parts.length >= 1)
					        {
					        	String[] ThisList = parts[0].split(DBMV_Mark.AM_str, -1);
					            int numElements = ThisList.length;
					            for (int i = 1; i < numElements; i++)
					            {                	
					            	if (ThisList[i].equals("RECORD"))
					            	{
					            		String data = parts[i];
					            		String[] values = data.split(DBMV_Mark.AM_str, -1);
					            		DataBaseType = values[3];
					            		break;
					            	}
					            }
					        }					
						} catch (Exception e1) {
							javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(e1), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
					    javax.swing.JOptionPane.showMessageDialog(null, error, "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e3) {
					javax.swing.JOptionPane.showMessageDialog(null, LinkarDemo.GetException(e3), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
				} 
								
			}
		});
		btnLogin.setBounds(77, 144, 89, 23);
		panel.add(btnLogin);
		
		btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Close the client object connection
				String error = "";
				try {
					_LinkarClt.Logout(); //Remove error return
				} catch (Exception e1) {
					error = LinkarDemo.GetException(e1);
				}
	            if (error == "" || error == null)
	            {
	            	btnLogout.setEnabled(false);
	                btnCustomers.setEnabled(false);
	                btnItems.setEnabled(false);
	                btnOrdersList.setEnabled(false);
	                btnOrdersDetails.setEnabled(false);
	            	btnLogin.setEnabled(true);  
	            	
	            	txtUserName.setEnabled(true);
	                txtPassword.setEnabled(true);
	                txtEntryPoint.setEnabled(true);
	                txtLinkarHost.setEnabled(true);
	                txtLinkarPort.setEnabled(true);
	                txtLanguage.setEnabled(true);
	                txtFreeText.setEnabled(true);

	            }
	            else
	            	javax.swing.JOptionPane.showMessageDialog(null, error, "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
				
			}
		});
		btnLogout.setEnabled(false);
		btnLogout.setBounds(264, 144, 89, 23);
		panel.add(btnLogout);
		
        txtPassword = new JPasswordField();
        txtPassword.setBounds(291, 67, 120, 20);
        panel.add(txtPassword);
        
        lblFreeText = new JLabel("Free Text");
        lblFreeText.setBounds(220, 98, 61, 14);
        panel.add(lblFreeText);
        
        txtFreeText = new JTextField();
        txtFreeText.setColumns(10);
        txtFreeText.setBounds(291, 95, 120, 20);
        panel.add(txtFreeText);      	    
        
        btnOrdersList = new JButton("Orders List");
        btnOrdersList.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		//Open OrdersView Form, the client object is passed as a parameter.
        		try {
					FormOrdersView window = new FormOrdersView(_LinkarClt);
					window.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
        	}
        });
        btnOrdersList.setEnabled(false);
        btnOrdersList.setBounds(332, 200, 110, 23);
        frmDemo.getContentPane().add(btnOrdersList);
        
        btnOrdersDetails = new JButton("Orders Details");
        btnOrdersDetails.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		//Open OrdersDetails Form, the client object is passed as a parameter.
        		try {
        			FormOrdesDetails window = new FormOrdesDetails(_LinkarClt);
					window.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
        	}
        });
        btnOrdersDetails.setEnabled(false);
        btnOrdersDetails.setBounds(208, 200, 114, 23);
        frmDemo.getContentPane().add(btnOrdersDetails);
        
        JLabel lblDescription = new JLabel("");
        lblDescription.setVerticalAlignment(SwingConstants.TOP);
        lblDescription.setBounds(10, 11, 432, 5);
        frmDemo.getContentPane().add(lblDescription);
	}
	
	public static String GetException(Exception ex) {
        String msg = "";
        if (ex instanceof LkException)
        {
        	LkException lkex = (LkException)ex;
        if (lkex.getErrorCode() == LkException.ERRORCODE.C0003)
            msg = "LINKAR EXCEPTION ERROR" + 
            	  "\r\nERROR CODE: " + lkex.getErrorCode() +
                  "\r\nERROR MESSAGE: " + lkex.getErrorMessage() +
                  "\r\nInternal ERROR CODE: " + lkex.getInternalCode() +
                  "\r\nInternal ERROR MESSAGE: " + lkex.getInternalMessage();
        else
            msg = "LINKAR EXCEPTION ERROR" + 
            		"\r\nERROR CODE: " + lkex.getErrorCode() +
                  "\r\nERROR MESSAGE: " + lkex.getErrorMessage();
        }
        else if (ex instanceof SocketException)
        {
        	msg = "SOCKET EXCEPTION ERROR\r\n" + ex.getMessage();
        }
        else
        {
        	msg = "EXCEPTION ERROR\r\n" + ex.getMessage();
        }

        return msg;
	}
}
