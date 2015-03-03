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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.CallableStatement;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sunny Patel
 */
class SearchProduct extends salesWindow implements ActionListener, TableModelListener, FocusListener {
    
    private String productCode = "";
    
    private JDialog framesp = new JDialog(frame, "Search Product", true);
    private JButton search, close;
    private JTextField barcode, productName, code;
    private int field = 0; /*This is for the keyboard*/
    String[] cn = {"Product Code", "Product Name", "Price", "Main Category", "Category" };
    Object[][] data = null;
    DefaultTableModel dtm = new DefaultTableModel(data, cn);
    JTable table = new JTable(dtm);
    String[] txt = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
    String[] txt2 = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
    String[] txt3 = {"Z", "X", "C", "V", "B", "N", "M"};
    String[] txt4 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "c"};
    JButton[] kb = new JButton[txt.length];
    JButton[] kb2 = new JButton[txt2.length];
    JButton[] kb3 = new JButton[txt3.length];
    JButton[] kb4 = new JButton[txt4.length];
    
    SearchProduct() {
        search = new JButton("Search");
        search.addActionListener(this);
        close = new JButton("Close");
        close.addActionListener(this);
        barcode = new JTextField(7);
        barcode.setFont(h3);
        barcode.addFocusListener(this);
        productName = new JTextField(7);
        productName.setFont(h3);
        productName.addFocusListener(this);
        code = new JTextField(7);
        code.setFont(h3);
        code.addFocusListener(this);
        JLabel barcodeLbl = new JLabel("Barcode");
        JLabel nameLbl = new JLabel("Product Name");
        JLabel codeLbl = new JLabel("Product Code");
        JScrollPane jsp = new JScrollPane(table);
        
        jsp.setPreferredSize(new Dimension(200, 200));
        dtm.addTableModelListener(this);
        
        JPanel kbPnl1 = new JPanel();
        JPanel kbPnl2 = new JPanel();
        JPanel kbPnl3 = new JPanel();
        JPanel kbPnl4 = new JPanel();
        JPanel kbMain = new JPanel();
        JPanel fieldPnl = new JPanel();
        for(int i = 0; i < txt.length; i ++) {
            kb[i] = new JButton(txt[i]);
            kb[i].addActionListener(this);
            kbPnl1.add(kb[i]);
        }
        kbPnl1.setLayout(new GridLayout(1, 10));
        for(int i = 0; i < txt2.length; i++) {
            kb2[i] = new JButton(txt2[i]);
            kb2[i].addActionListener(this);
            kbPnl2.add(kb2[i]);
        }
        kbPnl2.setLayout(new GridLayout(1, 10));
        for(int i = 0; i < txt3.length; i++) {
            kb3[i] = new JButton(txt3[i]);
            kb3[i].addActionListener(this);
            kbPnl3.add(kb3[i]);
        }
        kbPnl3.setLayout(new GridLayout(1, 10));
        for(int i = 0; i < txt4.length; i++) {
            kb4[i] = new JButton(txt4[i]);
            kb4[i].addActionListener(this);
            kbPnl4.add(kb4[i]);
        }
        kbPnl4.setLayout(new GridLayout(1, 10));
        // Build the keyboard
        kbMain.add(kbPnl4);
        kbMain.add(kbPnl1);
        kbMain.add(kbPnl2);
        kbMain.add(kbPnl3);
        
        /*Add to Fields Panel*/
        fieldPnl.add(codeLbl);
        fieldPnl.add(code);
        fieldPnl.add(nameLbl);
        fieldPnl.add(productName);
        fieldPnl.add(barcodeLbl);
        fieldPnl.add(barcode);
        fieldPnl.add(search);
        fieldPnl.add(close);
        fieldPnl.setLayout(new GridLayout(4, 2));
        
        kbMain.setLayout(new GridLayout(4, 1));
        
        JPanel mainPnl = new JPanel();
        JPanel top_two = new JPanel();
        top_two.add(fieldPnl);
        top_two.add(jsp);
        top_two.setLayout(new GridLayout(1, 2));
        mainPnl.add(top_two);
        mainPnl.add(kbMain);
        
        mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.PAGE_AXIS));
        framesp.add(mainPnl);
        framesp.setSize(1024, 730);
        framesp.setVisible(true);
    }
    
    public void listProducts(String barcodeArg, String productNameArg, String codeArg) {
        Integer rowcount = table.getRowCount();
        while(rowcount > 0) {
            dtm.removeRow(0);
            rowcount = table.getRowCount();
        }
        try {
            String sql = "{call listProducts(?, ?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setString(1, barcodeArg);
            cstmt.setString(2, productNameArg);
            cstmt.setString(3, codeArg);
            
            rs = cstmt.executeQuery();
            while(rs.next()) {
                Object[] row = {
                        rs.getString(1), 
                        rs.getString(2), 
                        rs.getString(3), 
                        "", 
                        ""
                };
                dtm.addRow(row);
            }
            
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
    
    @Override
    public void focusGained(FocusEvent fe) {
        Object trigger = fe.getSource();
        if (trigger == code) {
            field = 0;
        } else if (trigger == barcode) {
            field = 1;
        } else if (trigger == productName) {
            field = 2;
        }
    }
    
    @Override
    public void focusLost(FocusEvent fe) {
        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object trigger = ae.getSource();
        if ( trigger == search ) {
            String brc = barcode.getText() + "";
            String prName = productName.getText() + "";
            String cde = code.getText() + "";
            listProducts(brc, prName, cde);
        } else if (trigger == close) {
            cleanup();
        }
        for(int i = 0; i < txt.length; i++) {
            if (trigger == kb[i]) {
                keyboard(txt[i]);
                break;
            }
        }
        for(int i = 0 ; i < txt2.length; i++) {
            if (trigger == kb2[i]) {
                keyboard(txt2[i]);
                break;
            }
        }
        for(int i = 0; i < txt3.length; i++) {
            if (trigger == kb3[i]) {
                keyboard(txt3[i]);
                break;
            }
        }
        for(int i = 0; i < txt4.length; i++) {
            if (trigger == kb4[i]) {
                keyboard(txt4[i]);
                break;
            }
        }
    }
    
    @Override
    public void cleanup() {
        Component[] elems = framesp.getComponents();
        for(int i = 0; i< elems.length; i++ ) {
            framesp.remove(elems[i]);
        }
        framesp.dispose();
    }
    
    public void keyboard(String key) {
        if (field == 0) {
            if ( key.equals("c")) {
                code.setText("");
            } else {
                String curStr = code.getText();
                String newStr = new StringBuffer().append(curStr).append(key).toString();
                code.setText(newStr);
            }
        } else if (field == 1) {
            if ( key.equals("c")) {
                barcode.setText("");
            } else {
                String curStr = barcode.getText();
                String newStr = new StringBuffer().append(curStr).append(key).toString();
                barcode.setText(newStr);
            }
        } else if (field == 2) {
            if ( key.equals("c")) {
                productName.setText("");
            } else {
                String curStr = productName.getText();
                String newStr = new StringBuffer().append(curStr).append(key).toString();
                productName.setText(newStr);
            }
        }
    }
    
    @Override
    public void tableChanged(TableModelEvent tem) {
        
    }
    
    public String getSelectedProduct() {
        if ( productCode.equals("") ) {
            Object[] msg = {"Must select a product from the list", "error"};
            errorMessagePopup(msg);
        }
        
        return productCode;
    }
    
    
}
