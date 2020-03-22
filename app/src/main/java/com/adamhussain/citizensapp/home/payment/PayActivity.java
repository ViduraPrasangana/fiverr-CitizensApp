package com.adamhussain.citizensapp.home.payment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.adamhussain.citizensapp.R;
import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;

public class PayActivity extends AppCompatActivity {

    private TextView title;
    private CardForm cardForm;

    private PayItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        item = getIntent().getParcelableExtra("item");
        createNotificationChannel();

        title = findViewById(R.id.title);
        title.setText(item.getTitle());
        cardForm = findViewById(R.id.card_form);

        TextView amount = (cardForm.getRootView().findViewById(R.id.payment_amount));
        Button btnPay = (cardForm.getRootView().findViewById(R.id.btn_pay));
        int value  =(int) Math.round(Math.random()*1000);
        amount.setText("$"+ value +".00");
        btnPay.setText("Pay $"+ value +".00");


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(PayActivity.this, "payment")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Payment Successful")
                .setContentText(item.getTitle()+" payment successfully done")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(PayActivity.this,"Payment Successful",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PayActivity.this.finish();


                    }
                },1000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PayActivity.this);
                        notificationManager.notify(1234, builder.build());
                    }
                },3000);
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("payment", name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
