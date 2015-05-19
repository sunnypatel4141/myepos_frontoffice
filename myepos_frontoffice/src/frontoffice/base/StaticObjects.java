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

package frontoffice.base;

import frontoffice.LineDisplayGeneric;
import static frontoffice.base.XMLSettings.Settings;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sunny Patel
 */
public class StaticObjects {
    //
    // here static objets are declard to be used by the entire system
    public Font h1 = new Font("Verdana", Font.BOLD, 18);
    public Font h2 = new Font("Verdana", Font.PLAIN, 12);
    public Font h3 = new Font("Verdana", Font.PLAIN, 16);
    public Font large = new Font("Verdana", Font.BOLD, 26);
    public Locale cLocale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
    public NumberFormat currencyFormatter = NumberFormat.getInstance(cLocale);
    DecimalFormat formatterDec = (DecimalFormat)
        NumberFormat.getNumberInstance(cLocale);
    
    public static LineDisplayGeneric ldg;
    Socket client;
    ObjectOutputStream out;
    
    public String getCurrency(String amountArg) {
        String returnval = "";
        try {
            Double doubleVal = Double.parseDouble(amountArg);
            returnval = currencyFormatter.format(doubleVal);
            
        } catch(Exception a) {
            a.printStackTrace();
        }
        
        return returnval;
    }
    
    public float getCurrencyInFloat(String amountArg) {
        return Float.parseFloat(getCurrency(amountArg));
    }
    
    public float getCurrencyInFloat(float amountArg) {
        
        /*Formatter fb = new Formatter(Locale.UK);
        Float floatVal = Float.parseFloat("" + amountArg);
        String formattedCur = fb.format("%,.2f", floatVal).toString();*/
        float returnval = Float.parseFloat(getCurrency("" + amountArg));
        
        return returnval;
    }
    
    public double getCurrencyInDouble(String amountArg) {
        return Double.parseDouble("" + getCurrency(amountArg));
    }
    
    public double getCurrencyInDouble(double amountArg) {
        return Double.parseDouble("" + getCurrency("" + amountArg));
    }
    
    /**
     * For getting a valid suffix to currency
     */
    public String getLikeCurrency(double amountArg) {
        formatterDec.applyPattern("###,##0.00");
        
        return formatterDec.format(amountArg);
    }
    
    /**
     * For getting a valid suffix to currency
     */
    public String getLikeCurrency(float amountArg) {
        formatterDec.applyPattern("###,##0.00");
        
        return formatterDec.format(amountArg);
    }
    
    public String getLikeCurrencyWhole(float amountArg) {
        formatterDec.applyPattern("###,###");
        
        String amount = formatterDec.format(amountArg);
        if (amount.equals("0")) {
            return "1";
        }
        return amount;
    }
    
    HashMap apprights = new HashMap();
    
    public boolean readRight(String app) {
        boolean returnVal = false;
        Object ar = apprights.get("" + app);
        if ( ar == null ) {
            returnVal = false;
        } else {
            String right = ar.toString();
            if ( right.equals("1")) {
                returnVal = true;
            } else {
                returnVal = false;
            }
        }
        
        //FIXME: need to revert this to true
        returnVal = true;
        return returnVal;
    }
    
    public void loadRights(String userid) {
        if( userid.equals("")) {
            // Ok lets not do anything and exit
        } else {
            try {
                String sql = "select * from rights where user='" + userid +"'";
                DBConnection dbc = new DBConnection();
                dbc.conn = dbc.getConnection();
                dbc.stmt = dbc.conn.createStatement();
                dbc.rs = dbc.stmtext.executeQuery(sql);
                while(dbc.rs.next()) {
                    String key = dbc.rs.getString("apcode");
                    String val = dbc.rs.getString("r");
                    apprights.put(key, val);
                }
                // Loop and set the rights
            } catch (Exception a) {
                a.printStackTrace();
            }
        }
    }
    
    /**
     * Idea here is to have a load of peripherals that are initialiased
     * on login hence reducing the time to init it every time
     */
    public void loadPeripherals() {
        try {
            ldg = new LineDisplayGeneric(Settings.get("COMPort").toString(), Settings.get("TestMode").toString());
            openSocketServer();
        } catch(Exception ex) {
            System.err.println(ex.getCause());
        }
    }
    
    /**
     * Gracefully close opened devices to prevent it being locked
     */
    public void closePeripherals() {
        try {
            ldg.closePort();
            closeSocketServer();
        } catch(Exception ex) {
            System.err.println(ex.getCause());
        }
    }
    
    /**
     * We need sockets to be fully working order
     */
    private void openSocketServer() {
        try {
            String socketServerName = Settings.get("socketserver").toString();
            int socketServerPort = Integer.parseInt(Settings.get("socketport").toString());
            /*
            client = new Socket(socketServerName, socketServerPort);
            OutputStream outToServer = client.getOutputStream();
            out = new ObjectOutputStream(outToServer);
            */
        } catch (Exception ex) {
            Settings.put("socketserverWorking", false);
            Logger.getLogger(StaticObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * We need to close the socket server
     */
    private void closeSocketServer() {
        try {
            client.close();
        } catch (IOException ex) {
            Settings.put("socketserverWorking", false);
            Logger.getLogger(StaticObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendToSocketServer(Object data) {
        try {
            Object headerRow = Settings.get("tillNumber");
            out.writeObject(headerRow);
            out.writeObject(data);
        } catch (IOException ex) {
            Logger.getLogger(StaticObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

