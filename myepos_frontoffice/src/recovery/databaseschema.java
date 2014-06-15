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

/**
 *
 * @author Sunny Patel
 */
public class databaseschema {
    
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
    
    // Create a 2D String object containing the table structure 
    // then create a strcuture object and add to the table
    String[][] userstruct = {{"id", "int"}, 
                    {"code", "varchar(10)"}, 
                    {"enabled", "tinyint(1)"},
                    {"firstname", "varchar(50)"},
                    {"lastname", "varchar(50)"},
                    {"address", "varchar(128)"},
                    {"phone", "bigint"}
    };

    structure user = new structure("user", userstruct);
    
    String[][] holdstruct = null;
    
    structure hold = new structure("hold", holdstruct);
    
    String[][] productstruct = null;
    
    structure product = new structure("product", productstruct);
    
    String[][] productpricestruct = null;
    
    structure productprice = new structure("productprice", productpricestruct);
    
    String[][] productbarcodestruct = null;
    
    structure productbarcode = new structure("productbarcode", productbarcodestruct);
    
    String[][] salestruct = null;
    
    structure sale = new structure("sales", salestruct);
    
    String[][] salesdatastruct = null;
    
    structure salesdata = new structure("salesdata", salesdatastruct);
    
    String[][] maincategorystruct = null;
    
    structure maincategory = new structure("maincategory", maincategorystruct);
    
    String[][] customerstruct = null;
    
    structure customer = new structure("customer", customerstruct);
    
    // tables
    Object tables[] = { "user", user,
                    "hold", hold,
                    "product", product,
                    "productprice", productprice,
                    "productbarcode", productbarcode,
                    "sale", sale,
                    "salesdata", salesdata,
                    "maincategory", maincategory,
                    "customer", customer
    };
    
}

class structure extends Object {
    private String TABLE_NAME;
    private String[] COL_NAME;
    private String[] COL_TYPE;
    
    public structure(String tablename, String[][] colinfo) {
        TABLE_NAME = tablename;
        for(int i = 0; i < colinfo.length; i++) {
            COL_NAME[i] = colinfo[i][0];
            COL_TYPE[i] = colinfo[i][1];
        }
    }
    
    public structure(Object struct) {
        
    }
    public int colCount() {
        
        return COL_NAME.length;
    }
    
    public String colType(String colName) {
        String retVal = "";
        for(int i = 0; i < COL_NAME.length; i++) {
            if(COL_NAME[i].equals(colName)) {
                retVal = COL_TYPE[i];
            }
        }
        
        return retVal;
    }
    
    public boolean colExists(String colname) {
        boolean found = false;
        for(int i = 0; i < COL_NAME.length; i++) {
            if(COL_NAME[i].equals(colname)) {
                found = true;
            }
        }
        
        return found;
    }
    
    public String tableName() {
        
        return TABLE_NAME;
    }
}