package initiator;

import java.util.ArrayList;
import java.util.Date;

import quickfix.ApplicationAdapter;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.ClOrdID;
import quickfix.field.ExecType;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.NewOrderSingle;

public class FixClient extends ApplicationAdapter {

    private SocketInitiator socketInitiator;

    public static void main(String[] args) throws ConfigError {

        FixClient fixIniator = new FixClient();
        SessionSettings sessionSettings = new SessionSettings("./config/initiator.cfg");
        ApplicationAdapter application = new FixClient();
        FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
        ScreenLogFactory screenLogFactory = new ScreenLogFactory(sessionSettings);
        DefaultMessageFactory defaultMessageFactory = new DefaultMessageFactory();
        fixIniator.socketInitiator = new SocketInitiator(application,fileStoreFactory, sessionSettings, screenLogFactory,defaultMessageFactory);
        fixIniator.socketInitiator.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        ArrayList<SessionID> sessions = fixIniator.socketInitiator.getSessions();

        NewOrderSingle order = new NewOrderSingle(new ClOrdID("APPL12456S"),
                new HandlInst(HandlInst.MANUAL_ORDER), new Symbol("APPL"),
                new Side(Side.BUY), new TransactTime(new Date()), new OrdType(OrdType.MARKET));

        order.set(new OrderQty(4500));
        order.set(new Price(200.9d));
        SessionID sessionID = sessions.get(0);
        System.out.println("Sending Order to Server");
        try {
            Session.sendToTarget(order, sessionID);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("Going to stop socketInitiator");
        fixIniator.socketInitiator.stop();
    }

    @Override
    public void onLogon(SessionID sessionId) {
        super.onLogon(sessionId);
        System.out.println("Logon requested by client");
    }

    @Override
    public void fromAdmin(quickfix.Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            RejectLogon {
        super.fromAdmin(message, sessionId);
        System.out.println("Inside fromAdmin");
    }

    @Override
    public void onCreate(SessionID sessionId) {
        super.onCreate(sessionId);
        System.out.println("Inside onCreate");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (null != this.socketInitiator) {
            this.socketInitiator.stop();
        }
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {

        if (message instanceof ExecutionReport) {
            ExecutionReport executionReport = (ExecutionReport) message;
            try {
                ExecType executionType = (ExecType) executionReport.getExecType();
                System.out.println(executionType);
                System.out.println("Received execution report for the requested order from Exchange \n");
            } catch (FieldNotFound e) {
                e.printStackTrace();
            }
        }

    }
}