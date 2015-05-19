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
import frontoffice.event.MiscButtonEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Sunny Patel
 */
public class MiscButtons extends DBConnection  implements MiscButtonEvent {

    int taxBtnCount = 25;
    int nTaxBtnCount = 25;
    int poBtnCount = 25;
    
    private MiscButtonEvent mbe = null;
    
    JTextField inputPrice = new JTextField(7);
    
    private String currentCard = "1";
    CardLayout layout;
    JPanel dispPnl = new JPanel();
    
    /**
     * Checks button length 
     * Sets it higher then what is required if need be
     */
    public MiscButtons(salesWindow sw) {
        mbe = sw;
        setButtonCounts();
    }
    
    /**
     * We want a place to bring to gather all the buttons
     * Here we will have a top with buttons and bottom with 
     * the numpad
     */
    public JPanel getMiscPanel() {
        
        JPanel returnPanel = new JPanel();
        
        dispPnl.add(renderTaxButtons(), "1");
        dispPnl.add(renderNonTaxButtons(), "2");
        dispPnl.add(renderPayOutButtons(), "3");
        dispPnl.setPreferredSize(new Dimension(300, 300));
        returnPanel.add(dispPnl);
        returnPanel.add(numberPad());
        returnPanel.setLayout(new GridLayout(2, 1));
        
        return returnPanel;
    }
    
