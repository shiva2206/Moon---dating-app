package model;

import java.util.HashMap;

public class constants {
    //ME DEFINED
    public static final String SWIPERIGHT="swipes";
    public static final String SWIPED="swiped";


    //OTHERS NECCESSARY CONSTANTS
    static final int LOCATION_SERVICE_ID =175;


    public static final String ACTION_START_LOCATION_SERVICE ="startlocationservice";
    public static final String ACTION_STOP_LOCATION_SERVICE="stoplocationservice";


    //retrofit call
    public static final String REMOTE_MSG_AUTHORIZATION="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE="Content-Type";


    public static final String REMOTE_MSG_TYPE="type";
    public static final String REMOTE_MSG_INVITATION="invitation";
    public static final String REMOTE_MSG_INVITER_TOKEN="inviterToken";
    public static final String REMOTE_MSG_DATA ="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";


    public static HashMap<String,String> getRemoteMessageHeaders(){
        HashMap<String,String> headers = new HashMap<>();
        headers.put(
                REMOTE_MSG_AUTHORIZATION,
                "key=AAAAbUum9D4:APA91bGRw_sHqKsIHL6Wj6xyjtDeGF5I_Q5FfrPclbVdcKxHSXb3WSuECQOjlhsKsjPrwX_DRkTqofngqLQcCZpea-ouweAXmPR5q-mKp8hP9KpTIw19RMO2sk9pmFrcPs9lUlJkbI8E");
        headers.put(REMOTE_MSG_CONTENT_TYPE,"application/json");
        return headers;
    }
}
