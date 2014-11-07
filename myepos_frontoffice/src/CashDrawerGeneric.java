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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jpos.POSPrinter;
import jpos.CashDrawer;
/**
 *
 * @author Sunny Patel
 */
class CashDrawerGeneric {
    
    private String change = "";
    private int DRAWER_METHOD = 0; // This is the default menthod as controlled by the cashdrawer.bat file
    private String DeviceName = "Star TSP100 Cutter (TSP143)";
    //System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "jpos.xml");
    
    // Cash Drawer Objects
    CashDrawer drw;
    POSPrinter posPrinter;
    
    public CashDrawerGeneric(Object[] arg) {
        change = arg[0].toString();
        DRAWER_METHOD = Integer.parseInt(arg[1].toString());
        if ( arg.length >= 2 ) {
            DeviceName = arg[2].toString(); // the device name for the printer
        }
    }
    
    public void openDrawer() {
        if ( DRAWER_METHOD == 0 ) {
            try {
                // Just run the cashdrawer.bat
                Process process = Runtime.getRuntime().exec("cashdrawer.bat");
            } catch (IOException ex) {
                Logger.getLogger(CashDrawerGeneric.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Cannot Open Drawer " + ex.getMessage(),
                        "Cannot Open Drawer", JOptionPane.ERROR_MESSAGE);
            }
        } else if ( DRAWER_METHOD == 1 ) {
            openDrawerThroughStarTSP();
        }
    }
    
    private void openDrawerThroughStarTSP() {
        try {
            drw = new CashDrawer();
            drw.open(DeviceName);
            drw.claim(1);
            drw.setDeviceEnabled(true);
            drw.openDrawer();
        } catch( Exception a ) {
            JOptionPane.showMessageDialog(null, "Cannot Open Drawer " + a.getMessage(), 
                    "Drawer Error", JOptionPane.ERROR_MESSAGE );
            a.printStackTrace();
        }
    }
}