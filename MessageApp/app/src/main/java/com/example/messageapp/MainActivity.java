package com.example.messageapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private TextView tvMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMessages = findViewById(R.id.tv_messages);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            // kieerm tra uwng dung da duoc cap quyen hay chua READ_SMS hay chuaw
            // kiem tra quyen PERMISSION_GRANTED da dc cap hay chua neu da dc cap thif thif goi
            //ActivityCompat.requestPermissions Quyền được yêu cầu là READ_SMS, và sử dụng mã yêu cầu SMS_PERMISSION_CODE.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
        } else {
            // nguoc lai neu chua thi
            readSmsMessages();
        }
    }
    private void readSmsMessages() {
        ContentResolver contentResolver = getContentResolver(); // dung de truy cap du lieu qua  ContentResolver
        Cursor smsCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        // duiong dan de doc tin nhan o trong ung dung
        if (smsCursor != null) {
            StringBuilder smsBuilder = new StringBuilder();
            while (smsCursor.moveToNext()) {
                String address = smsCursor.getString(smsCursor.getColumnIndex("address")); // lay dia chi cua tin nhan
                String body = smsCursor.getString(smsCursor.getColumnIndex("body")); // lay noi dung cua tin nhan
                smsBuilder.append("Từ: ").append(address).append("\n");
                smsBuilder.append("Nội dung: ").append(body).append("\n\n");
                tvMessages.setText(smsBuilder.toString()); // gom nhung doan tin nhan thanh 1 chuoi hien thi
                smsCursor.close();
            }
        }
    }
    // Xử lý kết quả yêu cầu quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) { // kiem tra co dung ma gui ve theo quyen truy cap hay khong
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // neu dung quyen truy cao thi truy xuat xuat tin nhan
                readSmsMessages();
            } else {
                // nguoc lai hien thi thong bao
                tvMessages.setText("Không có quyền truy cập tin nhắn SMS");
            }
        }
    }
}