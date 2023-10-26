package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class FaceVerification extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private CameraManager cameraManager;
    private String cameraId;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession captureSession;
    private SurfaceView surfaceView;
    //private CircleImageView imageViewer;

    ImageView imageViewer;
    private ImageReader imageReader;
    ImageButton captureButton;
    Button buttonSubmit;
    ImageButton backButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verification);

        buttonSubmit = findViewById(R.id.submitBtn);


        imageReader = ImageReader.newInstance(/* width */ 640, /* height */ 480, ImageFormat.JPEG, /* maxImages */ 1);

        backButton3 = findViewById(R.id.backButton3);
        backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaceVerification.this, IdentityVerification2.class);
                startActivity(intent);
            }
        });

        // Now you have access to the data in the new activity.

        Button retakeBtn = findViewById(R.id.retakeBtn);
        retakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureButton.setClickable(true);
                closeCamera();
                deleteImageFile();
                surfaceView.setVisibility(View.VISIBLE);
                openCamera();
            }
        });

        surfaceView = findViewById(R.id.surfaceView);
        imageViewer = findViewById(R.id.imageViewer);

        captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(v -> {
            surfaceView.setVisibility(View.INVISIBLE);


            captureButton.setClickable(false);

            captureImage();
            buttonSubmit.setClickable(true);

        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();

                String Name = intent.getStringExtra("name");
                String Email = intent.getStringExtra("email");
                String Number = intent.getStringExtra("number");
                String Password = intent.getStringExtra("password");
                String RePassword = intent.getStringExtra("repassword");

                closeCamera();
                Intent intents = new Intent(FaceVerification.this, UploadDriversLicense.class);
                intents.putExtra("name", Name);
                intents.putExtra("email", Email);
                intents.putExtra("number", Number);
                intents.putExtra("password", Password);
                intents.putExtra("repassword", RePassword);
                startActivity(intents);
            }
        });



        // Check and request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            openCamera();
        }

    }



    // Function to close the camera
    private void closeCamera() {
        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }

    }

    // Function to delete the captured image file
    private void deleteImageFile() {
        // Construct the path to the captured image file based on your directory structure
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File appDirectory = new File(downloadsDirectory, "YourAppName");
        File imageFile = new File(appDirectory, "captured_image.jpg");

        if (imageFile.exists()) {
            if (imageFile.delete()) {
                Log.d("CAMERAPOPERS", "Image file deleted.");
            } else {
                Log.e("CAMERAPOPERS", "Failed to delete image file.");
            }
        }
    }

    private void captureImage() {
        if (cameraDevice == null) {
            Log.e("CAMERAPOPERS", "CameraDevice is null");
            return;
        }

        try {
            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(imageReader.getSurface());

            // Create a CameraCaptureSession
            cameraDevice.createCaptureSession(Arrays.asList(imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureSession = session;
                    try {
                        captureSession.capture(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                                super.onCaptureCompleted(session, request, result);

                                Log.e("CAMERAPOPERS", "loob ng capturesession");
                                // Image capture completed, you can save the image here
                                String fileName = saveImage(imageReader.acquireNextImage());

                                // Apply brightness adjustment to the image
                                Bitmap bitmap = BitmapFactory.decodeFile(fileName);
                                Bitmap adjustedBitmap = adjustBrightnessAndRotate(bitmap, 1F,-90); // Adjust brightness factor as needed

                                // DITO MAG DISPLAY SA IMAGEVIEW
                                // Java
                                ImageView imageView = findViewById(R.id.imageViewer);
                                imageView.setImageBitmap(adjustedBitmap);

                            }
                        }, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        Log.e("CAMERAPOPERS", "CameraAccessException: " + e.getMessage());
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e("CAMERAPOPERS", "CaptureSession configuration failed");
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Function to adjust image brightness
    private Bitmap adjustBrightnessAndRotate(Bitmap bitmap, float brightnessFactor, float rotationAngle) {
        // Apply brightness adjustment
        ColorMatrix cm = new ColorMatrix();
        cm.set(new float[] {
                brightnessFactor, 0, 0, 0, 0,  // Red
                0, brightnessFactor, 0, 0, 0,  // Green
                0, 0, brightnessFactor, 0, 0,  // Blue
                0, 0, 0, 1, 0                // Alpha
        });

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        Paint paint = new Paint();
        paint.setColorFilter(filter);

        Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(adjustedBitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        // Rotate the image
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);

        return Bitmap.createBitmap(adjustedBitmap, 0, 0, adjustedBitmap.getWidth(), adjustedBitmap.getHeight(), matrix, true);
    }





    private String saveImage(Image image) {
        if (image == null) {
            Log.e("CAMERAPOPERS", "Image is null");
            return null;
        }

        // Specify the Downloads folder as the parent directory
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create a subdirectory for your app if needed
        File appDirectory = new File(downloadsDirectory, "YourAppName");
        if (!appDirectory.exists()) {
            if (!appDirectory.mkdirs()) {
                Log.e("CAMERAPOPERS", "Failed to create app directory");
                return null;
            }

        }
        // Create the file in the app directory
        File imageFile = new File(appDirectory, "captured_image.jpg");

        try (FileOutputStream output = new FileOutputStream(imageFile)) {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CAMERAPOPERS", "IOException: " + e.getMessage());
            return null;
        } finally {
            image.close();
        }

        // Notify the MediaScanner to refresh the gallery with the newly captured image
        MediaScannerConnection.scanFile(this, new String[]{imageFile.getPath()}, null, null);

        String imagePath = imageFile.getPath();
        //Toast.makeText(this, "Image saved to Downloads folder: " + imagePath, Toast.LENGTH_SHORT).show();

        return imagePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        String frontCameraId = null;

        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    frontCameraId = cameraId; // Store the front camera ID
                    break;
                }
            }

            if (frontCameraId != null) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraManager.openCamera(frontCameraId, cameraStateCallback, null);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                }
            } else {
                Log.e("CAMERAPOPERS", "Front camera not available on this device.");
                Toast.makeText(this, "Front camera not available on this device.", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e("CAMERAPOPERS", "CameraAccessException: " + e.getMessage());
        }
    }

    private final CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void createCameraPreview() {
        try {
            Surface surface = surfaceView.getHolder().getSurface();
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(
                    Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            if (cameraDevice == null) {
                                return;
                            }
                            captureSession = session;
                            try {
                                captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Toast.makeText(FaceVerification.this, "Failed to create camera preview.", Toast.LENGTH_SHORT).show();
                        }
                    },
                    null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }





}