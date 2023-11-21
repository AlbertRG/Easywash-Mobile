package com.h2o.easywash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RecoverPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Intent intent;
    private TextView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        email = findViewById(R.id.setEmailRecoveryText);
        send = findViewById(R.id.txtEmailSend);

        ImageView back = findViewById(R.id.imgBackRecovery);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }//onCreate

    public void sendRecoveryEmail(View view) {
        send.setText("Correo Enviado");
    }//sendRecoveryEmail

}