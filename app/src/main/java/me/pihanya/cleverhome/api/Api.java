package me.pihanya.cleverhome.api;

import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pihanya on 11.08.16.
 */
public class Api {
    private static int RECONNECT_TIME = 1000;
    private static int RECONNECT_DEPTH = 5;

    private static String TAG = "HomeConnection";
    private static String HOME_IP = "192.168.1.48:8080";
    private static String DEFAULT_PASSWORD = "qwerty123";

    private static boolean isConnected = false;
    private static boolean isAuthificated = false;

    /**============================== [Arduino API] ==============================**/

    public static Map<String, Object> auth( String password ) {
        return auth( password, -1 );
    }

    public static Map<String, Object> auth( String password, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "password", password != null ? password : DEFAULT_PASSWORD );
        args.put( "token", token );

        return sendRequest( "auth", args );
    }

    public static Map<String, Object> isAuthorized() {
        return isAuthorized( -1 );
    }

    public static Map<String, Object> isAuthorized( Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "token", token );

        return sendRequest( "isAuthorized", args );
    }

    public static Map<String, Object> set( Integer slot, Integer value ) {
        return set( slot, value, -1 );
    }

    public static Map<String, Object> set( Integer slot, Integer value, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "slot", slot );
        args.put( "value", value );
        args.put( "token", token );

        return sendRequest( "set", args );
    }

    public static Map<String, Object> get( Integer slot, Integer value ) {
        return get( slot, value, -1 );
    }

    public static Map<String, Object> get( Integer slot, Integer value, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "slot", slot );
        args.put( "value", value );
        args.put( "token", token );

        return sendRequest( "get", args );
    }

    public static Map<String, Object> feature( String name, Integer value ) {
        return feature( name, value, -1 );
    }

    public static Map<String, Object> feature( String name, Integer value, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "name", name );
        args.put( "value", value );
        args.put( "token", token );

        return sendRequest( "feature", args );
    }

    public static Map<String, Object> getFeature( String name ) {
        return getFeature( name, -1 );
    }

    public static Map<String, Object> getFeature( String name, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "name", name );
        args.put( "token", token );

        return sendRequest( "getFeature", args );
    }

    public static Map<String, Object> lightning( Integer room, Integer value, Integer brightness, Integer color ) {
        return lightning( room, value, brightness, color, -1 );
    }

    public static Map<String, Object> lightning( Integer room, Integer value, Integer brightness, Integer color, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "value", value );
        args.put( "brightness", brightness );
        args.put( "color", color );
        args.put( "token", token );

        return sendRequest( "lightning", args );
    }

    /**
     * Allows managing lighting in room #room. Value 1 enables lighting. Value 0 disabls
     *
     * @param room  - room ID [0-3]
     * @param value - [enabled|disabled] [1|0]
     * @return - map of response arguments [success - Integer [0-1]]
     */

    public static Map<String, Object> setEnabled( Integer room, Integer value ) {
        return setEnabled( room, value, -1 );
    }

    public static Map<String, Object> setEnabled( Integer room, Integer value, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "value", value );
        args.put( "token", token );

        return sendRequest( "setEnabled", args );
    }

    public static Map<String, Object> getEnabled( Integer room ) {
        return getEnabled( room, -1 );
    }

    public static Map<String, Object> getEnabled( Integer room, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "token", token );

        return sendRequest( "setEnabled", args );
    }

    public static Map<String, Object> brightness( Integer room, Integer brightness ) {
        return brightness( room, brightness, -1 );
    }

    public static Map<String, Object> brightness( Integer room, Integer brightness, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "brightness", brightness );
        args.put( "token", token );

        return sendRequest( "brightness", args );
    }

    public static Map<String, Object> getBrightness( Integer room ) {
        return getBrightness( room, -1 );
    }

    public static Map<String, Object> getBrightness( Integer room, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "token", token );

        return sendRequest( "getBrightness", args );
    }

    public static Map<String, Object> color( Integer room, Integer color ) {
        return color( room, color, -1 );
    }

    public static Map<String, Object> color( Integer room, Integer color, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "color", color );
        args.put( "token", token );

        return sendRequest( "color", args );
    }

    public static Map<String, Object> getColor( Integer room ) {
        return getColor( room, -1 );
    }

    public static Map<String, Object> getColor( Integer room, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "token", token );

        return sendRequest( "getColor", args );
    }

    public static Map<String, Object> mode( Integer room, Integer value ) {
        return mode( room, value, -1, -1 );
    }

    public static Map<String, Object> mode( Integer room, Integer value, Integer duration, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "value", value );
        args.put( "duration", duration );
        args.put( "token", token );

        return sendRequest( "mode", args );
    }

    public static Map<String, Object> getMode( Integer room ) {
        return mode( room, -1 );
    }

    public static Map<String, Object> getMode( Integer room, Integer token ) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "token", token );

        return sendRequest( "getMode", args );
    }

    /**===========================================================================**/

    public static Map<String, Object> sendRequest( String method, Map<String, Object> args ) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            HttpURLConnection connection = connectTo( method + parseAgrs( args ) );
            InputStream stream = new BufferedInputStream( connection.getInputStream() );

            byte[] dataBytes = new byte[stream.available()];
            stream.read( dataBytes );

            String data = new String( dataBytes );

            Log.v( TAG, data );

            parseData( map, data );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return map;
    }

    private static String parseAgrs( Map<String, Object> args ) {
        if ( args == null || args.size() == 0 )
            return "";

        String arguments = "";
        boolean isFirst = true;

        for ( Map.Entry<String, Object> entry : args.entrySet() ) {
            String value = "";

            if ( entry.getValue() instanceof String ) {
                value = (String) entry.getValue();
            } else if ( entry.getValue() instanceof Double ) {
                if ( (Double) entry.getValue() == -1 )
                    continue;

                value = String.valueOf( (Double) entry.getValue() );
            } else if ( entry.getValue() instanceof Integer ) {
                if ( (Integer) entry.getValue() == -1 )
                    continue;

                value = String.valueOf( (Integer) entry.getValue() );
            } else if ( entry.getValue() instanceof Boolean ) {
                value = String.valueOf( (Boolean) entry.getValue() );
            } else
                continue;

            if ( isFirst ) {
                arguments += "?" + entry.getKey() + "=" + value;
                isFirst = false;
            } else
                arguments += "&" + entry.getKey() + "=" + value;
        }

        return arguments;
    }

    private static void parseData( Map<String, Object> map, String data ) {
        String pairs[] = data.split( "&" );

        for ( String pair : pairs ) {
            int indexOf = pair.indexOf( '=' );

            String key = pair.substring( indexOf );
            String value = pair.substring( indexOf + 1 );

            try {
                map.put( key, Boolean.parseBoolean( value ) );
            } catch ( Exception e ) {
                try {
                    map.put( key, Double.parseDouble( value ) );
                } catch ( Exception e1 ) {
                    try {
                        map.put( key, Integer.parseInt( value ) );
                    } catch ( Exception e2 ) {
                        map.put( key, value );
                    }
                }
            }
        }
    }

    static {
        disableConnectionReuseIfNecessary();
    }

    private static HttpURLConnection connectTo( String urlString ) {
        return connectTo( urlString, 0 );
    }

    private static HttpURLConnection connectTo( final String urlString, final int depth ) {
        final HttpURLConnection[] connection = new HttpURLConnection[1];

        if ( depth < RECONNECT_DEPTH ) {
            try {
                URL url = new URL( String.format( "http://%s/%s", HOME_IP, urlString ) );
                connection[0] = (HttpURLConnection) url.openConnection();
            } catch ( Exception e ) {
                e.printStackTrace();
                Log.v( TAG, String.format( "HttpURLConnection to ip %s/%s failed. Reconnecting in %dms", HOME_IP, urlString, RECONNECT_TIME ) );

                new Timer().schedule( new TimerTask() {
                    @Override
                    public void run() {
                        connection[0] = connectTo( urlString, depth + 1 );
                    }
                }, RECONNECT_TIME );
            }
        } else
            connection[0] = null;

        isConnected = ( connection[0] != null );
        return connection[0];
    }

    private static void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if ( Integer.parseInt( Build.VERSION.SDK ) < Build.VERSION_CODES.FROYO ) {
            System.setProperty( "http.keepAlive", "false" );
        }
    }
}