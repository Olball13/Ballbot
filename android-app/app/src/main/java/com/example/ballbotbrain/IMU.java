package com.example.ballbotbrain;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class IMU implements SensorEventListener {

    // Volatile to update to all systems quickly
    private volatile float pitch, roll, yaw, pitchR, rollR, yawR;

    // Custom Listener
    public interface IMUListener {
        void onOrientationChanged( float pitch, float roll, float yaw, float pitchR, float rollR, float yawR);
    }

    float[] rotationMatrix = new float[9];
    float[] orientationAngles = new float[3];

    private final SensorManager sensorManager;
    private Sensor gameRotationVectorSensor;
    private Sensor gyroscopeSensor;
    private IMUListener listener;

    public IMU(Context context) {
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            // Sensor Available
            // Gyroscope to measure the rate of angular change for the derivative term of the control loop
            gameRotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null) {
            // Sensor Available
            // Game Rotation Vector doesn't use magnetometer so no Magnetic Distortion
            gameRotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        }

    }

    // Setter for listener
    public void setListener(IMUListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (listener == null) return; // No listener set! Nowhere to update too

        // Check if the sensor that is used is the one that changed
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            // Update rotation rate variables
            yawR = event.values[0];
            pitchR = event.values[1];
            rollR = event.values[2];

            // Send data to the Activity
            listener.onOrientationChanged(pitch, roll, yaw, pitchR, rollR, yawR);

        } else if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {

            // Get rotation matrix and convert to orientation angels
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            // Update rotation variables
            yaw   = (float) Math.toDegrees(orientationAngles[0]); // Rotation around Z-axis
            pitch = (float) Math.toDegrees(orientationAngles[1]); // Rotation around X-axis
            roll = (float) Math.toDegrees(orientationAngles[2]); // Rotation around Y-axis

            // Send data to the Activity
            listener.onOrientationChanged(pitch, roll, yaw, pitchR, rollR, yawR);
        }
    }

    public void startListening() {
        if (gameRotationVectorSensor != null || gyroscopeSensor != null) {
            sensorManager.registerListener(this, gameRotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST); // Needed for quick corrections
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST); // Needed for quick corrections
        }
    }

    public void stopListening() {
        if (gameRotationVectorSensor != null || gyroscopeSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
