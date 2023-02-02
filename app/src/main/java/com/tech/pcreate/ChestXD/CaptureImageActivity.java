package com.tech.pcreate.ChestXD;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.FloatingActionButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.PendingIntent.getActivity;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class CaptureImageActivity extends AppCompatActivity {

    private FloatingActionButton btn;
    private Button predB;
    private TextView atelectasis;
    private ImageView imageview;
    private TextView resText;
    private TextView linkText;
    private LinearLayout linearlayout1,newLay;


    private CardView cameraIcon,photoIcon;

    final int SELECT_IMAGE = 1;

    private static final int IMG_SIZE = 64;
    private static final int IMAGE_MEAN = 0;
    private static final float IMAGE_STD = 255;

    private List<Classifier> mClassifiers = new ArrayList<>();

    Bitmap bitmap;
    private Uri fileUri, imagepath;

    private static final String IMAGE_DIRECTORY = "/ClassifyX";
    private int GALLERY = 1, CAMERA = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    String text =  "";
    String patientid =  "";

    private Button logout, history;

    private Helpers helpers;

    private FirebaseStorage storage;

    private StorageReference storageReference;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cardview_shadow_start_color)));

        helpers = new Helpers(this);

        patientid = getIntent().getStringExtra("patientid");

        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference();

        dialog = new ProgressDialog(this);

        logout= findViewById(R.id.logout);
        history= findViewById(R.id.history);
        predB= findViewById(R.id.predictBtn);
        atelectasis= findViewById(R.id.linkAt);
        imageview=findViewById(R.id.iv);
        resText=findViewById(R.id.takenPic);

        linearlayout1= (LinearLayout) findViewById(R.id.ImageLay);
        newLay= (LinearLayout) findViewById(R.id.newLay);

        cameraIcon= findViewById(R.id.camera_icon);
        photoIcon= findViewById(R.id.gallery_icon);
        loadClassifier();
        //openGallery();

        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaptureImageActivity.this, HistoryActivity.class));
            }
        });

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePhotoFromCamera();
            }
        });

        photoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choosePhotoFromGallery();
            }
        });


        predictButton();
        logout();


    }
    //function of logout button
    private void logout() {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(
                        CaptureImageActivity.this
                );
                builder.setIcon(R.drawable.ic_check);
                builder.setTitle("LOGOUT?");
                builder.setMessage("Are you sure you want to logout?");

                builder.setNegativeButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        helpers.clearStore();
                        dialog.cancel();
                        Intent movetohomepage = new Intent(CaptureImageActivity.this, category.class);
                        movetohomepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(movetohomepage);
                    }
                });

                builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                final AlertDialog alertDialog=builder.create();
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                .setTextColor(getResources().getColor(R.color.red));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                });

                alertDialog.show();
            }
        });
    }

    private void predictButton() {

        predB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            println("hi");
                loadModel(bitmap);

            }
        });
    }

    private void openGallery() {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPictureDialog();
            }
        });

    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select Photo",
                "Take Photo" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //choosePhotoFromGallery();
                                break;
                            case 1:
                                //takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA);
    }

    public Uri getOutputMediaFileUri(int type) {
        Uri photoURI = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                getOutputMediaFile(type));
        return photoURI;
        // return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY, "Oops! Failed to create "
                        + IMAGE_DIRECTORY + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
    //calling the image selector
    private void choosePhotoFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    imagepath = data.getData();

                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(getApplicationContext()
                                        .getContentResolver(), data.getData());

                        linearlayout1.setVisibility(View.VISIBLE);
                        newLay.setVisibility(View.GONE);
                        imageview.setImageBitmap(bitmap);

