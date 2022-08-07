package OpcUtils.ConnectionThread;

import android.os.Handler;
import android.os.Message;

import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.core.WriteValue;


public class ThreadWrite extends Thread {
    private Handler handler;
    private int namespace;
    private int nodeId;
    private String nodeId_String;
    private UnsignedInteger attribute;
    private SessionChannel session;
    private Variant valueToWrite;
    private Boolean valueToWrite2;

    public ThreadWrite(SessionChannel session, int namespace, int nodeId, UnsignedInteger attribute, Variant valueToWrite) {
        this.valueToWrite = valueToWrite;
        this.namespace = namespace;
        this.nodeId = nodeId;
        this.attribute = attribute;
        this.session = session;
        nodeId_String = null;
    }

    public ThreadWrite(SessionChannel session, int namespace, int nodeId, UnsignedInteger attribute, Boolean valueToWrite) {
        this.valueToWrite2 = valueToWrite;
        this.namespace = namespace;
        this.nodeId = nodeId;
        this.attribute = attribute;
        this.session = session;
    }

    public ThreadWrite(SessionChannel session, int namespace, String nodeId, UnsignedInteger attribute, Variant valueToWrite) {
        this.valueToWrite = valueToWrite;
        this.namespace = namespace;
        this.nodeId_String = nodeId;
        this.attribute = attribute;
        this.session = session;
    }

    private synchronized void send(Message msg) {
        msg.sendToTarget();
    }

    public void start(Handler handler) {
        start();
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            WriteResponse res;
            if (nodeId_String == null)
                res = session.Write(null, new WriteValue(new NodeId(namespace, nodeId), Attributes.Value, null, new DataValue(valueToWrite, StatusCode.GOOD)));
            else
                res = session.Write(null, new WriteValue(new NodeId(namespace, nodeId_String), Attributes.Value, null, new DataValue(valueToWrite, StatusCode.GOOD)));
            send(handler.obtainMessage(0, res));
        } catch (ServiceResultException e) {
            e.printStackTrace();
            // TODO: mostrare messaggio d'errore
        }
    }
}
