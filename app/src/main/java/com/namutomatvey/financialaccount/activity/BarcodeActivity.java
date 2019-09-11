package com.namutomatvey.financialaccount.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.namutomatvey.financialaccount.R;

import java.io.IOException;

public class BarcodeActivity  extends AppCompatActivity {

  SurfaceView cameraView;
  BarcodeDetector barcode;
  CameraSource cameraSource;
  SurfaceHolder holder;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_barcode);

    cameraView = findViewById(R.id.cameraView);
    cameraView.setZOrderMediaOverlay(true);
    holder = cameraView.getHolder();

    barcode = new BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build();

    if (!barcode.isOperational()) {
      Toast.makeText(getApplicationContext(), "Не удалось настроить детектор!", Toast.LENGTH_SHORT).show();
      this.finish();
    }

    cameraSource = new CameraSource.Builder(this, barcode)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedFps(15)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1920, 1024)
            .build();

    cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        try {
          if(ContextCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraSource.start(cameraView.getHolder());
          }
        }
        catch (IOException e) {
          e.printStackTrace();
          finish();
        }
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {
        cameraSource.stop();
      }
    });

    barcode.setProcessor(new Detector.Processor<Barcode>() {
      @Override
      public void release() {

      }

      @Override
      public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray <Barcode> barcodes = detections.getDetectedItems();
        if(barcodes.size() > 0) {
          Intent intent = new Intent();
          intent.putExtra("barcode", barcodes.valueAt(0));
          setResult(RESULT_OK, intent);
          finish();
        }
      }
    });
  }
}