//                        loadModel(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (resultCode == Activity.RESULT_CANCELED)  {
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }

        else if (requestCode == CAMERA) {
            imagepath = data.getData();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            //final Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                //LinearLayout lL1 = findViewById(R.id.first);
                linearlayout1.setVisibility(View.VISIBLE);
                newLay.setVisibility(View.GONE);
                imageview.setImageBitmap(bitmap);
                /*predB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resText.setText("");
                        loadModel(bitmap);
                    }
                });*/
                //imageview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadClassifier(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {mClassifiers.add(
                        TensorFlowClassifier.create(getAssets(),
                                "xray.pb", "labels.txt", IMG_SIZE,
                                "conv2d_1_input", "dense_2/Sigmoid"));
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing classifiers!", e);
                }
            }
        }).start();
    }
    // infers the image to the classification model for processing and
    private void loadModel(Bitmap bitmap) {

        if(bitmap.getWidth() != IMG_SIZE || bitmap.getHeight() != IMG_SIZE)   bitmap = Bitmap.createScaledBitmap(bitmap, IMG_SIZE, IMG_SIZE, false);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        float[] retPixels = new float[width * height * 3];

        for (int i = 0; i < pixels.length; i++) {
            final int val = pixels[i];
            retPixels[i * 3] = (((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            retPixels[i * 3 + 1] = (((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            retPixels[i * 3 + 2] = ((val & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
        }
//The code that performs the classification
        for (Classifier classifier : mClassifiers) {
            // compares image pixels of selected model to image pixels of trained models
            //if theres a match it gets the label of the image or the name of the condition/disease
            final Classification res = classifier.recognize(retPixels);

            //if it cant find a name, output no prediction
            if (res.getLabel() == null) {
                text += "No Prediction\n";
            } else {
                //else output its name to the variable called text, and call the text
                text += String.format(Locale.US, "%s", res.getLabel());
            }
        }
        resText.setText(text);
        //check if it is any of the diseases and output the right related link
//        predB.setClickable(false);
        findViewById(R.id.predictBtn).setVisibility(View.GONE);
//        predB.setVisibility(View.GONE);

        //save the image to the database
        dialog.setMessage("Saving diagnosis. Please Wait");
        dialog.show();
        new SaveImageToDatabase(imagepath, patientid, text).execute();

        findViewById(R.id.hyperlinktext).setVisibility(View.VISIBLE);
        findViewById(R.id.hyperlinktext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moreinfointent = new Intent(CaptureImageActivity.this, MoreinfoActivity.class);
                moreinfointent.putExtra( "diseasename", text);
                startActivity(moreinfointent);
            }
        });
    }


    private class SaveImageToDatabase extends AsyncTask<String, Void, Void>{

        Uri imageuri;
        String patientid, diagnosis;

        public SaveImageToDatabase(Uri imageuri, String patientid, String diagnosis) {
            this.imageuri = imageuri;
            this.patientid = patientid;
            this.diagnosis = diagnosis;
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (imageuri != null) {
                StorageReference reference = storageReference.child("images/patientdiagnosis/" + patientid);

                //get data from imageview in bytes
                imageview.setDrawingCacheEnabled(true);
                imageview.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();

                reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null){
                            if (taskSnapshot.getMetadata().getReference() != null){
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageurl = uri.toString();
                                        //save image url to database here
                                        //also save diagnosis here
                                        UploadDiagnosisToDatabase(imageurl, patientid, text);
                                    }
                                });
                            }
                        }
                    }
                });
            }
            return null;
        }
    }

    private void UploadDiagnosisToDatabase(String imageurl, String patientid, String text) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("diagnosis").child(patientid).child("images");
        String imageid = reference.push().getKey();
        Map<String, Object> diagnosis = new HashMap<>();
        diagnosis.put("imageurl", imageurl);
        diagnosis.put("diagnosis", text);
        diagnosis.put("timestamp", new Helpers(this).timestamp());
        reference.child(imageid).updateChildren(diagnosis).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               dialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Diagnosis successfully saved",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent movetophyscian = new Intent(CaptureImageActivity.this, physicianhomepage.class);
        movetophyscian.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(movetophyscian);
    }

}