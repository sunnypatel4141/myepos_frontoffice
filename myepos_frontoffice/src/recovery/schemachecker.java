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

import recovery.structure;
/**
 *
 * @author Sunny Patel
 */
public class schemachecker extends databaseschema {
    String notfound = "";
    
    // Connection settings for the db
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String url;
    String user;
    String pass;
    
    void checkTables() {
        String tablesindb[][] = new String[tables.length][1];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("show tables");
            int count = 0;
            while(rs.next()) {
                tablesindb[count][0] = rs.getString("Field");
                tablesindb[count][1] = "1";
                count++;
            }
            for(int i = 0; i < tables.length; i++) {
                boolean found = false;
                int ref = 0;
                // Lets loop through and check if the tables in the database exist
                for(int j = 0; j < tablesindb.length; j++) {
                    if(tables[i].equals(tablesindb[j][0])) {
                        found = true;
                        ref = j;
                    }
                }
                if(found == false) {
                    tablesindb[ref][1] = "0";
                    notfound = notfound + " \n" + tablesindb[ref][0];
                } else {
                    structure tableData = tables[i++];
                    checkStructure(tableData);
                }
            }
            System.out.println(notfound);            
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    void checkStructure(structure table) {
        try {
            String tableName = table.tableName();
            // Do a describe of the table structure and see iof it works 
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("describe " + tableName);
            while( rs.next() ) {
                String field = rs.getString("Field");
                if (table.colExists(field) == false) {
                    System.out.println(tableName + ": " + field + " Does not exist in structure");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}