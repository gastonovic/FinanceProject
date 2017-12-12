package initiator;

import quickfix.field.*;

public class OrderF {
     HandlInst handlInst; char side; char type;
    private static int nextID = 1;
    private double price;
    private int quantity;
    private String symbol;
    private String ID = null;





    public OrderF() {
        ID = generateID();
    }


    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public char getSide(){
       return this.side;

    }

    public void setSide(char side) {
        this.side = side;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getID() {
        return ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String generateID() {
        return Long.toString(System.currentTimeMillis() + (nextID++));
    }





}
