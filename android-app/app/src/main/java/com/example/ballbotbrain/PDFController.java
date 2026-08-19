package com.example.ballbotbrain;

public class PDFController implements Runnable{
    private float pitch, roll, pitchR, rollR;
    public void updateValues(float pitch, float roll, float pitchR, float rollR) {
        this.pitch = pitch;
        this.roll = roll;
        this.pitchR = pitchR;
        this.rollR = rollR;
    }
    @Override
    public void run() {

    }
}
