package initiator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.NewOrderSingle;

import java.util.Date;

public class ClientApplicationAdapter implements Application {

    private static final Logger log = LoggerFactory.getLogger(ClientApplicationAdapter.class);
    private SessionID sessionID;
    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("fromApp: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("onCreate: SessionId={}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("onLogon: SessionId={}", sessionId);
        this.sessionID=sessionId;
        this.buyOrder();
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("onLogout: SessionId={}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("toApp: Message={}, SessionId={}", message, sessionId);
    }
    public void buyOrder() {
        NewOrderSingle order = new NewOrderSingle(new ClOrdID("ATB12456S"),
                new HandlInst(HandlInst.MANUAL_ORDER), new Symbol("ATB"),
                new Side(Side.BUY), new TransactTime(new Date()), new OrdType(OrdType.MARKET));

        order.set(new OrderQty(4500));
        order.set(new Price(200.9d));
        System.out.println("Sending Order to Server");
        try {
            Session.sendToTarget(order, this.sessionID);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }




    }
}