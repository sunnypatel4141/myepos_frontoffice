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
    Object[][] tables[] = { {"user", user},
                    {"hold", hold[][]},
                    {"product", product[][],
                    {"productprice", productprice[][]},
                    {"productbarcode", productbarcode[][]},
                    {"sale", sale[][]},
                    {"salesdata", salesdata[][]},
                    {"maincategory", maincategory[][]},
                    {"customer", customer[][]}
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
    
    Mapsuer = {"id", "int",
        "code", "varchar",
        "enabled", "tinyint",
    };
    
    String hold[][] = {
        {}
    };
}

