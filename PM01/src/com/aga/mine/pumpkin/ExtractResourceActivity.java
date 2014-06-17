package com.aga.mine.pumpkin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.android.vending.expansion.zipfile.ZipResourceFile.ZipEntryRO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ExtractResourceActivity extends Activity{

	private static final String OBB_PATH = "/Android/obb/";
	private static final String FOLDER_EXTRACT = "/com.aga.mine.pumpkin.resources";
	
	private ProgressDialog mDialog;
	private static final String TAG = ExtractResourceActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		File extractPath = new File(Environment.getExternalStorageDirectory() + OBB_PATH + getApplicationContext().getPackageName() + FOLDER_EXTRACT);
		if(extractPath.exists()){
			startActivity(new Intent(ExtractResourceActivity.this, MainActivity.class));
		}else{
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			setContentView(R.layout.main);
			new ExtractResourceTask().execute();
		}
		
		
		
	}

	private class ExtractResourceTask extends AsyncTask<Void, Void, Void>{

		String packageName = getApplicationContext().getPackageName();
		
        File root = Environment.getExternalStorageDirectory();
        File expPath = new File(root.toString() + OBB_PATH + packageName);
        
        private void showDialog(String title, String message) {
            mDialog = ProgressDialog.show(ExtractResourceActivity.this, title, message, true);
        }
        
        private void hideDialog() {
            if (mDialog != null) {
                mDialog.hide();
            }
        }
        
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog("Please wait", "Extracting resources ...");
//			WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//			lp.dimAmount = 0.0f;
//			mDialog.getWindow().setAttributes(lp);
//			mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			startActivity(new Intent(ExtractResourceActivity.this, MainActivity.class));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			extract();
			return null;
		}
		
		private void extract(){
	        if (expPath.exists()) {
	            String strMainPath = null;
	            try {
	                strMainPath = expPath + File.separator + "main."
	                    + getApplicationContext().getPackageManager().getPackageInfo(
	                                packageName, 0).versionCode + "."
	                        + packageName + ".obb";

	                ZipResourceFile expandsionFile = new ZipResourceFile(strMainPath);
	                ZipEntryRO[] zip = expandsionFile.getAllEntries();
	                Log.e("", "zip[0].isUncompressed() : " + zip[0].isUncompressed());
	                Log.e("",
	                        "mFile.getAbsolutePath() : "
	                                + zip[0].mFile.getAbsolutePath());
	                Log.e("", "mFileName : " + zip[0].mFileName);
	                Log.e("", "mZipFileName : " + zip[0].mZipFileName);
	                Log.e("", "mCompressedLength : " + zip[0].mCompressedLength);
	                
//	                File file = new File(Environment.getExternalStorageDirectory() + EXTRACT_PATH);
//	                ZipHelper.unzip(zip[0].mFileName, file);
//	                if(file.exists()){
//	                	Log.e("", "unzipped : " + file.getAbsolutePath());
//	                }
	                String pathToExtract = expPath + FOLDER_EXTRACT;
	                Log.e(TAG, pathToExtract);
	                extractZip(strMainPath, pathToExtract);
	           
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	        }
		}
		
		private void extractZip(String pathOfZip,String pathToExtract)
		 {


		        int BUFFER_SIZE = 4096;//1024
		        int size;
		        byte[] buffer = new byte[BUFFER_SIZE];


		        try {
		            File f = new File(pathToExtract);
		            if(!f.exists()){
		            	if(!f.mkdirs()){
		            		Log.e(TAG, "Problem creating image folder");
		            	}
		            }
//		            if(!f.isDirectory()) {
//		                f.mkdirs();
//		            }
		            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(pathOfZip), BUFFER_SIZE));
		            try {
		                ZipEntry ze = null;
		                while ((ze = zin.getNextEntry()) != null) {
		                    String path = pathToExtract  +"/"+ ze.getName();

		                    if (ze.isDirectory()) {
		                        File unzipFile = new File(path);
		                        if(!unzipFile.isDirectory()) {
		                            unzipFile.mkdirs();
		                        }
		                    }
		                    else {
		                        FileOutputStream out = new FileOutputStream(path, false);
		                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
		                        try {
		                            while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
		                                fout.write(buffer, 0, size);
		                            }

		                            zin.closeEntry();
		                        }catch (Exception e) {
		                            Log.e("Exception", "Unzip exception 1:" + e.toString());
		                        }
		                        finally {
		                            fout.flush();
		                            fout.close();
		                        }
		                    }
		                }
		            }catch (Exception e) {
		                Log.e("Exception", "Unzip exception2 :" + e.toString());
		            }
		            finally {
		                zin.close();
		            }
//		            return true;
		        }
		        catch (Exception e) {
		            Log.e("Exception", "Unzip exception :" + e.toString());
		        }
//		        return false;
		    }
		
	}
	
}
