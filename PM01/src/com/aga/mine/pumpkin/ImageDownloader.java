package com.aga.mine.pumpkin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.cocos2d.nodes.CCSprite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloader extends AsyncTask<Void, Integer, Void> {
	
	private String url;
	private ImageLoaderListener listener;
	private Bitmap bmp;

	public ImageDownloader(String url, ImageLoaderListener listener) {
		this.url = url;
		this.listener = listener;
	}

	public interface ImageLoaderListener {
		void onImageDownloaded(CCSprite profile);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		bmp = getBitmapFromURL(url);
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		if (listener != null) {
			listener.onImageDownloaded(CCSprite.sprite(bmp));
		}
		super.onPostExecute(result);
	}

	public Bitmap getBitmapFromURL(String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}
	}

}