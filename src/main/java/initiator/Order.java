package initiator;

import quickfix.field.*;

public class Order {
    ClOrdID clOrdID; HandlInst handlInst; Side side; TransactTime transactTime; OrdType ordType;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }




}
