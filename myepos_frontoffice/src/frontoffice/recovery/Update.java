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

package frontoffice.recovery;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Sunny Patel
 */
public class Update {
    private String currentVersion = "";
    private String url = "";
    private boolean updateAvailable = false;
    private boolean approved = false; // Is the update allowed
    private JFrame checkFrame = new JFrame("Update");
    
    public Update(String curVer, String urlArg, boolean approvedArg) {
        currentVersion = curVer;
        url = urlArg;
        approved = approvedArg;
    }
    
    public boolean checkForUpdate() {
        
        return updateAvailable;
    }
    
    
    // When Instruction is given to go with the update
    public void proceedWithUpdate() {
        if ( checkForUpdate() && approved ) {
            // update is available we can proceed
            final JOptionPane exportConfirm = new JOptionPane("DANGER! \n " +
                    "This will OVERWRITE Current Software Version", 
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION);
            final JDialog dialog = new JDialog(checkFrame, "Export Schema", true);
            dialog.setContentPane(exportConfirm);
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            exportConfirm.addPropertyChangeListener(
                    new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent e) {
                            String prop = e.getPropertyName();
                            if ( dialog.isVisible()
                                && (e.getSource() == exportConfirm)
                                && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                                dialog.setVisible(false);
                            }
                        }
                    });
            dialog.pack();
            dialog.setVisible(true);
            int value = ((Integer)exportConfirm.getValue()).intValue();
            if ( value == JOptionPane.YES_OPTION ) {
                runTheUpdate();
            }
        } else {
            // No update Available 
        }
    }
    
    // When Everything is ok and update will take place
    void runTheUpdate() {
        
    }
}
