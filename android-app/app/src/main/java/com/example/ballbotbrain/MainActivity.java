package com.example.ballbotbrain;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements IMU.IMUListener {

    IMU imu;
    TextView pitchInput, rollInput, yawInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pitchInput = findViewById(R.id.pitchInput);
        rollInput = findViewById(R.id.rollInput);
        yawInput = findViewById(R.id.yawInput);

        imu = new IMU(this);
        imu.setListener(this); // Register MainActivity to receive the updates
    }

    @Override
    public void onOrientationChanged(float pitch, float roll, float yaw) {
        pitchInput.setText(String.format("Pitch: " + pitch + " deg"));
        rollInput.setText(String.format("Roll: " + roll + " deg"));
        yawInput.setText(String.format("Yaw: " + yaw + " deg"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        imu.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        imu.stopListening();
    }
}