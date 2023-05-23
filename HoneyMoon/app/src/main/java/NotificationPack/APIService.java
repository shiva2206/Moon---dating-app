package NotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
   @Headers(
       {
                   "Content-Type:application/json",
                   "Authorization:key=AAAAbUum9D4:APA91bGRw_sHqKsIHL6Wj6xyjtDeGF5I_Q5FfrPclbVdcKxHSXb3WSuECQOjlhsKsjPrwX_DRkTqofngqLQcCZpea-ouweAXmPR5q-mKp8hP9KpTIw19RMO2sk9pmFrcPs9lUlJkbI8E"
       }
   )
   @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);



}
