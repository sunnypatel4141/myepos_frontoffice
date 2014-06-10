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

package recovery;

import java.sql.*;
/**
 *
 * @author Sunny Patel
 */
public class schemachecker {
    // tables
    String tables[] = { "user",
                    "hold",
                    "product",
                    "productprice",
                    "productbarcode",
                    "sale",
                    "salesdata",
                    "maincategory",
                    "customer"
    };    

    // views
    String views[] = { "quickkeys",
                    "outstandingtransactions",
                    "salesbyhour"
    };
    
    // Routines
    String routines[] = { "holdsale",
                        "unholdsale",
                        "updateproduct",
                        "deleteproduct",
    };
    
    // Connection settings for the db
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String url;
    String user;
    String pass;
    
    String[] checkTables() {
        String tablesnotindb[] = new String[tables.length];
        // Lets loop
        for(int i = 0; i < tables.length; i++) {
            int tablecols = 0;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pass);
                stmt = conn.createStatement();
                rs = stmt.executeQuery("describe " + tables[i]);
                while(rs.next()) {
                    tablecols++;
                }
                if(tablecols > 0) {
                    checkStructure(tables[i]);
                } else {
                    int currentsize = tablesnotindb.length;
                    tablesnotindb[currentsize++] = tables[i];
                }
            } catch(Exception a) {
                a.printStackTrace();
                int currentsize = tablesnotindb.length;
                tablesnotindb[currentsize++] = tables[i];
            }
        }
        
        return tablesnotindb;
    }
    
    void checkStructure(String table) {
        try {
            // Do a describe of the table structure and see iof it works 
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
