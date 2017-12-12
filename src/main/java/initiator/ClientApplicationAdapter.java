package initiator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.NewOrderSingle;
import user.User;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientApplicationAdapter implements Application {
    User user  = new User("Omar","Mahjoubi");
    private static final Logger log = LoggerFactory.getLogger(ClientApplicationAdapter.class);
    private SessionID sessionID;
    private AtomicInteger counter;
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

    public void send(OrderF orderF) {
        NewOrderSingle order = new NewOrderSingle(new ClOrdID(orderF.getID()),
                new HandlInst(HandlInst.MANUAL_ORDER), new Symbol(orderF.getSymbol()),
                new Side(orderF.getSide()), new TransactTime(new Date()), new OrdType(orderF.getType()));

        order.set(new OrderQty(orderF.getQuantity()));
        order.set(new Price(orderF.getPrice()));
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
    @Bean
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}