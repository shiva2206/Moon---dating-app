package NotificationPack;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if (fbuser!=null){
            updateToken(refreshToken);
        }
    }
    public void updateToken(String refreshToken){
        FirebaseUser fbuser =FirebaseAuth.getInstance().getCurrentUser();
        Token token1 = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("users").child(fbuser.getUid())
                .child("devicetoken").setValue(token1);
    }


}
