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
import frontoffice.event.QuickKeyEvent;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class holds the quick keys.
 * When a main category is clicked it loads the sub categories for that main cat
 * Then it loads all the products associated with the main category and first 
 * sub category.
 * When a product is clicked it is relayed back to the sales window via the 
 * QuickKeyEvent Object that is a pass in when this class is first initialised
 * 
 * @author Sunny Patel
 */

/* BEST TO GET THE CARD LAYOUT WORKING ON THIS -- SO THAT ATLEAST WE CAN THEN hAvE IT ALL LOAD CORRECTLY*/
//# this might as well be multi threaded as it will just stop the normal process.. if it gets too big
public class QuickKeys extends DBConnection implements ActionListener {
    
    private QuickKeyEvent qke;
    
    CardLayout CLmc = new CardLayout();
    CardLayout CLsc = new CardLayout();
    CardLayout CLpr = new CardLayout();
    
    int CLmcInt = 1;
    int CLscInt = 1;
    int LCprInt = 1;
    
    ArrayList<String> MCName = new ArrayList<>();
    ArrayList<String> MCID = new ArrayList<>();
    
    ArrayList<String> SCName = new ArrayList<>();
    ArrayList<String> SCID = new ArrayList<>();
    
    ArrayList<String> PRName = new ArrayList<>();
    ArrayList<String> PRID = new ArrayList<>();
    
    /**
     * Main Category does not need an array of panels
     * 
     *            __________ // Product Panel loadProducts
     *           /
     *      ____/_________ // Sub Category panels loadSubCategories
     *     /
     * ___/_____________// Base Panel loadQuickKeys Return this panel
    */
    JPanel mcPnl = new JPanel();
    JPanel scPnl = new JPanel();
    JPanel prPnl = new JPanel();
    
    JPanel[] scIndPnl = new JPanel[49];
    JPanel[] prIndPnl = new JPanel[49];

    private int mainCategoryCount = 49;
    private int subCategoryCount = 49;
    private int productCount = 99;
    JButton[] MCBtn = new JButton[mainCategoryCount];
    JButton[] SCBtn = new JButton[subCategoryCount];
    JButton[] PRBtn = new JButton[productCount];
    
    /**
     * Pass in the sales Window Arg as a call back feature
     */
    QuickKeys(salesWindow salesWindowArg) {
        qke = salesWindowArg;
        updateButtonArray();
    }
    
