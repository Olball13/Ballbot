package com.example.ballbotbrain;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class IMU implements SensorEventListener {

    // Volatile to update to all systems quickly
    // Use doubles or floats? Is the precision insignificant compared to the speed gain?
    private volatile float pitch;
    private volatile float roll;
    private volatile float yaw;
    public interface IMUListener {
        void onOrientationChanged( float pitch, float roll, float yaw);
    }

    float[] rotationMatrix = new float[9];
    float[] orientationAngles = new float[3];

    private final SensorManager sensorManager;
    private Sensor sensor;
    private IMUListener listener;

    public IMU(Context context) {
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null) {
            // Sensor Available
            // Game Rotation Vector doesn't use magnetometer so no Magnetic Distortion
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        }
    }

    // Setter for listener
    public void setListener(IMUListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check if the sensor that is used is the one that changed
        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {

            // Get rotation matrix and convert to orientation angels
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            yaw   = (float) Math.toDegrees(orientationAngles[0]); // Rotation around Z-axis
            pitch = (float) Math.toDegrees(orientationAngles[1]); // Rotation around X-axis
            roll = (float) Math.toDegrees(orientationAngles[2]); // Rotation around Y-axis

            // Send data to the Activity
            if (listener != null) {
                listener.onOrientationChanged(pitch, roll, yaw);
            }
        }
    }

    public void startListening() {
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST); // Needed for quick corrections
        }
    }

    public void stopListening() {
        if (sensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
