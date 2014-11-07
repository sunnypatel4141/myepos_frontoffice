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

/**
 *
 * @author Sunny Patel
 */
package frontoffice;
import frontoffice.base.DBConnection;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Sunny Patel
 */

public class salesWindow extends DBConnection implements ActionListener, FocusListener, TableModelListener, KeyListener {
    
    JFrame frame = new JFrame("Sales Window");
    JTextField searchFld;
    JButton searchProduct, hold, unhold, plusTool, minusTool, delete;
    JLabel safesale;
    JLabel infoLbl;
    JPanel searchPnl;
    // Payments
    JButton payment;
    JTextField total, subtotal, discount, qty;
    JPanel paymentSuperPanel, paymentAmountPnl;
    // Customer Info
    JLabel customerNameLbl = new JLabel("CUSTOMER NAME");
    // For the misc options layout
    CardLayout miscOptLayout;
    int currentcard = 1;
    JPanel miscOptDispPnl, miscOptCallPnl, numberPnlSuperParent;
    JPanel nonTaxBtnPnl, taxBtnPnl, payOutBtnPnl;
    ArrayList<JButton> nonTaxBtnsAL = new ArrayList<>();
    ArrayList<JButton> taxBtntnsAL = new ArrayList<>();
    ArrayList<JButton> payOutBtnsAL = new ArrayList<>();
    JButton miscTax, miscNonTax, payOut, menu, quickKeys;
    JButton[] nonTaxBtns;
    JButton[] taxBtns;
    JButton[] payOutBtns;
    String[] numberbtns = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "CLEAR", "OK"};
    JButton[] numbers = new JButton[12];
    JTextField inputPrice;
    /* Expremental START*/
    JButton addnew = new JButton("Add Row");
    /* Expremental END*/

    // Quick Key components
    static ArrayList<Integer> mainCatAL = new ArrayList<>();
    static ArrayList<String> mainCatNameAL = new ArrayList<>();
    static ArrayList<Integer> subCatAL = new ArrayList<>();
    static ArrayList<String> subCatNameAL = new ArrayList<>();
    static ArrayList<String> productAL = new ArrayList<>();
    static ArrayList<String> productNameAL = new ArrayList<>();
    static JPanel[] mainCatPnl = new JPanel[50];
    static JPanel[] subCatPnl = new JPanel[50];
    static JPanel[] productPnl = new JPanel[50];
    JPanel mainCategoryParent = new JPanel();
    JPanel subCategoryParent = new JPanel();
    JPanel productParent = new JPanel();
    JButton[] mainCatBtns = new JButton[50];
    JButton[] subCatBtns = new JButton[50];
    JButton[] quickProducts = new JButton[150];
    // for keeping track of buttons issued
    static int SC = 0;
    static int PR = 0;
    static int productPanel = 0;
    CardLayout quickKeysTab;
    CardLayout quickKeysTabSub;
    int qkCurrentCard = 1;
    int qkCurrentCardSub = 1;
    
    // Main GUI
    Font fontBold = new Font("Sans", Font.BOLD, 14);
    Font fontItalic = new Font("Sans", Font.ITALIC, 14);
    Font fontRegular = new Font("Sans", Font.PLAIN, 14);
    
    // Main Panels
    JPanel rightPnl, leftPnl;
    JPanel tablePnl, paymentPnl, optionsPnl, quickBtnsPnl,
    numPadPnl, quickFldPnl, toolsPnl, numberPnlParent;
    public JPanel menuOptsPnl = new JPanel();
    
    // Render Table
    public static Vector salesData = new Vector(14, 14);
    public static SalesTableModel model = new SalesTableModel(salesData);
    public JTable table = new JTable(model);
    
    void table() {
        tablePnl = new JPanel();
        // Set the table GUI
        table.setRowHeight(25);   
        // Add to scrollPane
        JScrollPane jsp = new JScrollPane(table);
        tablePnl.add(jsp);
        jsp.setPreferredSize(new Dimension(500, 400));
        model.addTableModelListener(this);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(1).setPreferredWidth(172);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        // Selection issues
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().setLeadSelectionIndex(table.getRowCount());
    }
    
    void payment() {
        paymentSuperPanel = new JPanel();
        paymentAmountPnl = new JPanel();
        payment = new JButton("Payment", new ImageIcon("Icons/pay.png"));
        payment.addActionListener(this);
        total = new JTextField(9);
        subtotal = new JTextField(9);
        discount = new JTextField(9);
        qty = new JTextField(9);
        JLabel totalLbl = new JLabel("Total");
        JLabel subtotalLbl = new JLabel("Sub-Total");
        JLabel discountLbl = new JLabel("Discount");
        JLabel qtyLbl = new JLabel("Quantity");
        paymentAmountPnl.add(totalLbl);
        paymentAmountPnl.add(total);
        paymentAmountPnl.add(subtotalLbl);
        paymentAmountPnl.add(subtotal);
        paymentAmountPnl.add(discountLbl);
        paymentAmountPnl.add(discount);
        paymentAmountPnl.add(qtyLbl);
        paymentAmountPnl.add(qty);
        paymentAmountPnl.setLayout(new GridLayout(4, 2));
        // Add to the superPanel
        paymentSuperPanel.add(paymentAmountPnl);
        paymentSuperPanel.add(payment);
        paymentSuperPanel.setLayout(new GridLayout(1, 2));
    }
    
    void searchProducts() {
        searchFld = new JTextField(11);
        searchFld.addKeyListener(this);
        searchFld.setFont(h1);
        searchProduct = new JButton("Search");
        searchProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SearchProduct sp = new SearchProduct();
                
            }
        });
        searchPnl = new JPanel();
        searchPnl.add(searchFld);
        //searchPnl.add(searchProduct);
        searchPnl.setPreferredSize(new Dimension(200, 10));
        searchPnl.setLayout(new GridLayout(1, 2));
    }
    
    void render() {
        // Panels
        rightPnl = new JPanel(); /* The right master panel*/
        leftPnl = new JPanel(); /* The Left Master Panel*/
        /* Compononents based */
        tablePnl = new JPanel(); /**/
        paymentPnl = new JPanel();
        optionsPnl = new JPanel();
        quickBtnsPnl = new JPanel();
        paymentSuperPanel = new JPanel();
        numPadPnl = new JPanel();
        numberPnlSuperParent = new JPanel(); /* The Super Number panel*/
        numberPnlParent = new JPanel(); /* The panel Containing imput box and pad */
        quickFldPnl = new JPanel();
        searchPnl = new JPanel();
        searchProducts();
        addnew.addActionListener(this);
        table();
        payment();
        toolsPanel();
        miscOptions();
        // If we don;t have any quick keys then there is no need
        if ( canLoadQuickKeys() ) {
            quickkeys();
        }
        // The Left Pnl
        leftPnl.add(searchPnl);
        leftPnl.add(tablePnl);
        leftPnl.add(toolsPnl);
        leftPnl.add(customerNameLbl);
        leftPnl.add(paymentSuperPanel);
        // The Right Panl
        rightPnl.add(miscOptCallPnl);
        rightPnl.add(miscOptDispPnl);
        rightPnl.add(paymentPnl);
        rightPnl.add(optionsPnl);
        rightPnl.add(quickBtnsPnl);
        rightPnl.add(numberPnlParent);
        rightPnl.add(quickFldPnl);
        rightPnl.add(menuOptsPnl);
        
        // Set the panels layout
        leftPnl.setLayout(new BoxLayout(leftPnl, BoxLayout.PAGE_AXIS));
        rightPnl.setLayout(new BoxLayout(rightPnl, BoxLayout.PAGE_AXIS));
        JPanel MasterPnl = new JPanel();
        // Set the Panels
        MasterPnl.add(leftPnl);
        MasterPnl.add(rightPnl);
        MasterPnl.setLayout(new BoxLayout(MasterPnl, BoxLayout.LINE_AXIS));
        frame.add(MasterPnl);
        frame.setSize(1024, 786);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    void toolsPanel() {
        /* Check ID, Hold, unhold, plus minus buttons*/
        hold = new JButton(new ImageIcon("Icons/out.png"));
        hold.addActionListener(this);
        unhold = new JButton(new ImageIcon("Icons/in.png"));
        unhold.addActionListener(this);
        plusTool = new JButton(new ImageIcon("Icons/list-add.png"));
        plusTool.addActionListener(this);
        minusTool = new JButton(new ImageIcon("Icons/list-remove.png"));
        minusTool.addActionListener(this);
        delete = new JButton(new ImageIcon("Icons/window-close.png"));
        delete.addActionListener(this);
        safesale = new JLabel("SAFE SALE");
        toolsPnl = new JPanel();
        toolsPnl.add(hold);
        toolsPnl.add(unhold);
        toolsPnl.add(plusTool);
        toolsPnl.add(minusTool);
        toolsPnl.add(delete);
        toolsPnl.add(safesale);
        toolsPnl.setPreferredSize(new Dimension(300, 40));
        toolsPnl.setLayout(new GridLayout(1, 6));
    }
    
    boolean canLoadQuickKeys() {
        boolean canload = false;
        try {
            String sql = "select count(*) from quickkeys";
            rs = stmt.executeQuery(sql);
            String count = "0";
            while(rs.next()) {
                count = rs.getString(1);
            }
            if ( count.equals("1") ) {
                // Ok we have products and can load
                canload = true;
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
        return canload;
    }
    
    void miscOptions() {
        miscOptDispPnl = new JPanel();
        miscOptCallPnl = new JPanel();
        numberPnlParent = new JPanel();
        nonTaxBtnPnl = new JPanel();
        taxBtnPnl = new JPanel();
        payOutBtnPnl = new JPanel();
        numPadPnl = new JPanel();
        
        // Set Layout for panel
        miscOptLayout = new CardLayout();
        miscOptDispPnl.setLayout(miscOptLayout);
        
        // Calling buttons
        miscTax = new JButton("VAT");
        miscTax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                miscOptLayout.first(miscOptDispPnl);
                currentcard = 1;
            }
        });
        miscOptCallPnl.add(miscTax);
        miscNonTax = new JButton("Non VAT");
        miscNonTax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentcard = 2;
                miscOptLayout.show(miscOptDispPnl, "" + (currentcard));
            }
        });
        miscOptCallPnl.add(miscNonTax);
        payOut = new JButton("Pay Out");
        payOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentcard = 3;
                miscOptLayout.show(miscOptDispPnl, "" + (currentcard));
            }
        });
        miscOptCallPnl.add(payOut);
        //payOut.addForeground(COlir)
        menu = new JButton("Menu");
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentcard = 4;
                miscOptLayout.show(miscOptDispPnl, "" +(currentcard));
            }
        });
        miscOptCallPnl.add(menu);
        //menu.setForeground(Color.BLUE);
        quickKeys = new JButton("Quick Keys");
        quickKeys.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                currentcard = 5;
                miscOptLayout.last(miscOptDispPnl);
            }
        });
        // Do we have any quick keys
        if ( canLoadQuickKeys() ) {
            miscOptCallPnl.add(quickKeys);
        }
        // Now add some layout and sizing
        miscOptCallPnl.setLayout(new GridLayout(1, 5));
        miscOptCallPnl.setPreferredSize(new Dimension(500, 70));
        
        try {
            stmt = conn.createStatement();
            // For non Tax Categories
            String sql = "select * from nontax";
            rs = stmt.executeQuery(sql);
            int count = 0;
            while ( rs.next() ) {
                final String btnName = rs.getString("name");
                final String miscProductCode = rs.getString("prid");
                nonTaxBtns = new JButton[count + 1];
                nonTaxBtns[count] = new JButton("" + rs.getString("name"));
                nonTaxBtns[count].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        String miscProductDesc = btnName;
                        int miscProductQty = 1;
                        float miscProductInputVal = Float.parseFloat(inputPrice.getText());
                        float miscProductUnitPrice = miscProductInputVal / 100;
                        float miscProductTotal = miscProductUnitPrice;
                        float miscProductDiscount = 0;
                        Object[] row = {miscProductCode, miscProductDesc, miscProductQty, 
                            miscProductDiscount, "" + miscProductUnitPrice, miscProductTotal};
                        // Add the product to table
                        insertRow(row);
                        inputPrice.setText("");
                    }
                });
                nonTaxBtnPnl.add(nonTaxBtns[count]);
                count++;
            }
            nonTaxBtnPnl.setLayout(new GridLayout(5, 3));
            // For Tax Categories
            sql = "select * from tax";
            rs = stmt.executeQuery(sql);
            count = 0;
            while ( rs.next() ) {
                final String btnName = rs.getString("name");
                final String miscProductCode = rs.getString("prid");
                taxBtns = new JButton[count + 1];
                taxBtns[count] = new JButton("" + rs.getString("name"));
                taxBtns[count].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        String miscProductDesc = btnName;
                        int miscProductQty = 1;
                        float miscProductInputVal = Float.parseFloat(inputPrice.getText());
                        float miscProductUnitPrice = miscProductInputVal / 100;
                        float miscProductDiscount = 0;
                        float miscProductTotal = miscProductUnitPrice;
                        Object[] row = {miscProductCode, miscProductDesc, miscProductQty, 
                            miscProductDiscount, "" + miscProductUnitPrice, miscProductTotal};
                        // Add the product to table
                        insertRow(row);
                        inputPrice.setText("");
                    }
                });
                taxBtnPnl.add(taxBtns[count]);
                count++;
            }
            taxBtnPnl.setLayout(new GridLayout(5, 3));
            // For pauout Categories
            sql = "select * from payout";
            rs = stmt.executeQuery(sql);
            count = 0;
            while ( rs.next() ) {
                final String btnName = rs.getString("name");
                final String miscProductCode = rs.getString("prid");
                payOutBtns = new JButton[count + 1];
                payOutBtns[count] = new JButton("" + rs.getString("name"));
                payOutBtns[count].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        String miscProductDesc = btnName;
                        int miscProductQty = 1;
                        float miscProductInputVal = Float.parseFloat(inputPrice.getText());
                        float miscProductUnitPrice = miscProductInputVal / -100;
                        float miscProductDiscount = 0;
                        float miscProductTotal = miscProductUnitPrice;
                        Object[] row = {miscProductCode, miscProductDesc, miscProductQty, 
                            miscProductDiscount, "" + miscProductUnitPrice, miscProductTotal};
                        // Add the product to table
                        insertRow(row);
                        inputPrice.setText("");
                    }
                });
                payOutBtnPnl.add(payOutBtns[count]);
                count++;
            }
            payOutBtnPnl.setLayout(new GridLayout(5, 3));
            rs.close();
            count = 0;
            // Now render the categories           
            //
            //
            // Number pad
            for(int i = 0; i < numberbtns.length; i++ ) {
                final int j = i;
                numbers[i] = new JButton(numberbtns[i]);
                numbers[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        String curVal = inputPrice.getText();
                        String retval = numberPad(numberbtns[j], curVal);
                        inputPrice.setText(retval);
                    }
                });
                numbers[i].setFont(h1);
                numPadPnl.add(numbers[i]);
                /**/
                menu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    currentcard = 4;
                    miscOptLayout.show(miscOptDispPnl, "" +(currentcard));
                }
            });                
                /**/
            }
            inputPrice = new JTextField(7);
            inputPrice.setFont(h1);
            numPadPnl.setLayout(new GridLayout(4, 3));
            numPadPnl.setPreferredSize(new Dimension(300, 300));
            numberPnlParent.setLayout(new BorderLayout());
            numberPnlParent.add(inputPrice, BorderLayout.PAGE_START);
            numberPnlParent.add(numPadPnl, BorderLayout.CENTER);
            // Now the build and output the menu
            MainMenu m = new MainMenu();
            
            // Add the panel to the super panel
            miscOptDispPnl.add(taxBtnPnl, "1");
            miscOptDispPnl.add(nonTaxBtnPnl, "2");
            miscOptDispPnl.add(payOutBtnPnl, "3");
            miscOptDispPnl.add(m.getMenu(), "4");
            miscOptDispPnl.setPreferredSize(new Dimension(300, 250));            
        } catch (SQLException a) {
            a.printStackTrace();
        }
    }
    
    void fetchProduct(String barcode) {
        try {
            String sql = "select * from searchproducts where barcode='" + 
                    barcode + "' ";
            boolean foundproduct = false;
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                foundproduct = true;
                Object[] row = {
                    rs.getString(1),
                    rs.getString(2),
                    "1",
                    "0.00",
                    rs.getString(4),
                    "0.00"};
                insertRow(row);
            }
            if (!foundproduct) {
                JOptionPane.showMessageDialog(frame, "Product not found!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
        
    String numberPad(String input, String arg2) {
        String ret = arg2; /*The current value is any*/
        if ( input.equals("CLEAR") ) {
            // Must be Clear
            ret = "";
        } else if ( input.equals("OK") ) {
            // Must be OK to Submit
        } else {
            // Must be numbers
            ret = new StringBuffer().append(arg2).append(input).toString();
        }
        
        return ret;
    }
    
    void quickkeys() {
        try {
            // How many Main Category(s) are there?
            String sql = "select distinct mcid, mcname from quickkeys";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while( rs.next() ) {
                mainCatAL.add(rs.getInt("mcid"));
                mainCatNameAL.add(rs.getString("mcname"));
            }
            //
            // TODO: If there are more than 50 categories then a mech is needed
            rs.close();
            int count = 0;
            rs = stmt.executeQuery("select distinct mcid, count(mcid) from quickkeys group by mcid");
            while(rs.next()) {
                count = rs.getInt("count");
            }
            if ( count > 149 ) {
                JButton[] quickProducts = new JButton[count++];
            }
            /* Leave the first and last panel alone 
             * as they will be handled by first | last for cardlayout(s)
             * Set the first Super Category card
            **/
            // Lets get second from last value so that it can be looped
            final int mainCatElem = mainCatAL.size() - 1;
            // Init the Super Cat Panel that will hold all the MAIN CAT buttons
            mainCategoryParent = new JPanel();
            // Initialise the first button and add listener and card layout
            mainCatBtns[0] = new JButton("" + mainCatNameAL.get(0));
            mainCatBtns[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    qkCurrentCard = 1;
                    quickKeysTab.first(subCategoryParent);
                }
            });
            mainCategoryParent.add(mainCatBtns[0]);
            // Which panel to add categories to and card no, first or last
            quickKeySubCat(mainCatAL.get(0), 0, true, false);
            // Now add all the buttons for the main cateory
            for ( int i = 1; i < mainCatElem; i++) {
                final int cc = i;
                mainCatBtns[i] = new JButton("" + mainCatNameAL.get(i));
                mainCatBtns[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        qkCurrentCard = cc;
                        quickKeysTab.show(subCategoryParent, "" + qkCurrentCard++);
                    }
                });
                mainCategoryParent.add(mainCatBtns[i]);
                subCatPnl[i] = new JPanel();
                subCategoryParent.add(mainCatPnl[i], qkCurrentCard);
                quickKeySubCat(mainCatAL.get(i), i, false, false);
            }
            final int lastCat = mainCatAL.size();
            mainCatBtns[lastCat] = new JButton("" + mainCatNameAL.get(lastCat));
            mainCatBtns[lastCat].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    qkCurrentCard = lastCat;
                    quickKeysTab.last(subCategoryParent);
                }
            });
            mainCategoryParent.add(mainCatBtns[lastCat]);
            subCatPnl[lastCat] = new JPanel();
            subCategoryParent.add(mainCatPnl[lastCat], qkCurrentCard++);
            quickKeySubCat(mainCatAL.get(lastCat), lastCat, false, true);
        } catch( Exception a ) {
            a.printStackTrace();
        }
        mainCatAL.clear();
        mainCatNameAL.clear();
    }
    
    void quickKeySubCat(int catArg, int panelArg, boolean firstArg, boolean lastArg) {
        /**
         Args: Category Arg,
         Panel: Panel Arg,
         [ First Pnl: first
         Last Pnl: last ] => when using a single card layout this ensures proper start and finish
         */
        final boolean first = firstArg;
        final boolean last = lastArg;
        try {
            // How many Sub Category(s) are there?
            String sql = "select distinct scid, scname from quickkeys where mcid='" +
                    catArg +"'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while( rs.next() ) {
                subCatAL.add(rs.getInt("scid"));
                subCatNameAL.add(rs.getString("scname"));
            }
            rs.close();
            //
            /* Now Commence with Sub Category */
            // Each button will need to be uniquely generated
            subCatBtns[SC] = new JButton("" + subCatNameAL.get(0));
            subCatBtns[SC].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(first) {
                        quickKeysTabSub.first(productParent);
                    } else {
                        quickKeysTabSub.show(productParent, "" + qkCurrentCardSub++);
                    }
                }
            });
            // Inatilase panel for subcat buttons
            subCatPnl[panelArg] = new JPanel();
            subCatPnl[panelArg].add(subCatBtns[SC]);
            // Now add the sub cat panel to parent
            subCategoryParent.add(subCatPnl[panelArg], panelArg);
            // Now add the products for this subcat
            quickKeysProduct(catArg, subCatAL.get(0), 1);
            SC++;
            // Workout the element before last
            int subCatElem = subCatAL.size() - 1;
            // Now add all the sub category buttons
            for ( int j = 1; j < subCatElem; j++ ) {
                final int cc = j;
                subCatBtns[SC] = new JButton("" + subCatNameAL.get(j));
                subCatBtns[SC].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        quickKeysTabSub.show(productParent, "" + qkCurrentCardSub++);
                    }
                });
                subCatPnl[panelArg].add(subCatBtns[SC]);
                quickKeysProduct(catArg, subCatAL.get(j), qkCurrentCardSub);
                SC++;
            }
            final int lastElem = subCatNameAL.size();
            subCatBtns[SC] = new JButton("" + subCatNameAL.get(lastElem));
            subCatBtns[SC].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(last) {
                        quickKeysTabSub.last(productParent);
                    } else {
                        quickKeysTabSub.show(productParent, "" + qkCurrentCardSub++);
                    }
                }
            });
            subCatPnl[panelArg].add(subCatBtns[SC]);
            quickKeysProduct(catArg, subCatAL.get(lastElem), qkCurrentCardSub);
            SC++;
            // Clear All the arrays down
            subCatAL.clear();
            subCatNameAL.clear();
        } catch(Exception a) {
            a.printStackTrace();
        }
        subCatAL.clear();
        subCatNameAL.clear();
    }
    
    // For adding the products to the individual panel
    public void quickKeysProduct(int cat1Arg, int cat2Arg, int panelArg) {
        try {
            String sql = "select id, prname from quickkeys where mcid='" + cat1Arg +
                    "' and scid='" + cat2Arg + "'";
            rs = stmt.executeQuery(sql);
            while( rs.next() ) {
                productAL.add(rs.getString("id"));
                productNameAL.add(rs.getString("prname"));
            }
            rs.close();
            // Initialise this products Panel
            productPnl[productPanel] = new JPanel();
            for (int i = 0; i < productAL.size(); i++) {
                final int cc = i;
                quickProducts[PR] = new JButton("" + productNameAL.get(i));
                quickProducts[PR].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        quickKeyClick(productAL.get(cc));
                    }
                });
                productPnl[productPanel].add(quickProducts[PR]);
                PR++;
            }
            // Add this to the child to the parent panel
            productParent.add(productPnl[productPanel], productPanel++);
            // Lets clear down everything
            productAL.clear();
            productNameAL.clear();
        } catch (Exception a) {
            a.printStackTrace();
        }        
    }
    
    // For adding quick key click to the table
    public void quickKeyClick( String productIDArg ) {
        System.out.println("Function Supressed for product barcode " + productIDArg);
        try {
            String storedProcedure = "{call getproductbyid(?)}";
            CallableStatement cs = conn.prepareCall(storedProcedure);
            cs.setString(1, productIDArg);
            cs.executeUpdate();
            
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    public boolean insertRow(Object[] args) {
        boolean returnval = false;
        if ( args.length <= 6 ) {
            // Format the values before entry
            String arg1 = args[0].toString();
            String arg2 = args[1].toString();
            Integer arg3 = Integer.parseInt(args[2].toString());
            Float arg4 = Float.parseFloat(args[3].toString());
            Float arg5 = Float.parseFloat(args[4].toString());
            Float arg6 = Float.parseFloat(args[5].toString());
            // Add to the table
            salesData.addElement(new SalesData(arg1, arg2, arg3, arg4, arg5, arg6));
            model.fireTableDataChanged();
        } else {
            Object[] msg = {"Cannot add values to row"};
            errorMessagePopup(msg);
        }        
        return returnval;
    }
 
    // For Product Infromation tags
    void setSafeSale(String[] args) {
        // sets the Icons 
        if( args[0].equals("1") ) {
            safesale.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
            safesale.setText(args[1]);
        } else if ( args[0].equals("2") ) {
            safesale.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            safesale.setText(args[1]);
        } else if( args[0].equals("3") ) {
            safesale.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
            safesale.setText(args[1]);
        }
    }
        
    // args are message, box_title, Message Icon
    void errorMessagePopup( Object[] args ) {
        String titleMessage = "Message Box";
        if ( !args[1].equals("") ) {
            titleMessage = args[1].toString();
        }
        if ( args[2] != null && args[2].equals("error") ) {
            JOptionPane.showMessageDialog(frame, args[0].toString(), titleMessage, 
                    JOptionPane.ERROR_MESSAGE);
        } else if ( args[2] != null && args[2].equals("warning") ) {
            JOptionPane.showMessageDialog(frame, args[0].toString(), titleMessage, 
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, args[0].toString(), titleMessage, 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    void holdsale() {
        int currentRowCount = table.getRowCount();
        if (currentRowCount > 0) {
            try {
                // Try and reuse a hold id
                Object holdidObj = Settings.get("holdid");
                Integer holdID = 0;
                HoldOrder h;
                if( holdidObj != null ) {
                    holdID = Integer.parseInt(holdidObj.toString());
                    h = new HoldOrder(holdID);
                } else {
                    h = new HoldOrder();
                }
                
                // Save The order
                h.save(getSaleTable());
                Object[] msg = {"Order on hold\n ID:" + h.getID(), "Order on Hold", ""};
                errorMessagePopup(msg);
                cleanup();
                Settings.remove("holdid");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Object[] msg = {"Cannot hold an empty order", "Empty Order Hold Error", "error"};
            errorMessagePopup(msg);
        }
    }
    
    void unholdWindow() {
        int currentRowCount = table.getRowCount();
        if ( currentRowCount < 1 ) {
            HoldOrder h = new HoldOrder("unhold");
            h.getHeldOrders();
        } else {
            Object[] msg = {"Cannot unhold on an existing order", "unhold Error", "error"};
            errorMessagePopup(msg);
        }
    }
    
    void unhold(HoldOrder h) {
        for(int i = 0; i < h.getRowCount(); i++) {
            insertRow(h.getRow(i));
        }
        Settings.put("holdid", h.getID());
    }
    
    void cleanup() {
        /*THIS IS FOR THE MAIN CLEAN UP ROUTINE SO WHEN A NEW SALE IS TO BE DONE*/
        try {
            int rowCount = model.getRowCount();
            for(int i = 0; i < rowCount; i++) {
                model.removeRow(0);
            }
        } catch(Exception a) {
            a.printStackTrace();
            Object[] error = {"Cannot clean up ", "Clean Up Error", "error"};
            errorMessagePopup(error);
        }
    }
    
    void updateTotals() {
        Total t = new Total();
        for(int i = 0; i < table.getRowCount(); i++) {
            // Set the Qty
            t.setQty(getQty(i));
            // Set the Total
            t.setTotal(model.getValueAt(i, 5));
            // Set the Discount
            t.setDiscount(model.getValueAt(i, 3));
        }
        // It's imposible to cast this to string so make it an object and pass it
        Object ttotal = t.getTotal();
        Object tsubtotal = t.getSubTotal();
        Object tqty = t.getQty();
        Object tdiscount = t.getDiscount();
        total.setText(ttotal.toString());
        subtotal.setText(tsubtotal.toString());
        qty.setText(tqty.toString());
        discount.setText(tdiscount.toString());
        // Set the focus to the barcode field
        searchFld.requestFocus();
    }
    
    int getQty(int rowArg) {
        int retval = 0;
        try {
            Object val = model.getValueAt(rowArg, 2);
            retval = Integer.parseInt(val.toString());
        } catch( Exception a ) {
            a.printStackTrace();
        }
        
        return retval;
    }
    
    void setQty(int rowArg, int qtyArg) {
        try {
            model.setValueAt(qtyArg, rowArg, 2);
            model.setValueAt(22.54f, rowArg, 5);
        } catch ( Exception a ) {
            a.printStackTrace();
        }
    }
    
    void updateTableCheck(String totals) {
        final int rowCount = table.getRowCount();
        HashMap products = new HashMap();
        // Outer loop
        for(int i = 0; i < rowCount; i++) {
            // Inner loop
            for(int j =0; j < rowCount; j++) {
                // Merge the rows
            }
        }
    }
    
    public Vector getSaleTable() {
        Vector LocalSaleData = new Vector();
        for(int i = 0; i < table.getRowCount(); i++) {
            Vector row = new Vector();
            row.addElement(model.getValueAt(i, 0).toString());
            row.addElement(model.getValueAt(i, 1).toString());
            row.addElement(model.getValueAt(i, 2).toString());
            row.addElement(model.getValueAt(i, 3).toString());
            row.addElement(model.getValueAt(i, 4).toString());
            row.addElement(model.getValueAt(i, 5).toString());
            LocalSaleData.addElement(row);
        }
        return LocalSaleData;
    }
   
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object caller = ae.getSource();
        if ( caller == hold ) {
            holdsale();
        } else if ( caller == unhold ) {
            unholdWindow();
        } else if ( caller == plusTool ) {
            // Increment quantity by 1
            int row = table.getSelectedRow();
            if ( row == -1 ) { // if not row is selected update the lastone
                int lastRow = table.getRowCount();
                lastRow--;
                int curQty = getQty(lastRow);
                curQty++;
                setQty(lastRow, curQty++);
            } else {
                int curQty = getQty(row);
                curQty++; 
                setQty(row, curQty++);
            }
        } else if ( caller == minusTool ) {
            // Decrement quantity by 1
            int row = table.getSelectedRow();
            if ( row == -1 ) { // if not row is selected update the lastone
                int lastRow = table.getRowCount();
                lastRow--;
                int curQty = getQty(lastRow);
                curQty--;
                setQty(lastRow, curQty--);
            } else {
                int curQty = getQty(row);
                curQty--;
                setQty(row, curQty);
            }
        } else if ( caller == payment ) {
            try {
                float totalArg = Float.parseFloat(total.getText());
                float subtotalArg = Float.parseFloat(subtotal.getText());
                float discountArg = Float.parseFloat(discount.getText());
                int qtyArg = Integer.parseInt(qty.getText());
                // If there is nothing there then there is absolutely no point
                if ( table.getRowCount() > 0 ) {
                    Payment p = new Payment(totalArg, subtotalArg, discountArg, qtyArg);
                    // Is a customer checking out
                    if ( Settings.containsKey("customerid") ) {
                        String custIDStr = Settings.get("customerid").toString();
                        int custid = Integer.parseInt(custIDStr);
                        p.setCustomer(custid);
                    }
                    // Now all the essentials of the payment
                    p.addSaleItem(getSaleTable());
                    p.render();
                }
            } catch (Exception a) {
                a.printStackTrace();
            }
        } else if ( caller == delete ) {
            try {
                int SelectedRow = table.getSelectedRow();
                int RowCount = table.getRowCount() - 1;
                if ( SelectedRow == -1 ) {
                    model.removeRow(RowCount);
                } else {
                    model.removeRow(SelectedRow);
                }
            } catch(Exception a) {
                warn(a.getMessage() + " @ Delete item");
            }
        }
        model.fireTableDataChanged();
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void focusGained(FocusEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void focusLost(FocusEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tableChanged(TableModelEvent tme) {
        updateTotals();
        // we want to calculate the values and totals
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    void warn(Object msg) {
        System.out.println(msg.toString());
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO: There is this massive issue with searchproducts thign the view needs to be rebuilt********
        if( e.getKeyCode() == 10 ) {
            // We have scanned it and saved it
            String barcode = searchFld.getText();
            // Clear it to stop further triggers
            searchFld.setText("");
            if(!barcode.equals("")) {
                fetchProduct(barcode);
            }
        }
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

/*
----------N O T E S----------
ActionEvent => Action Listeners can be Overridden on their own loops 
    i.e.
    for(int i = 0; i < buttons.length; i++) {
        final int j = i;
        buttons[i] = new Jbutton("Button " + i);
        buttons[i].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Buttons " + j);
            }
        });
    }

    // This is the preferred method as it is easy to follow

*/