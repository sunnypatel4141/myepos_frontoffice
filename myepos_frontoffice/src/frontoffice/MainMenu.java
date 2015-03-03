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

import frontoffice.recovery.Update;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Sunny Patel
 */
class MainMenu extends salesWindow implements ActionListener {
    private JPanel menuPnl = new JPanel();
    private JButton[] menuBtn = new JButton[11];
    private String[] menuTxt = {"Exit", "Update", "Z Read", "X Read", "Last Reciept", "History",
        "End Of Day", "Float", "Settings", "Hourly Report", "Customer"};
    
    MainMenu() {
        for(int i = 0; i < menuTxt.length; i++) {
            final int j = i;
            menuBtn[i] = new JButton(menuTxt[i]);
            if(menuTxt[i].equals("Exit") ) {
                menuBtn[i].setIcon(new ImageIcon("Icons/system-shutdown.png"));
            }
            menuBtn[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if ( menuTxt[j].equals("Exit")) {
                        close();
                    } else if ( menuTxt[j].equals("Update")) {
                        if ( !readRight("updatemenubtn") ) { 
                            menuBtn[j].setEnabled(false);
                        }
                        update();
                    } else if ( menuTxt[j].equals("Z Read")) {
                        if ( !readRight("zreadmenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        zread();
                    } else if ( menuTxt[j].equals("X Read")) {
                        if ( !readRight("xreadmenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        xread();
                    } else if ( menuTxt[j].equals("Last Reciept")) {
                        if ( !readRight("lastrecieptmenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        lastrec();
                    } else if ( menuTxt[j].equals("History")) {
                        if ( !readRight("historymenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        history();
                    } else if ( menuTxt[j].equals("End Of Day")) {
                        if ( !readRight("endofdaymenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        endofday();
                    } else if ( menuTxt[j].equals("Float")) {
                        if ( !readRight("floatmenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        floatcheck();
                    } else if ( menuTxt[j].equals("Settings")) {
                        if ( !readRight("settingsmenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        settings();
                    } else if ( menuTxt[j].equals("Hourly Report")) {
                        if ( !readRight("hourlyreportmenubtn") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        hourlyreport();
                    } else if (menuTxt[j].equals("Customer")) {
                        if ( !readRight("customer") ) {
                            menuBtn[j].setEnabled(false);
                        }
                        customer();
                    }                    
                }
            });
            menuPnl.setLayout(new GridLayout(5, 3));
            // Do some application rights checks
            if ( menuTxt[j].equals("Exit")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("Update") && readRight("updatemenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("Z Read") && readRight("zreadmenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("X Read") && readRight("xreadmenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("Last Reciept") && readRight("lastrecieptmenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("History") && readRight("historymenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("End Of Day") && readRight("endofdaymenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("Float") && readRight("floatmenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("Settings") && readRight("settingsmenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("Hourly Report") && readRight("hourlyreportmenubtn")) {
                menuPnl.add(menuBtn[i]);
            } else if ( menuTxt[j].equals("Customer") && readRight("customer")) {
                menuPnl.add(menuBtn[i]);
            }
        }
    }
    
    JPanel getMenu() {
        return menuPnl;
    }
    void close() {
        System.exit(0);
    }
    
    void update() {
        String cv = Settings.get("currentVersion").toString();
        String url = Settings.get("updateUrl").toString();
        boolean updateApproval = false; //flag for update approval and checking
        Update u = new Update(cv, url, updateApproval);
        u.proceedWithUpdate();
    }
    
    void zread() {
//        Report r = new Report();
    }
    
    void xread() {
        
    }
    
    void lastrec() {
        
    }
    
    void history() {
        SaleHistory sh = new SaleHistory();
    }
    
    void floatcheck() {
        String floatamount = Settings.get("floatamount").toString();
        String floatamountopening = Settings.get("floatamountopening").toString();
        FloatSettings fs = new FloatSettings(floatamount, floatamountopening);
    }
    
    void settings() {
        
    }
    
    void hourlyreport() {
        
    }
    
    void endofday() {
        EndOfDay eod = new EndOfDay();
    }
    
    void customer() {
        Customer c = new Customer();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
    }
}
