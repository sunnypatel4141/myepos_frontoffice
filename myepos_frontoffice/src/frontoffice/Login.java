/*
 * Copyright (C) 2014 Sunny Patel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package frontoffice;
import frontoffice.base.DBConnection;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import recovery.schemachecker;
/**
 *
 * @author Sunny Patel
 */
public class Login extends DBConnection implements ActionListener, FocusListener {
    // This is the login page
    JFrame frame = new JFrame("System Login");
    JPanel inputFldPnl, numPadPnl, detailPnl, optsPnl;
    String[] numberbtns = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "CLEAR", "OK"};
    JButton[] numbers = new JButton[numberbtns.length];
    JTextField unFld, swpFld;
    JPasswordField pwFld;
    Integer focusfield;
    JButton exitBtn, recovery;
    // All the user information
    HashMap userdata = new HashMap();
    
    void render() {
        // Render All Panels
        inputFldPnl = new JPanel();
        numPadPnl = new JPanel();
        detailPnl = new JPanel();
        optsPnl = new JPanel();
        exitBtn = new JButton("Exit");
        exitBtn.addActionListener(this);
        recovery = new JButton("Recovery");
        recovery.addActionListener(this);
        JLabel unFldLbl = new JLabel("User Name");
        unFld = new JTextField(14);
        unFld.addFocusListener(this);
        JLabel pwFldLbl = new JLabel("Password");
        pwFld = new JPasswordField(14);
        pwFld.addFocusListener(this);
        
        detailPnl.add(unFldLbl);
        detailPnl.add(unFld);
        detailPnl.add(pwFldLbl);
        detailPnl.add(pwFld);
        
        optsPnl.add(exitBtn);
        optsPnl.add(recovery);
        
        // Numpad Render
        for ( int i = 0; i < numberbtns.length; i++ ) {
            //numbers = new JButton[j];
            numbers[i] = new JButton("" + numberbtns[i]);
            numbers[i].addActionListener(this);
            numPadPnl.add(numbers[i]);
        }
        numPadPnl.setLayout(new GridLayout(4, 3));
        numPadPnl.setPreferredSize(new Dimension(300, 300));
        String address = "";
        try {
            InetAddress addObj = InetAddress.getLocalHost();
            address = "IP: " + addObj.toString();
        } catch( UnknownHostException a ) {
            address = "IP: Unknown";
        }
        JLabel detailsLbl = new JLabel(address);
        
        frame.add(detailsLbl);
        frame.add(detailPnl);
        frame.add(numPadPnl);
        frame.add(optsPnl);
        frame.setLayout(new FlowLayout());
        frame.setSize(724, 186);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    void clearDown() {
        frame.remove(detailPnl);
        frame.remove(numPadPnl);
        frame.remove(optsPnl);
        frame.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object aeObj = ae.getSource();
        if ( aeObj == exitBtn ) {
            clearDown();
            System.gc();
            System.exit(0);
        } else if ( aeObj == recovery ) {
            schemachecker sch = new schemachecker();
            sch.checkTables();
        }
        // Lets Iterate through the loop
        for(int i = 0; i < numberbtns.length; i++) {
            if ( aeObj.equals(numbers[i]) && !numberbtns[i].equals("CLEAR") && !numberbtns[i].equals("OK") ) {
                // for userName Field
                if ( focusfield == 1 ) {
                    String currentUNStr = unFld.getText();
                    String unStr = new StringBuffer().append(currentUNStr).append(numberbtns[i]).toString();
                    unFld.setText(unStr);
                } else {
                    String currentPWStr = pwFld.getText();
                    String pwStr = new StringBuffer().append(currentPWStr).append(numberbtns[i]).toString();
                    pwFld.setText(pwStr);
                }
                break;
            } else if ( aeObj.equals(numbers[i]) && numberbtns[i].equals("CLEAR") ) {
                if ( focusfield == 1 ) {
                    unFld.setText("");
                } else {
                    pwFld.setText("");
                }
                break;
            } else if ( aeObj.equals(numbers[i]) && numberbtns[i].equals("OK") ) {
                if ( !unFld.getText().equals("") || !pwFld.getText().equals("") ) {
                    authenticate();
                } else {
                    JOptionPane.showMessageDialog(null, "Must enter username and password");
                }
                break;          
            }
        }
    }
    
    void authenticate() {
        try {
            String unStr = unFld.getText();
            String pwStr = pwFld.getText();
            String sql = "select * from individual where loginID='" + unStr + 
                    "' and loginpass='" + pwStr  +"' and canlogin='1';";
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql); 
            while ( rs.next() ) {
                userdata.put("username", rs.getString("firstname"));
                userdata.put("userid", rs.getString("id"));
            }
            rs.close();
            if ( userdata.get("username") == null ) {
                JOptionPane.showMessageDialog(frame, "Invalid user credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
            } else {
                //
                // This is a view
                sql = "select r, w, c, d from rights where loginID='" + 
                        userdata.get("userid").toString() + "' and apid = " +
                        "(select id from apps where code = 'saleswindow')";
                rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    userdata.put("saleswindow", rs.getString("r"));
                }
                rs.close();
                if ( userdata.get("saleswindow") == null ) {
                    JOptionPane.showMessageDialog(frame, "Access Denied", "Access Denied", JOptionPane.ERROR_MESSAGE);
                } else {
System.out.println("Login Completed");
                    login();
                }
            }
        } catch ( Exception a ) {
            JOptionPane.showMessageDialog(frame, "Unable to Connect", "Login Error", JOptionPane.ERROR_MESSAGE);
            a.printStackTrace();
        }
    }
    
    void login() {
        clearDown();
        // Login to the main application
        salesWindow sw = new salesWindow();
        sw.render(userdata);
    }
    
    @Override
    public void focusGained(FocusEvent fe) {
        if ( fe.getSource() == unFld ) {
            focusfield = 1;
        } else {
            focusfield = 2;
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static void main(String[] args) {
        frontoffice fo = new frontoffice();
        fo.render();
    }
}