    /**
     * This class updates the PRBtn array size
     * This is typically when we have more then 100 products
     * in the quick keys so to avoid it tripping up.
     */
    public void updateButtonArray() {
        try {
            String sql = "select count(*) from quickkeys";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                // are there more then 99 products?
                if(rs.getInt(1) > 99 ) {
                    PRBtn = new JButton[rs.getInt(1)];
                }
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    /**
     * Check if quick keys worth loading
     */
    public boolean canLoadQuickKeys() {
        boolean load = false;
        try {
            String sql = "select count(*) from quickkeys";
            rs = stmt.executeQuery(sql);
            String count = "0";
            while(rs.next()) {
                count = rs.getString(1);
                load = true;
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
        return load;
    }
    
    /**
     * Load Quick keys and return the JPanel with everything on it
     */
    public JPanel loadQuickKeys() {
        JPanel mainPanel = new JPanel();
        try {
            mainPanel.add(mcPnl);
            mainPanel.add(scPnl);
            mainPanel.add(prPnl);
            prPnl.setLayout(CLpr);
            scPnl.setLayout(CLsc);
            mainCategoryLoad(); //# this might need to be renamed so make it better
            mainPanel.setLayout(new GridLayout(3, 1));
            mainPanel.setVisible(true);
        } catch(Exception a) {
            a.printStackTrace();
        }
        return mainPanel;
    }
    
    /**
     * Load the main categories
     */
    private void mainCategoryLoad() {
        try {
            // How many Main Category(s) are there?
            String sql = "select distinct mcid, mcname from quickkeys order by mcname";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while( rs.next() ) {
                // ResultSet
                MCName.add(rs.getString("mcname"));
                MCID.add(rs.getString("mcid"));
            }
            for(int i = 0; i < MCName.size(); i++) {
                final String MCCountStr = "" + i;
                MCBtn[i] = new JButton(MCName.get(i));
                MCBtn[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        showSubCategoryProducts(MCCountStr);
                    }
                });
                mcPnl.add(MCBtn[i]);
                mcPnl.setLayout(new GridLayout(3, 3));
            }
            //# We need to tell this to assin a subcat panel for each main cat
            loadSubCategories();
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    /**
     * Tell me how many distinct main categories there are
     */
    private int mainCategoryCount() {
        int count = 0;
        try {
            String sql = "select distinct mcid, count(mcid) as `count` " +
                    "from quickkeys group by mcid";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                count = rs.getInt("count");
            }
            // This should NEVER happen but in case it does
            if(count > 49) {
                mainCategoryCount = count;
                MCBtn = new JButton[count++];
            }
            
        }catch(Exception a) {
            a.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * Load the sub Categories
     * @param MCIDArg
     */
    //# really need to remove the maincat id
    private void loadSubCategories() {
        try {
            //# loop thorugh the main cat id then assign a cardlayout foreach mcid
            for(int i = 0; i < MCID.size(); i++) {
                // Create the static value for later reference
                final String count = "" + i;
                // Get all the button for this particular category
                scIndPnl[i] = new JPanel();
                String sql = "select distinct scid, scname from quickkeys " +
                        "where mcid = '" + MCID.get(i) + "' order by scid";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    SCName.add(rs.getString("scname"));
                    SCID.add(rs.getString("scid"));
                    SCBtn[i] = new JButton(rs.getString("scname"));
                    SCBtn[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            CLpr.show(prPnl, count);
                        }
                    });
                    scIndPnl[i].add(SCBtn[i]);
                    scIndPnl[i].setLayout(new GridLayout(3, 3));
                }
                // Add this with the card Layout so we know
                scPnl.add(scIndPnl[i], count);
            }
            scPnl.setLayout(CLsc);
            scPnl.revalidate();
            loadProducts();
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    /**
     * Tell me how many distinct main categories there are
     */
    private int subCategoryCount() {
        int count = 0;
        try {
            String sql = "select distinct scid, count(scid) as `count` " +
                    "from quickkeys group by scid";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                count = rs.getInt("count");
            }
            
        }catch(Exception a) {
            a.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * This method is so that when a main category is click 
     * it can load the first panel of the sub category and also load the 
     * first panel of the product. this way when moving across categories
     * the products from the previous category are not left in the panel
     */
    private void showSubCategoryProducts(String panelIDArg) {
        // Load the requested sub category panel
        CLsc.show(scPnl, panelIDArg);
        // Load the product panel of this subcategory
        CLpr.show(prPnl, panelIDArg);
    }
    
    /**
     * Load the products for the main and sub category
     * @param SCIDArg
     * 
     */
    //# do't need sub cat id here
    private void loadProducts() {
        //# need to loopthrough all the sub categories and assign the cardlayout so that it can be called
        try {
            for(int i = 0; i < SCID.size(); i ++) {
                final String count = "" + i;
                prIndPnl[i] = new JPanel();
                String sql = "select * from quickkeys where scid = '" + SCID.get(i) + "'";
                rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    PRName.add(rs.getString("prname"));
                    // For later use
                    final String prid = rs.getString("id");
                    PRID.add(prid);
                    PRBtn[i] = new JButton(rs.getString("prname"));
                    PRBtn[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            qke.quickKeyEvent(prid);
                        }
                    });
                    prIndPnl[i].add(PRBtn[i]);
                    prIndPnl[i].setLayout(new GridLayout(3, 3));
                }
                prPnl.add(prIndPnl[i], count);
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
