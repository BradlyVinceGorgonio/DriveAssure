package com.example.driveassure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadDocumentContract extends AppCompatActivity {

    TextView textView25;
    Button button;
    private static final String CHANNEL_ID = "DownloadChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_document_contract);

        createNotificationChannel();
        textView25 = findViewById(R.id.textView25);
        textView25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadDocx(view);
                Toast.makeText(getApplicationContext(),"Contract Downloaded", Toast.LENGTH_SHORT).show();
            }
        });

        button = findViewById(R.id.nextBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DownloadDocumentContract.this,PaymentAndSignature.class);
                startActivity(intent);
            }
        });
    }

    public void downloadDocx(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            new DownloadFileTask(this).execute();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static class DownloadFileTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        DownloadFileTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = context.getResources().openRawResource(R.raw.driveassurecontract);
            File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File outputFile = new File(outputDir, "rentalagreement.docx");

            try {
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }

                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                bufferedInputStream.close();
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, e.g., display a toast message
                publishProgress(); // Inform onPostExecute about the error
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showDownloadCompleteNotification();
        }

        private void showDownloadCompleteNotification() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.heart_icon) // Use your custom small icon
                    .setContentTitle("Download Complete")
                    .setContentText("Rental Agreement.docx file downloaded")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, builder.build());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(context, "Error downloading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Channel";
            String description = "Channel for download notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}