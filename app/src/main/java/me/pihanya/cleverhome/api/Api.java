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
    private static int RECONNECT_TIME;
    private static int RECONNECT_DEPTH;

    private static String DEFAULT_PASSWORD = "qwerty123";

    private static boolean isConnected = false;

    private static String homeIp;

    public static Map<String, Object> auth (String password, Integer token) {
        Map<String, Object> args = new HashMap<>();

        args.put( "password", password != null ? password : DEFAULT_PASSWORD );
        args.put( "token", token );

        return sendRequest( "auth", args );
    }

    public static Map<String, Object> auth (String password) {
        Map<String, Object> args = new HashMap<>();

        args.put( "password", password != null ? password : DEFAULT_PASSWORD );

        return sendRequest( "auth", args );
    }

    public static Map<String, Object> isAuthorized (Integer token) {
        Map<String, Object> args = new HashMap<>();

        args.put( "token", token );

        return sendRequest( "isAuthorized", args );
    }

    public static Map<String, Object> isAuthorized () {
        Map<String, Object> args = new HashMap<>();

        return sendRequest( "isAuthorized", args );
    }

    public static Map<String, Object> set (Integer slot, Integer value, Integer token) {
        Map<String, Object> args = new HashMap<>();

            args.put( "slot", slot );
            args.put( "value", value );
            args.put( "token", token );

        return sendRequest( "set", args );
    }

    public static Map<String, Object> set (Integer slot, Integer value) {
        Map<String, Object> args = new HashMap<>();

            args.put( "slot", slot );
            args.put( "value", value );

        return sendRequest( "set", args );
    }

    public static Map<String, Object> guard (Integer value, Integer token) {
        Map<String, Object> args = new HashMap<>();

        args.put( "value", value );
        args.put( "token", token );

        return sendRequest( "guard", args );
    }

    public static Map<String, Object> guard (Integer value) {
        Map<String, Object> args = new HashMap<>();

        args.put( "value", value );

        return sendRequest( "guard", args );
    }

    public static Map<String, Object> isGuard (Integer token) {
        Map<String, Object> args = new HashMap<>();

        args.put( "token", token );

        return sendRequest( "isGuard", args );
    }

    public static Map<String, Object> isGuard () {
        Map<String, Object> args = new HashMap<>();


        return sendRequest( "isGuard", args );
    }

    public static Map<String, Object> feature (String name, Integer value, Integer token) {
        Map<String, Object> args = new HashMap<>();

        args.put( "name", name );
        args.put( "value", value );
        args.put( "token", token );

        return sendRequest( "feature", args );
    }

    public static Map<String, Object> feature (String name, Integer value) {
        Map<String, Object> args = new HashMap<>();

        args.put( "name", name );
        args.put( "value", value );

        return sendRequest( "feature", args );
    }

    public static Map<String, Object> getFeature (String name, Integer token) {
        Map<String, Object> args = new HashMap<>();

        args.put( "name", name );
        args.put( "token", token );

        return sendRequest( "getFeature", args );
    }

    public static Map<String, Object> getFeature (String name) {
        Map<String, Object> args = new HashMap<>();

        args.put( "name", name );

        return sendRequest( "getFeature", args );
    }

    public static Map<String, Object> lightning (Integer room, Integer value, Integer brightness, Integer color, Integer token) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "value", value );
        args.put( "brightness", brightness );
        args.put( "color", color );
        args.put( "token", token );

        return sendRequest( "lightning", args );
    }

    public static Map<String, Object> lightning (Integer room, Integer value, Integer brightness, Integer color) {
        Map<String, Object> args = new HashMap<>();

        args.put( "room", room );
        args.put( "value", value );
        args.put( "brightness", brightness );
        args.put( "color", color );

        return sendRequest( "lightning", args );
    }

    public static Map<String, Object> sendRequest (String method, Map<String, Object> args) {
        HashMap<String, Object> map = new HashMap<>();

        if ( !isConnected )
            return map;

        try {
            HttpURLConnection connection = connectTo( method + parseAgrs( args ) );
            InputStream stream = new BufferedInputStream( connection.getInputStream() );

            byte[] dataBytes = new byte[stream.available()];
            stream.read( dataBytes );

            String data = new String( dataBytes );

            parseData( map, data );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private static String parseAgrs (Map<String, Object> args) {
        if ( args == null || args.size() == 0 )
            return "";

        String arguments = "?";
        boolean isFirst = true;

        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String value = "";

            if ( entry.getValue() instanceof String )
                value = (String) entry.getValue();
            else if ( entry.getValue() instanceof Double )
                value = String.valueOf( (Double) entry.getValue() );
            else if ( entry.getValue() instanceof Integer )
                value = String.valueOf( (Integer) entry.getValue() );
            else if ( entry.getValue() instanceof Boolean )
                value = String.valueOf( (Boolean) entry.getValue() );

            if ( isFirst ) {
                arguments += entry.getKey() + "=" + value;
                isFirst = false;
            } else
                arguments += "&" + entry.getKey() + "=" + value;
        }

        return arguments;
    }

    private static void parseData (Map<String, Object> map, String data) {
        String pairs[] = data.split( "&" );

        for (String pair : pairs) {
            int indexOf = pair.indexOf( '=' );

            String key = pair.substring( indexOf );
            String value = pair.substring( indexOf + 1 );

            try {
                map.put( key, Double.parseDouble( value ) );
            } catch (Exception e) {
                try {
                    map.put( key, Integer.parseInt( value ) );
                } catch (Exception e1) {
                    map.put( key, value );
                }
            }
        }
    }

    static {
        disableConnectionReuseIfNecessary();
    }

    private static HttpURLConnection connectTo (String urlString) {
        return connectTo( urlString, 0 );
    }

    private static HttpURLConnection connectTo (final String urlString, final int depth) {
        final HttpURLConnection[] connection = new HttpURLConnection[1];

        if ( depth < RECONNECT_DEPTH ) {
            try {
                URL url = new URL( String.format( "http://%s/%s", homeIp, urlString ) );
                connection[0] = (HttpURLConnection) url.openConnection();
            } catch (Exception e) {
                e.printStackTrace();
                Log.v( "HomeConnection", String.format( "HttpURLConnection to ip %s/%s failed. Reconnecting in %dms", homeIp, urlString, RECONNECT_TIME ) );

                new Timer().schedule( new TimerTask() {
                    @Override
                    public void run () {
                        connection[0] = connectTo( urlString, depth + 1 );
                    }
                }, RECONNECT_TIME );
            }
        } else
            connection[0] = null;

        isConnected = ( connection[0] != null );
        return connection[0];
    }

    private static void disableConnectionReuseIfNecessary () {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if ( Integer.parseInt( Build.VERSION.SDK ) < Build.VERSION_CODES.FROYO ) {
            System.setProperty( "http.keepAlive", "false" );
        }
    }
}
