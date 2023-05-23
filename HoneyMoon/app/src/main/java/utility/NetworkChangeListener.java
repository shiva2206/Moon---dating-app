package utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.honeymoon.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!common.isConnectedToInternet(context)){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            View laydesign = LayoutInflater.from(context).inflate(R.layout.custom_check_internet,null);
            builder.setView(laydesign);

            Button retry = laydesign.findViewById(R.id.retry);


            AlertDialog dialog= builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context,intent);
                }
            });
        }
    }
}
