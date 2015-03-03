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

import frontoffice.base.DBConnection;

/**
 *
 * @author Sunny Patel
 */
class Total extends DBConnection {
    private float total = 0.0f;
    private float discount = 0.0f;
    private int qty = 0;  
    
    public Total() {
        
    }
    
    /**
     * If we pass in Unit Price and Qty 
     * this class will calculate this automatically
     */
    public void setTotal(Object unitPriceArg, Object qtyArg) {
        float unitAmt = Float.parseFloat(unitPriceArg.toString()) + total;
        int unitQtyInt = Integer.parseInt(qtyArg.toString());
        float totalAmt = unitQtyInt * unitAmt;
        total = totalAmt;
    }
    
    public float getTotal() {
        return getCurrencyInFloat(total - discount);
    }
    
    public float getSubTotal() {
        return getCurrencyInFloat(total);
    }
    
    public void setDiscount(Object arg) {
        float discountAmt = Float.parseFloat(arg.toString()) + discount;
        discount = discountAmt;
    }
    
    public float getDiscount() {
        // TODO:: Implement Formatter here
        return getCurrencyInFloat(discount);
    }
    
    public void setQty(Object arg) {
        int qtyAmt = Integer.parseInt(arg.toString()) + qty;
        qty = qtyAmt;
    }
    
    public int getQty() {
        return qty;
    }
    
}
