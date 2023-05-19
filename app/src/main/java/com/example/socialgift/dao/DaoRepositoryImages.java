package com.example.socialgift.dao;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class DaoRepositoryImages {
    private final String  url = "https://balandrau.salle.url.edu/i3/repositoryimages/uploadfile";

    public DaoRepositoryImages() {
    }
    public void uploadFile(File image) {
        Log.wtf("IMAGE NAME", image.getName());
        Log.wtf("IMAGE PATH", image.getAbsolutePath());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("myFile", image.getName(),
                                RequestBody.create(image.getAbsolutePath(), MediaType.parse("application/octet-stream")))
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .method("POST", body)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    Log.wtf("RESPONSE", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
