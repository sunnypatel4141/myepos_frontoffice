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
    
    // tables
    String tables[] = { "user",
                    "hold",
                    "product",
                    "productprice",
                    "productbarcode",
                    "sale",
                    "salesdata",
                    "maincategory",
                    "customer",
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
    
    // Create the column info objects
    String[][] userstruct = {{"id", "int"}, 
                    {"code", "varchar(10)"}, 
                    {"enabled", "tinyint(1)"},
                    {"firstname", "varchar(50)"},
                    {"lastname", "varchar(50)"},
                    {"address", "varchar(128)"},
                    {"phone", "bigint"}
    };

    structure user = new structure("user", userstruct);
    
}

class structure {
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
    
}