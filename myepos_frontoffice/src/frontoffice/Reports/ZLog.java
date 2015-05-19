/*
 * Copyright (C) 2015 Sunny Patel
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
package frontoffice.Reports;

import frontoffice.Report;
import javax.swing.JOptionPane;

/**
 *
 * @author Sunny Patel
 */

/**
 * This class can run zread and an xread.
 * an XRead is a read only option that allows us to see the totals so far.
 * the ZRead is a hard option it allows us to
 */
public class ZLog {
    
    public int X_READ = 0;
    public int Z_READ = 1;
    
    /**
     * In this class we want to use the `zlog` table.
     * Everytime this class is called it will create 
     * an entry in the zlog table
     */
    public ZLog(int readType) {
        if(readType == Z_READ) {
            String zreadMessage = "Are you sure you want to do a Z Read?";
            int decision = JOptionPane.showConfirmDialog(null, zreadMessage,
                    "Z Read", JOptionPane.YES_NO_OPTION);
            // Yes Do one please
            if (decision == 0 ) {
                try {
                    // do a z read
                } catch(Exception a){
                    System.err.println("I don't understand the decision you made on the zread");
                }
            }
        } else if (readType == X_READ) {
            generateReportInViewer();
        }
    }
    
    private void doAZREAD() {
        // generateReportInViewer
        generateReportInViewer();
        clearTheFloat();
        clearEndOfDay();
    }
    
    private void clearTheFloat() {
        
    }
    
    private void clearEndOfDay() {
        
    }
    
    private void generateReportInViewer() {
        Report rp = new Report("Reports/XZRead.jrxml");
        rp.viewReport();
    }
    
    private void markAsDone() {
        // Mark as done
    }
    
}
