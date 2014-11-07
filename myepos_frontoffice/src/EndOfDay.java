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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * End Of Day is intended to allow a float balancing.
 * When end of day is posted it will balance the float as well.
 * End of day has it's own database table that will keep track 
 * of amounts posted this will be useful for audits
 * 
 * @author Sunny Patel
 */
public class EndOfDay extends MainMenu implements NumberPadEvent, MouseListener, ActionListener {
    
    JDialog frameeod = new JDialog(frame, "End Of Day", true);
    Object[] cneod = {"Type", "Count"};
    Object[][] dataeod = null;
    DefaultTableModel dtmeod = new DefaultTableModel(dataeod, cneod);
    JTable tableeod = new JTable(dtmeod);
    JTextField cashFld, cardFld, onlineFld, voucherFld,  accountFld;
    JButton saveeod, closeeod;
    JCheckBox cardok, onlineok, voucherok, accountok;
    int tablerowmapper = 0;
    
    public EndOfDay() {
        renderendofday();
    }
    
    private void renderendofday() {
        // Render End Of Day
        JPanel tblPnl = new JPanel();
        JPanel infoFld = new JPanel();
        JPanel btmPnl = new JPanel();
        tableeod.addMouseListener(this);
        
        saveeod = new JButton("Save");
        saveeod.addActionListener(this);
        closeeod = new JButton("Close");
        closeeod.addActionListener(this);
        
        btmPnl.add(saveeod);
        btmPnl.add(closeeod);
        
        // JTextField For All the Info
        cashFld = new JTextField("0.00", 7);
        cashFld.setFont(large);
        cardFld = new JTextField("0.00", 7);
        cardFld.setFont(large);
        onlineFld = new JTextField("0.00", 7);
        onlineFld.setFont(large);
        voucherFld = new JTextField("0.00", 7);
        voucherFld.setFont(large);
        accountFld = new JTextField("0.00", 7);
        accountFld.setFont(large);
        
        JLabel cashLbl = new JLabel("Cash");
        JLabel cardLbl = new JLabel("Card");
        JLabel onlineLbl = new JLabel("Online");
        JLabel voucherLbl = new JLabel("Voucher");
        JLabel accountLbl = new JLabel("Account");
        JLabel tickLbl = new JLabel("Amount OK");
        
        cardok = new JCheckBox();
        onlineok = new JCheckBox();
        voucherok = new JCheckBox();
        accountok = new JCheckBox();
        
        infoFld.add(cashLbl);
        infoFld.add(cashFld);
        infoFld.add(tickLbl);
        infoFld.add(cardLbl);
        infoFld.add(cardFld);
        infoFld.add(cardok);
        infoFld.add(onlineLbl);
        infoFld.add(onlineFld);
        infoFld.add(onlineok);
        infoFld.add(voucherLbl);
        infoFld.add(voucherFld);
        infoFld.add(voucherok);
        infoFld.add(accountLbl);
        infoFld.add(accountFld);
        infoFld.add(accountok);
        infoFld.setLayout(new GridLayout(5, 2));
        
        JScrollPane jspeod = new JScrollPane(tableeod);
        jspeod.setPreferredSize(new Dimension(300, 220));
        tblPnl.add(jspeod);
        
        frameeod.add(infoFld);
        frameeod.add(tblPnl);
        frameeod.add(btmPnl);
        
        addAmountsToTable();
        loadAmounts();
        
        frameeod.setLayout(new FlowLayout());
        frameeod.setLocation(350, 200);
        frameeod.setSize(400, 450);
        frameeod.setVisible(true);
    }
    
    /**
     * So that we can control the denominations
    */
    private void addAmountsToTable() {
        // Add Amounts to table
        String[] amounts = {"50", "20", "10", "5", "2", "1", 
            "0.50", "0.20", "0.10", "0.05", "0.02", "0.01"};
        for(int i = 0; i < amounts.length; i++) {
            Object[] row = {amounts[i], "0"};
            dtmeod.addRow(row);
        }
    }
    
    /**
     * We need to load amounts from the db all the things that happened in sale
     */
    private void loadAmounts() {
         // Load Amounts since last update or sicne epoch
        try {
            String sql = "select sum(cash), sum(card), sum(voucher), " +
                "sum(online), sum(account) from sale where `created` between " +
                "ifnull((select `created` from endofdaydetail order by `created` desc limit 1 ) " +
                ", '1970-01-01') and curDate();";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                // lets get all the things
                cashFld.setText(rs.getString(1));
                cardFld.setText(rs.getString(2));
                onlineFld.setText(rs.getString(3));
                voucherFld.setText(rs.getString(4));
                accountFld.setText(rs.getString(5));
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    /**
     * Update the end of day table
     */
    private void updateendofday() {
        try {
            String sql = "insert into endofday()";
            // Set the amounts in to endofday table
            sql = "insert into endofdaydetail (`type`, `amount`, `amountcount`" +
                    "`createdby`) values(?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            for(int i = 0; i < tableeod.getRowCount(); i++) {
                float amountFlt = Float.parseFloat(dtmeod.getValueAt(i, 0).toString());
                int amountcount = Integer.parseInt(dtmeod.getValueAt(i, 1).toString());
                float totalAmount = amountFlt * amountcount;
                pstmt.setFloat(1, amountFlt);
                pstmt.setFloat(2, totalAmount);
                pstmt.setInt(3, amountcount);
                pstmt.setString(4, Settings.get("userid").toString());
                pstmt.execute();
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    private void cleanupeod() {
        frameeod.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object trigger = ae.getSource();
        if(trigger == saveeod){
            // Save eod
            updateendofday();
            cleanupeod();
        } else if(trigger == closeeod) {
            // Close eod
            cleanupeod();
        }
    }
    
    @Override
    public void numberPadEvent(String returnArg) {
        // Lets set the item here
        dtmeod.setValueAt(returnArg, tablerowmapper, 1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        tablerowmapper = tableeod.getSelectedRow();
        NumberPad np = new NumberPad(this, frameeod);
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
