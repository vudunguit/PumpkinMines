package com.aga.mine.pumpkin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.Session;
import com.facebook.model.GraphUser;

public class Directory extends Activity implements Serializable{
	
	private GraphUser userInfo = null;
	private Bitmap userPhoto = null;
	private List<GraphUser> friendsInfo = new ArrayList<GraphUser>();
	
	private boolean login = false;
	
	private Session faceSession = null;
	private String accessToken = "";
	
	Context context = this;
	
	private static Directory directory;

	public static synchronized Directory getinstance() {
		if (directory == null)
			directory = new Directory();
		return directory;
	}
	
	private Directory() {
	}
	
//	private void fileRead() throws IOException, ClassNotFoundException {
//		FileInputStream fis = context.openFileInput("directory.data");
//		// Reading by byte.
//		byte[] buffer = new byte[20];
//		fis.read(buffer);
//		fis.close();
//
//		// Reading by object. ( class Directory implements Serializable )
//		ObjectInputStream ois = new ObjectInputStream(fis);
//		directory = (Directory) ois.readObject();
//		ois.close();
//	}
//	
//	private void filewrite() throws IOException {
//		FileOutputStream fos = context.openFileOutput("directory.data", Context.MODE_PRIVATE);
//		// Writing by byte.
//		String string = "hello world!";
//		fos.write(string.getBytes());
//		fos.close();
//
//		// Writing by object.
//		ObjectOutputStream oos = new ObjectOutputStream(fos);
//		oos.writeObject(directory); 
//		oos.close();
//	}
}