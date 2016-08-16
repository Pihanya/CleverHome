package me.pihanya.cleverhome.api;

import android.os.Build;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by pihanya on 11.08.16.
 */
public class Api {
    private static Timer timer = new Timer();

    private static int RECONNECT_TIME = 1000;
    private static int RECONNECT_DEPTH = 5;
    private static int API_DELAY = 1;

    private static String TAG = "API";
    private static String HOME_IP = "192.168.1.48:8080";
    private static String DEFAULT_PASSWORD = "qwerty123";

    private static boolean isAsycn = true;
    private static boolean isConnected = false;
    private static boolean isAuthificated = false;

    /***============================== [Arduino API] ==============================***/

    public static Map<String, Object> auth( String password ) {
        return auth( password, -1 );
    }

    public static Map<String, Object> auth( final String password, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "password", password != null ? password : DEFAULT_PASSWORD ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "auth", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> isAuthorized() {
        return isAuthorized( -1 );
    }

    public static Map<String, Object> isAuthorized( final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "isAuthorized", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> set( Integer slot, Integer value ) {
        return set( slot, value, -1 );
    }

    public static Map<String, Object> set( final Integer slot, final Integer value, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "slot", slot ) );
                args.add( new Pair<String, Object>( "value", value ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "set", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> get( Integer slot, Integer value ) {
        return get( slot, value, -1 );
    }

    public static Map<String, Object> get( final Integer slot, final Integer value, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "slot", slot ) );
                args.add( new Pair<String, Object>( "value", value ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "get", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> feature( String name, Integer value ) {
        return feature( name, value, -1 );
    }

    public static Map<String, Object> feature( final String name, final Integer value, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "name", name ) );
                args.add( new Pair<String, Object>( "value", value ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "feature", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> getFeature( String name ) {
        return getFeature( name, -1 );
    }

    public static Map<String, Object> getFeature( final String name, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "name", name ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "getFeature", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> lightning( Integer room, Integer value, Integer brightness, Integer color ) {
        return lightning( room, value, brightness, color, -1 );
    }

    public static Map<String, Object> lightning( final Integer room, final Integer value, final Integer brightness, final Integer color, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "value", value ) );
                args.add( new Pair<String, Object>( "brightness", brightness ) );
                args.add( new Pair<String, Object>( "color", color ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "lightning", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
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

    public static Map<String, Object> setEnabled( final Integer room, final Integer value, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "value", value ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "setEnabled", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> getEnabled( Integer room ) {
        return getEnabled( room, -1 );
    }

    public static Map<String, Object> getEnabled( final Integer room, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "getEnabled", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> brightness( Integer room, Integer brightness ) {
        return brightness( room, brightness, -1 );
    }

    public static Map<String, Object> brightness( final Integer room, final Integer brightness, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "brightness", brightness ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "brightness", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> getBrightness( Integer room ) {
        return getBrightness( room, -1 );
    }

    public static Map<String, Object> getBrightness( final Integer room, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "getBrightness", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> color( Integer room, final Integer red, final Integer green, final Integer blue ) {
        return color( room, red, green, blue, -1 );
    }

    public static Map<String, Object> color( final Integer room, final Integer red, final Integer green, final Integer blue, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "r", red ) );
                args.add( new Pair<String, Object>( "g", green ) );
                args.add( new Pair<String, Object>( "b", blue ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "color", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> getColor( Integer room ) {
        return getColor( room, -1 );
    }

    public static Map<String, Object> getColor( final Integer room, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "getColor", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> mode( Integer room, Integer value ) {
        return mode( room, value, -1, -1 );
    }

    public static Map<String, Object> mode( final Integer room, final Integer value, final Integer duration, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "value", value ) );
                args.add( new Pair<String, Object>( "duration", duration ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "mode", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    public static Map<String, Object> getMode( Integer room ) {
        return mode( room, -1 );
    }

    public static Map<String, Object> getMode( final Integer room, final Integer token ) {
        final Object[] map = new Object[1];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Pair<String, Object>> args = new ArrayList<>();

                args.add( new Pair<String, Object>( "room", room ) );
                args.add( new Pair<String, Object>( "token", token ) );

                map[0] = sendRequest( "getMode", args );
            }
        };

        if ( isAsycn )
            timer.schedule( task, API_DELAY );
        else
            task.run();

        return (Map<String, Object>) map[0];
    }

    /**
     * ===========================================================================
     **/

    public static Map<String, Object> sendRequest( String method, ArrayList<Pair<String, Object>> args ) {
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

    private static String parseAgrs( ArrayList<Pair<String, Object>> args ) {
        if ( args == null || args.size() == 0 )
            return "";

        String arguments = "";
        boolean isFirst = true;

        for ( Pair<String, Object> pair : args ) {
            String value;

            if ( pair.second instanceof String ) {
                value = (String) pair.second;
            } else if ( pair.second instanceof Double ) {
                if ( (Double) pair.second == -1 )
                    continue;

                value = String.valueOf( pair.second );
            } else if ( pair.second instanceof Integer ) {
                if ( (Integer) pair.second == -1 )
                    continue;

                value = String.valueOf( pair.second );
            } else if ( pair.second instanceof Boolean ) {
                value = String.valueOf( pair.second );
            } else
                continue;

            if ( isFirst ) {
                arguments += "?" + pair.first + "=" + value;
                isFirst = false;
            } else
                arguments += "&" + pair.first + "=" + value;

            Log.v( TAG, pair.first + " " + value );
        }

        Log.v( TAG, "=========================" );
        Log.v( TAG, "ARGUMENTS: " + arguments );

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