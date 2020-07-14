package com.example.sanat.dressexchangev03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

public class NewDress extends AppCompatActivity {

    //AddDress
    private ImageView mainImage = null;
    private ImageView image2 = null;
    private ImageView image3 = null;
    private ImageView image4 = null;

    private String loginID= null;

    private Boolean submitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dress);

        submitted = false;

        //Remove ActionBar
        getSupportActionBar().hide();

        Bundle b = getIntent().getExtras();
        if(b != null)
            loginID = b.getString("login");

        add();
    }

    private void add() {
        mainImage = findViewById(R.id.mainImage);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);

        mainImage.setClickable(true);
        image2.setClickable(true);
        image3.setClickable(true);
        image4.setClickable(true);

        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("image", "clicked");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("image", "clicked");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("image", "clicked");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("image", "clicked");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scrollCards();
                finish();
            }
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!submitted) {
                    try {
                        submitted = true;

                        EditText desc = (EditText) findViewById(R.id.title);
                        EditText sizeText = (EditText) findViewById(R.id.size);
                        EditText condition = findViewById(R.id.condition);

                        String description = desc.getText().toString();
                        String size = sizeText.getText().toString();
                        String cond = condition.getText().toString();

                        ImageView mainImage = (ImageView) findViewById(R.id.mainImage);

                        Bitmap bitmap = ((BitmapDrawable) mainImage.getDrawable()).getBitmap();
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream); // Convert bitmap to byteoutputstream
                        String encodedImage = Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);

                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image2 = findViewById(R.id.image2);
                        image2.setImageBitmap(decodedByte);
                        String response = new ServerComm().execute("add", loginID, size, description, cond).get();
                        if (response.equals("1")) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Dress successfully uploaded!", Snackbar.LENGTH_LONG);

                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    //see Snackbar.Callback docs for event details
                                    finish();
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {

                                }
                            });
                            snackbar.show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
