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

import frontoffice.event.HoldEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sunny Patel
 */
class HoldOrder extends salesWindow implements ActionListener, HoldEvent {
    
    private int holdid = 0;
    private Vector SaleData = new Vector();
    JDialog frameHold = new JDialog(frame, "Orders on Hold", true);
    private JButton selectOrder, deleteOrder, close;
    private String[] cn = {"ID", "Till No", "Timestamp"};
    private Object[][] data = null;
    private DefaultTableModel dtmh = new DefaultTableModel(data, cn);
    private JTable tableHold = new JTable(dtmh);
    
    /**
     * This is for viewing Orders
     */
    HoldOrder(String arg) {
        if (arg.equals("unhold")) {
            // get the orders on hold
            getHeldOrders();
            renderFrame();
        }
    }
    
    /**
     * Creating a new order
     */
    public HoldOrder() {
        try {
            String sql = "{call holdneworder(?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            Object tillObj = Settings.get("tillNumber");
            String tillString = "";
            if ( tillObj != null ) {
                tillString = tillObj.toString();
            }
            cstmt.setString(1, tillString);
            rs = cstmt.executeQuery();
            while( rs.next() ) {
                holdid = rs.getInt(1);
            }
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
    
    HoldOrder(int hid) {
        holdid = hid;
        setHoldOrder(); // Make the Order live again
        clearOrderData(); //Clear the Order data so that it can be rbuilt
    }
    
    private void renderFrame() {
        JPanel tableHoldPnl = new JPanel();
        JPanel btnsHoldPnl = new JPanel();

        JScrollPane jspHold = new JScrollPane(tableHold);
        jspHold.setPreferredSize(new Dimension(500, 400));
        tableHoldPnl.add(jspHold);

        selectOrder = new JButton("Select");
        selectOrder.addActionListener(this);
        deleteOrder = new JButton ("Delete");
        deleteOrder.addActionListener(this);
        close = new JButton("Close");
        close.addActionListener(this);
        btnsHoldPnl.add(selectOrder);
        //btnsPnl.add(deleteOrder);
        btnsHoldPnl.add(close);
        btnsHoldPnl.setLayout(new GridLayout(1, 3));
        frameHold.add(tableHoldPnl);
        frameHold.add(btnsHoldPnl);
        frameHold.setSize(500, 500);
        frameHold.setLocation(200, 100);
        frameHold.setLayout(new FlowLayout());
        frameHold.setVisible(true);
    }
    
    private void buildOrder() {
        try {
            // Now set the final order
            String sql = "select * from holdorder ho left join hold hl " +
                    "on hl.id=ho.holdid where hl.id=" + holdid + " and hl.status='1'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                Vector row = new Vector();
                row.add(rs.getString("productid"));   // ID
                row.add(rs.getString("productname"));   // Name
                row.add(rs.getString("qty"));   // Qty
                row.add(rs.getString("discount"));   // Discount
                row.add(rs.getString("unitprice"));   // Unit price
                row.add(rs.getString("total"));   // Total
                SaleData.add(row);
            }
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
    
    public void getHeldOrders() {
        try {
            String sql = "select * from hold where status='1'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Object[] row = {rs.getString(1), rs.getString(2), rs.getString(4)};
                dtmh.addRow(row);
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    public Object[] getOrderInfo() {
        Object[] row = {};
        try {
            String sql = "select * from hold where status='1' and id='" + holdid + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
        return row;
    }
    
    public int getRowCount() {
        
        return SaleData.size();
    }
    
    public Object[] getRow(int row) {
        
        Vector DataRow = (Vector) SaleData.get(row);
        
        return DataRow.toArray();
    }
    
    public Object getValueAt(int row, int col){
        
        Vector DataRow = (Vector) SaleData.get(row);
        
        return DataRow.get(col);
    }
    
    public int getID() {
        return holdid;
    }
    
    public void save(Vector data) {
        SaleData = data;
        try {
            for(int i = 0; i < SaleData.size(); i ++) {
                Vector row = (Vector) SaleData.get(i);
                String sql = "insert into holdorder " +
                    "(productid, productname, qty, discount, unitprice, total, holdid)" +
                    "values(?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, row.get(0).toString());
                pstmt.setString(2, row.get(1).toString());
                pstmt.setString(3, row.get(2).toString());
                pstmt.setString(4, row.get(3).toString());
                pstmt.setString(5, row.get(4).toString());
                pstmt.setString(6, row.get(5).toString());
                pstmt.setInt(7, holdid);
                pstmt.execute();
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    private void setUnholdOrder() {
        try {
            String sql = "update hold set status='0' where id='" + holdid + "'";
            int success = stmt.executeUpdate(sql);
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    private void setHoldOrder() {
        try {
            String sql = "update hold set status='1' where id='" + holdid + "'";
            int success = stmt.executeUpdate(sql);
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    private void clearOrderData() {
        try {
            // Delete the current order vals so that it can re replaced easily
            String sql = "delete from holdorder where holdid='" + holdid + "'";
            int statusSet = stmt.executeUpdate(sql);
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    @Override
    void cleanup() {
        Component[] comp = frameHold.getComponents();
        for(int i = 0; i < comp.length; i++) {
            frameHold.remove(comp[i]);
        }
        frameHold.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object trigger = ae.getSource();
        if ( trigger == close ) {
            cleanup();
        } else if ( trigger == selectOrder ) {
            try {
                int row = tableHold.getSelectedRow();
                String orderStr = dtmh.getValueAt(row, 0).toString();
                holdid = Integer.parseInt(orderStr);
                buildOrder(); // The order needs to be built 
                unhold(this); // Populate the main table
                setUnholdOrder();
                cleanup();
            } catch (Exception a) {
                a.printStackTrace();
            }
        }
    }

    @Override
    public void holdSaleOrder(Vector saleTable) {
    }
}