    /**
     * Typically we want to check that we have enough buttons to render
     * what is in the database
     */
    private void setButtonCounts() {
        try {
            // For the tax first
            String sql = "select count(*) from tax";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                taxBtnCount = rs.getInt(1) + 1;
            }
            rs.close();
            // Now for the non Tax
            sql = "select count(*) from nontax";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                nTaxBtnCount = rs.getInt(1) + 1;
            }
            rs.close();
            // Count the payout
            sql = "select count(*) from payout";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                poBtnCount = rs.getInt(1) + 1;
            }
            
            // Now we need to adjust the count if it has changed
            
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    public JPanel getCallButtons() {
        JPanel callButtonsPnl = new JPanel();
        layout = new CardLayout();
        dispPnl.setLayout(layout);
        JButton vat = new JButton("VAT");
        vat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentCard = "1";
                layout.show(dispPnl, currentCard);
                mbe.miscButtonFocus("VAT");
            }
        });
        vat.setBackground(new Color(68, 108, 179));
        vat.setForeground(Color.WHITE);
        
        callButtonsPnl.add(vat);
        JButton nonVat = new JButton("Non VAT");
        nonVat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentCard = "2";
                layout.show(dispPnl, currentCard);
                mbe.miscButtonFocus("NON_VAT");
            }
        });
        nonVat.setBackground(new Color(68, 108, 179));
        nonVat.setForeground(Color.WHITE);
        
        callButtonsPnl.add(nonVat);
        JButton payOut = new JButton("Pay Out");
        payOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentCard = "3";
                layout.show(dispPnl, currentCard);
                mbe.miscButtonFocus("PAY_OUT");
            }
        });
        payOut.setBackground(new Color(68, 108, 179));
        payOut.setForeground(Color.WHITE);
        callButtonsPnl.add(payOut);
        callButtonsPnl.setLayout(new GridLayout(1, 3));
        
        return callButtonsPnl;
    }
    
    /**
     * Render the Tax Buttons
     */
    public JScrollPane renderTaxButtons() {
        JPanel taxPnl = new JPanel();
        JButton[] taxBtn = new JButton[taxBtnCount];
        Vector btnInfo = new Vector();
        try {
            String sql = "select * from tax";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                // Name and the product ID
                Vector info = new Vector();
                info.add(rs.getString("name"));
                info.add(rs.getString("prid"));
                btnInfo.add(info);
            }
            // Lets build the buttons
            for(int i = 0; i < btnInfo.size(); i++) {
                final Vector info = (Vector) btnInfo.get(i);
                taxBtn[i] = new JButton("" + info.get(0));
                taxBtn[i].setBackground(Color.WHITE);
                taxBtn[i].addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent ae) {
                       Float amount = Float.parseFloat(inputPrice.getText()) / 100;
                       Object[] row = {info.get(1), info.get(0), 1, 0.00f, amount, amount};
                       mbe.miscButtonEvent(row);
                       inputPrice.setText("");
                   }
                });
                taxPnl.add(taxBtn[i]);
                taxPnl.setLayout(new GridLayout(4, 4));
            }
        } catch(Exception a ) {
            a.printStackTrace();
        }
        // For each category the buttons are renderd
        // the layout is set correctly and a panel with the buttons is returned
            // When a button is initialiased we need to add an actionlistener that
            // this listener will trigger a miscButtonEvent with the prepared row
            // This will call sales window and attempt to add that row to the table
        JScrollPane taxSP = new JScrollPane(taxPnl);
        return taxSP;
    }
    
    /**
     * Render Non Tax Buttons
     */
    public JScrollPane renderNonTaxButtons() {
        JPanel nonTaxPnl = new JPanel();
        JButton[] nonTaxBtn = new JButton[nTaxBtnCount];
        Vector btnInfo = new Vector();
        try {
            String sql = "select * from nontax";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                // Name and the product ID
                Vector info = new Vector();
                info.add(rs.getString("name"));
                info.add(rs.getString("prid"));
                btnInfo.add(info);
            }
            // Lets build the buttons
            for(int i = 0; i < btnInfo.size(); i++) {
                final Vector info = (Vector) btnInfo.get(i);
                nonTaxBtn[i] = new JButton("" + info.get(0));
                nonTaxBtn[i].setBackground(Color.WHITE);
                nonTaxBtn[i].addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent ae) {
                       Float amount = Float.parseFloat(inputPrice.getText()) / 100;
                       Object[] row = {info.get(1), info.get(0), 1, 0.00f, amount, amount};
                       mbe.miscButtonEvent(row);
                       inputPrice.setText("");
                   }
                });
                nonTaxPnl.add(nonTaxBtn[i]);
            }
            nonTaxPnl.setLayout(new GridLayout(4, 4));
        } catch(Exception a ) {
            a.printStackTrace();
        }
        JScrollPane ntSP = new JScrollPane(nonTaxPnl);
        return ntSP;
    }
    
    /**
     * Render Payout Buttons
     */
    public JScrollPane renderPayOutButtons() {
        JPanel poPnl = new JPanel();
        JButton[] poBtn = new JButton[poBtnCount];
        Vector btnInfo = new Vector();
        try {
            String sql = "select * from payout";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                // Name and the product ID
                Vector info = new Vector();
                info.add(rs.getString("name"));
                info.add(rs.getString("prid"));
                btnInfo.add(info);
            }
            // Lets build the buttons
            for(int i = 0; i < btnInfo.size(); i++) {
                final Vector info = (Vector) btnInfo.get(i);
                poBtn[i] = new JButton("" + info.get(0));
                poBtn[i].setBackground(Color.WHITE);
                poBtn[i].addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent ae) {
                       Float amount = Float.parseFloat(inputPrice.getText()) / 100;
                       amount = amount * -1;
                       Object[] row = {info.get(1), info.get(0), 1, 0.00f, amount, amount};
                       mbe.miscButtonEvent(row);
                       inputPrice.setText("");
                   }
                });
                poPnl.add(poBtn[i]);
            }
            
            poPnl.setLayout(new GridLayout(3, 3));
        } catch(Exception a ) {
            a.printStackTrace();
        }
        JScrollPane poSP = new JScrollPane(poPnl);
        return poSP;
    }
    
    public JPanel numberPad() {
        JPanel returnPnl = new JPanel();
        JPanel padPnl = new JPanel();
        JPanel inputPnl = new JPanel();
        final String[] numberbtns = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "00", "CLEAR"};
        JButton[] numBtn = new JButton[numberbtns.length];
        inputPrice.setFont(h1);
        inputPnl.add(inputPrice);
        inputPnl.setLayout(new GridLayout(2, 1));
        
        for(int i = 0; i < numberbtns.length; i++ ) {
            final String buttonText = numberbtns[i];
            numBtn[i] = new JButton(numberbtns[i]);
            numBtn[i].setBackground(Color.WHITE);
            numBtn[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    String curVal = inputPrice.getText();
                    String retval = numberPad(buttonText, curVal);
                    inputPrice.setText(retval);
                }
            });
            numBtn[i].setFont(h1);
            padPnl.add(numBtn[i]);
        }
        
        padPnl.setLayout(new GridLayout(4, 3));
        returnPnl.add(inputPnl);
        returnPnl.add(padPnl);
        returnPnl.setLayout(new FlowLayout());
        
        padPnl.setPreferredSize(new Dimension(300, 300));
        return returnPnl;
    }
    
    /**
     * For applying the Numbers to the text field form the misc numpad
     */
    private String numberPad(String input, String arg2) {
        String ret = arg2; /*The current value is any*/
        if ( input.equals("CLEAR") ) {
            // Must be Clear
            ret = "";
        } else {
            // Must be numbers
            ret = new StringBuffer().append(arg2).append(input).toString();
        }
        
        return ret;
    }

    @Override
    public void miscButtonEvent(Object[] rowArg) {
    }

    @Override
    public void miscButtonFocus(String buttonName) {
    }
    
    /**
     * Render the payout buttons
     */
    
}
