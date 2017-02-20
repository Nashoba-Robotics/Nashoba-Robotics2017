package ArduinoComm;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

/**
 * @author Colin in 2016
 */
public class Network implements ITableListener { 
    private static Network singleton;
    public static Network getInstance()
    {
        if(singleton == null)
            singleton = new Network();
        return singleton;
    }

    private NetworkTable table;
    private final String DASHBOARD_NAME = "SmartDashboard";

    private OnMessageReceivedListener listener = null;

    private Network()
    {
        NetworkTable.setClientMode();
        NetworkTable.setIPAddress("roboRIO-1768-FRC.local");
    }

    public interface ConnectionListener
    {
        public void onConnectionStateChanged(boolean state);
    }

    private ConnectionListener connectionListener;
    public void setConnectionListener(ConnectionListener listener)
    {
        this.connectionListener = listener;
    }

    public void connect()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                table = (NetworkTable) NetworkTable.getTable(DASHBOARD_NAME).getSubTable("Robot Diagram");
                table.addTableListener(Network.this);
                table.addSubTableListener(Network.this);

                getTable().addConnectionListener(new IRemoteConnectionListener()
                {
                    @Override
                    public void connected(IRemote iRemote)
                    {
                        Arduino.display.setRobotStatus("Connected to Robot", true);
                        if(connectionListener != null)
                            connectionListener.onConnectionStateChanged(true);
                    }

                    @Override
                    public void disconnected(IRemote iRemote)
                    {
                        Arduino.display.setRobotStatus("Not Connected to Robot", false);
                        if(connectionListener != null)
                            connectionListener.onConnectionStateChanged(false);
                    }
                }, true);
            }
        }).start();
    }

    public void putString(String key, String value)
    {
        table.putString(key, value);
    }

    public void putNumber(String key, Double value)
    {
        table.putNumber(key, value);
    }

    public void putBoolean(String key, Boolean value)
    {
        table.putBoolean(key, value);
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void valueChanged(ITable iTable, String s, Object o, boolean b)
    {
        //Printer.println("MESSAGE: " + s + ": " + o);
        if(iTable == table)
        {
            if(listener != null)
            {
                listener.onMessageReceived(s, o);
            }
        }
    }

    public interface OnMessageReceivedListener
    {
        public void onMessageReceived(String key, Object value);
    }

    public NetworkTable getTable()
    {
        return table;
    }

    public String getString(String key)
    {
        return table.getString(key, "");
    }

    public Boolean getBoolean(String key)
    {
        try
        {
            return table.getBoolean(key);
        }
        catch (TableKeyNotDefinedException e)
        {
            return null;
        }
    }

    public Double getNumber(String key)
    {
        try
        {
            return table.getNumber(key);
        }
        catch (TableKeyNotDefinedException e)
        {
            System.err.println("Couldn't find table key " + key);
            return 0.0;

        }
    }

    public NetworkTable getNetworkSubTable(String name)
    {
        return (NetworkTable)table.getSubTable(name);
    }
}
