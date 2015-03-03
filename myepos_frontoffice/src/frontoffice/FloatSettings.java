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

import frontoffice.event.NumberPadEvent;
import frontoffice.util.NumberPad;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 * A FloatSettings is intended to give exact reading of the float 
 * at any given time. It will keep track of the float in the background.
 * NOTE:: IF YOU WANT TO ADJUST THE FLOAT OPENING BALANCE THEN 
 * IT MUST BE ZERO BEFORE IT CAN BE ADJUSTED
 * 
 * @author Sunny Patel
 */

/*
THOUGHT::::: MAYBE THERE SHOULD BE A FLOAT HISTRY TABLE THAT KEEPS TRACK OF ALL THE VALUES in the table
THE FLOAT SETTING SHOULD BE IN SYSTEMPREF table

*/
public class FloatSettings extends MainMenu implements NumberPadEvent, 
        ActionListener, MouseListener, FocusListener {
    // 
    JDialog frameFloatSettings = new JDialog(frame, "Float Settings", true);
    JDialog approveFrame = new JDialog(frameFloatSettings, "Login", true);
    JButton closeFloat, saveFloat, numpad;
    JTextField currentBalance, openingBalance, cashLift, cashInput, unFld;
    JPasswordField pwFld;
    String floatamountopening = "";
    String floatamount = "";
    int focusmapper = 0; // For Approval login thing
    int amountmapper = 0; // For amount mapper 
    
    public FloatSettings(String faArg, String faoArg) {
        floatamount = faArg;
        floatamountopening = faoArg;
        // Call the render function
        renderFloatSettings();
    }
    
    private void renderFloatSettings() {
        JPanel btnsPnl = new JPanel();
        JPanel boxPnl = new JPanel();
        
        JLabel cbLbl = new JLabel("Current Balance");
        JLabel obLbl = new JLabel("Opening Balance");
        JLabel clLbl = new JLabel("Cash Lift");
        JLabel chLbl = new JLabel("Cash Input");
        JLabel fltLbl = new JLabel("Float Limit");
        JTextField fltFld = new JTextField(7);
        
        currentBalance = new JTextField("0.00", 7);
        currentBalance.setFont(large);
        openingBalance = new JTextField("0.00", 7);
        openingBalance.setFont(large);
        openingBalance.addMouseListener(this);
        cashLift = new JTextField("0.00", 7);
        cashLift.addMouseListener(this);
        cashLift.setFont(large);
        cashInput = new JTextField("0.00", 7);
        cashInput.addMouseListener(this);
        cashInput.setFont(large);
        fltFld.setFont(large);
        
        currentBalance.setText(floatamount);
        openingBalance.setText(floatamountopening);
        fltFld.setText(Settings.get("floatlimit").toString());
        
        boxPnl.add(cbLbl);
        boxPnl.add(currentBalance);
        boxPnl.add(obLbl);
        boxPnl.add(openingBalance);
        boxPnl.add(clLbl);
        boxPnl.add(cashLift);
        boxPnl.add(chLbl);
        boxPnl.add(cashInput);
        boxPnl.add(fltLbl);
        boxPnl.add(fltFld);
        boxPnl.setLayout(new GridLayout(3, 2));
        
        closeFloat = new JButton("Close");
        closeFloat.addActionListener(this);
        saveFloat = new JButton("Save");
        saveFloat.addActionListener(this);
        
        btnsPnl.add(closeFloat);
        btnsPnl.add(saveFloat);
        
        frameFloatSettings.add(boxPnl);
        frameFloatSettings.add(btnsPnl);
        frameFloatSettings.setSize(500, 200);
        frameFloatSettings.setLocation(300, 200);
        frameFloatSettings.setLayout(new FlowLayout());
        frameFloatSettings.setVisible(true);
    }
    
    // Save Float Settings
    private void saveFloatSettings() {
        // Get the float Settings        
        String userName = Settings.get("username").toString();
        String userId = Settings.get("userid").toString();
        
        String[] btnsTxt = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "CLEAR", "OK"};
        JButton[] nBtns = new JButton[btnsTxt.length];
        
        JLabel unLbl = new JLabel("User Name");
        unFld = new JTextField(7);
        unFld.addFocusListener(this);
        JLabel pwLbl = new JLabel("Password");
        pwFld = new JPasswordField(7);
        pwFld.addFocusListener(this);
        
        JPanel btnsPnl = new JPanel();
        JPanel fldPnl = new JPanel();
        JPanel btmPnl = new JPanel();
        
        for(int i = 0; i < btnsTxt.length; i++) {
            final String number = btnsTxt[i];
            nBtns[i] = new JButton(btnsTxt[i]);
            nBtns[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    addNumberToField(number);
                }
            });
            btnsPnl.add(nBtns[i]);
        }
        btnsPnl.setLayout(new GridLayout(4, 3));
        btnsPnl.setPreferredSize(new Dimension(300, 300));
        
        JButton closeApprove = new JButton("Close");
        closeApprove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                approvalCleanup();
            }
        });
        btmPnl.add(closeApprove);
        
        fldPnl.add(unLbl);
        fldPnl.add(unFld);
        fldPnl.add(pwLbl);
        fldPnl.add(pwFld);
        fldPnl.setLayout(new GridLayout(4, 1));
        approveFrame.add(fldPnl);
        approveFrame.add(btnsPnl);
        approveFrame.add(btmPnl);
        approveFrame.setSize(300, 450);
        approveFrame.setLayout(new FlowLayout());
        approveFrame.setLocation(400, 150);
        approveFrame.setVisible(true);
    }
    
    private void approvalCleanup() {
        // Cleanup Approval
        Component[] comp = approveFrame.getComponents();
        for(int i = 0; i < comp.length; i ++) {
            approveFrame.remove(comp[i]);
        }
        approveFrame.dispose();
    }
    
    private void addNumberToField(String numArg) {
        if(numArg.equals("CLEAR")) {
            // Clear the field in focus
            if( focusmapper == 0 ) {
                unFld.setText("");
            } else {
                pwFld.setText("");
            }
        } else if (numArg.equals("OK")) {
            String unStr = unFld.getText();
            String pwStr = pwFld.getText();
            approve(unStr, pwStr);
        } else {
            String unStr = unFld.getText();
            String pwStr = pwFld.getText();
            if(focusmapper == 0) {
                String unStrComp = new StringBuffer(unStr).append(numArg).toString();
                unFld.setText(unStrComp);
            } else {
                String pwStrComp = new StringBuffer(pwStr).append(numArg).toString();
                pwFld.setText(pwStrComp);
            }
        }
    }
    
    private void approve(String pw, String un) {
        try {
            boolean found = false;
            String userid = "";
            // Connect and Authorise
            String sql = "select * from users where loginID= ? and loginpass= ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pw);
            pstmt.setString(2, un);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                found = true;
                userid = rs.getString("id");
            }
            if(found) {
                // Update the Settings Object
                float currentAmount = Float.parseFloat(Settings.get("floatamount").toString());
                float liftBalance = Float.parseFloat(cashLift.getText());
                float inputBalance = Float.parseFloat(cashInput.getText());
                float newBalance = currentAmount - liftBalance + inputBalance;
                // Sometimes we need to adjust the opening balance
                sql = "update cashdrawer set amount = ? where `register` = ? and `created` = curDate() - interval 1 day and `userid` = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, openingBalance.getText());
                pstmt.setString(2, Settings.get("tillNumber").toString());
                pstmt.setString(3, Settings.get("userid").toString());
                pstmt.execute();
                // Need to update DB with details
                sql = "update cashdrawer set amount = ? where `register` = ? and `created` = curDate() and `userid` = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setFloat(1, newBalance);
                pstmt.setString(2, Settings.get("tillNumber").toString());
                pstmt.setString(3, Settings.get("userid").toString());
                pstmt.execute();
                // Now Tell Float History what happened
                sql = "insert into cashdrawerhistory (register, openingbalance, " +
                        "cashlift, cashinput, currentbalance, createdby, approverid) " +
                        "values(?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, Settings.get("tillNumber").toString());
                pstmt.setString(2, openingBalance.getText());
                pstmt.setString(3, cashLift.getText());
                pstmt.setString(4, cashInput.getText());
                pstmt.setString(5, currentBalance.getText());
                pstmt.setString(6, Settings.get("userid").toString());
                pstmt.setString(7, userid);
                pstmt.execute();
                JOptionPane.showMessageDialog(approveFrame, "APPROVAL GRANTED");
                // Update the balance
                Settings.put("floatamount", newBalance);
                Settings.put("floatamountopening", openingBalance.getText());
                approvalCleanup();
            } else {
                // Else Just send an error message to retry
                JOptionPane.showMessageDialog(approveFrame, "Cannot verify user!", "User error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception a) {
            a.printStackTrace();
            JOptionPane.showMessageDialog(approveFrame, "APPROVAL ERROR", "APPROVAL ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object trigger = ae.getSource();
        if( trigger == closeFloat ) {
            // Close Float
            frameFloatSettings.dispose();
        } else if ( trigger == saveFloat ) {
            // Save Float
            saveFloatSettings();
        }
    }

    @Override
    public void numberPadEvent(String returnArg) {
        try {
            float retFlt = Float.parseFloat(returnArg) / 100;
            if(amountmapper == 0) {
                // This is the cashlift
                cashLift.setText("" + retFlt);
            } else if( amountmapper == 1) {
                // This is the cash input
                cashInput.setText("" + retFlt);
            } else if( amountmapper == 2 ) {
                openingBalance.setText("" + retFlt);
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    // Cos we only want to trigger on focus gained else send to parent
    @Override
    public void focusGained(FocusEvent fe) {
        if(fe.getSource() == unFld) {
            focusmapper = 0;
            System.out.println("Focus is unfld");
        } else if (fe.getSource() == pwFld) {
            focusmapper = 1;
            System.out.println("Focus is pwfld");
        }
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object trigger = e.getSource();
        if (trigger == cashLift) {
            amountmapper = 0;
            NumberPad np = new NumberPad(this, frameFloatSettings);
        } else if( trigger == cashInput) {
            amountmapper = 1;
            NumberPad np = new NumberPad(this, frameFloatSettings);
        } else if( trigger == openingBalance) {
            amountmapper = 2;
            NumberPad np = new NumberPad(this, frameFloatSettings);
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}
