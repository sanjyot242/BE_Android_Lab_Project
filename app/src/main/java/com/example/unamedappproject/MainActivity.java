package com.example.unamedappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText inputUsernme,inputPassword;
    private Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsernme=findViewById(R.id.username);
        inputPassword=findViewById(R.id.password);
        signIn=findViewById(R.id.signIn);

        signIn.setOnClickListener(v->{
                if(TextUtils.isEmpty(inputPassword.getText())||TextUtils.isEmpty(inputPassword.getText())){
                    Toast.makeText(this, "Please Fill all details", Toast.LENGTH_SHORT).show();
                }else{
                    signIn();
                }
        });
    }

    private void signIn() {
        Toast.makeText(this, inputUsernme.getText()+"\n"+inputPassword.getText(), Toast.LENGTH_SHORT).show();
    }
}
