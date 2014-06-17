package com.aga.mine.pumpkin;

import java.io.InputStream;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

//    private ProgressDialog mDialog;

    public DownloadImageTask() {
    }

//    protected void onPreExecute() {
//        mDialog = ProgressDialog.show(MainApplication.getInstance().getActivity(),"Please wait...", "Retrieving data ...", true);
//    }
    
    private Bitmap inputImage(String... urls){
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", "image download error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
		return bitmap;
    }
    
	@Override
    protected Bitmap doInBackground(String... urls) {
		Bitmap bitmap =  inputImage(urls);
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
//        mDialog.dismiss();
		super.onPostExecute(result);
    }
}