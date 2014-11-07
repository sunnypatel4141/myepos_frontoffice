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

/**
 *
 * @author Sunny Patel
 */

class SalesData {
    private String S_ProductID;
    private String S_Description;
    private Integer S_Qty;
    private float S_Discount;
    private float S_Unit_Price;
    private float S_Total;
    
    public SalesData(String ProductID, String Description, Integer Qty, float Discount, float Unit_Price, float Total) {
        Total = (Qty * Unit_Price) - Discount;
        S_Description = Description;
        S_Qty = Qty;
        S_Discount = Discount;
        S_Unit_Price = Unit_Price;
        S_Total = Total;
        S_ProductID = ProductID;
    }
    
    public String getProductID() {
            return S_ProductID;
    }
    public String getDescription() {
            return S_Description;
    }

    public Integer getQty() {
            return S_Qty;
    }

    public float getDiscount() {
            return S_Discount;
    }

    public float getUnitPrice() {
            return S_Unit_Price;
    }

    public float getTotal() {
            return S_Total;
    }

    public void setProductID(String ProductID) {
            S_ProductID = ProductID;
    }
    public void setDescription(String Description) {
            S_Description = Description;
    }

    public void setQty(Integer Qty) {
            S_Qty = Qty;
    }

    public void setDiscount(float Discount) {
            S_Discount = Discount;
    }

    public void setUnit_Price(float Unit_Price) {
            S_Unit_Price = Unit_Price;
    }

    public void setTotal(float Total) {
            S_Total = (S_Qty * S_Unit_Price) - S_Discount;
    }
}