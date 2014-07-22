package com.orphanages.activities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

import com.orphanages.R;
import com.orphanages.objects.JSONParser;
import com.orphanages.objects.Public;

public class Register extends Activity implements OnClickListener
{

	String imagePath = null;
	String upLoadServerUri = "http://www.wedroiders.com/orphanages/upload.php";
	private int serverResponseCode = 0;
	Button register;
	EditText username;
	EditText fullname;
	EditText password;
	EditText confirmPassword;
	EditText mail;
	Spinner city;
	ImageButton profilePic;

	ProgressDialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		register = (Button)findViewById(R.id.signup);
		profilePic = (ImageButton)findViewById(R.id.imageButton1);
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		confirmPassword = (EditText)findViewById(R.id.confirmPassword);
		mail = (EditText)findViewById(R.id.mail);
		fullname = (EditText)findViewById(R.id.fullname);
		city = (Spinner)findViewById(R.id.spinner1);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.cities));
	    city.setAdapter(adapter);
	    
		profilePic.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	
	 @Override
	    public void onClick(View arg0) {
	        if(arg0==profilePic)
	        {
	            Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
	        }
	        else if (arg0==register) {
	             
	        	if(username.getText().toString().isEmpty())
	        		username.setError("username can't be empty");
	        	if(password.getText().toString().isEmpty())
	        		username.setError("password can't be empty");
	        	if(confirmPassword.getText().toString().isEmpty())
	        		confirmPassword.setError("please confirm your password");
	        	if(mail.getText().toString().isEmpty())
	        		mail.setError("mail can't be empty");
	        	if(password.getText().toString().compareTo(confirmPassword.getText().toString())== 1)
	        		confirmPassword.setError("password mismatch");
	        	else
	        	{
	             dialog = ProgressDialog.show(Register.this, "", "Registering...", true);
 	             new Thread(new Runnable() {
	                 public void run() {
	                	 List<NameValuePair> params = new ArrayList<NameValuePair>();

	     				params.add(new BasicNameValuePair("tag", "register"));
	     				params.add(new BasicNameValuePair("username", username
	     						.getText().toString()));
	     				params.add(new BasicNameValuePair("fullname", fullname
	     						.getText().toString()));
	     				params.add(new BasicNameValuePair("mail", mail
	     						.getText().toString()));
	     				params.add(new BasicNameValuePair("city", city.getSelectedItem().toString()));
	     				params.add(new BasicNameValuePair("password", (password
	     						.getText().toString())));
	     				JSONObject json = new JSONParser().getJSONFromUrl(Public.url
	     						+ "register.php", params);
	     				try
						{
							if(json.getString("response").equals("done"))
							{
								Public.name = username.getText().toString();
								uploadFile(imagePath);
								runOnUiThread(new Runnable()
								{
									
									@Override
									public void run()
									{
										// TODO Auto-generated method stub
										startActivity(new Intent(Register.this, Dashboard.class));
										finish();
									}
								});
							}
							else
							{
								dialog.dismiss();
								runOnUiThread(new Runnable()
								{
									
									@Override
									public void run()
									{
										// TODO Auto-generated method stub
										username.setError("username taken");
									}
								});
							}
						} catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                                               
	                 }
	               }).start();     
	        }
	        }
	         
	    } 
	     
	 
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath(); 
           
            Uri selectedImageUri = data.getData();
            imagePath = getPath(selectedImageUri);
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            Bitmap bmResized=getResizedBitmap(bitmap,200,200);
            profilePic.setImageBitmap(bmResized);
            profilePic.setScaleType(ScaleType.FIT_XY);
            
        }
    }
         public String getPath(Uri uri) {
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
         
    public int uploadFile(String sourceFileUri) {
           
           
          String fileName = sourceFileUri;
  
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
           
          if (!sourceFile.isFile()) {
               
               Log.e("uploadFile", "Source File not exist :"+imagePath);
                dialog.dismiss();
               runOnUiThread(new Runnable() {
                   public void run() {
                       Toast.makeText(Register.this,"Source File not exist :"+ imagePath,Toast.LENGTH_SHORT).show();;
                   }
               }); 
               return 0;
            
          }
          else
          {
               try { 
                    
                     // open a URL connection to the Servlet
                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
                   URL url = new URL(upLoadServerUri);
                    
                   // Open a HTTP  connection to  the URL
                   conn = (HttpURLConnection) url.openConnection(); 
                   conn.setDoInput(true); // Allow Inputs
                   conn.setDoOutput(true); // Allow Outputs
                   conn.setUseCaches(false); // Don't use a Cached Copy
                   conn.setRequestMethod("POST");
                   conn.setRequestProperty("Connection", "Keep-Alive");
                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                   conn.setRequestProperty("uploadedfile", Public.name+".jpg"); 
                    
                   dos = new DataOutputStream(conn.getOutputStream());
          
                   dos.writeBytes(twoHyphens + boundary + lineEnd); 
                   dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                                             + Public.name+".jpg" + "\"" + lineEnd);
                    
                   dos.writeBytes(lineEnd);
          
                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available(); 
          
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];
          
                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                      
                   while (bytesRead > 0) {
                        
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                      
                    }
          
                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
          
                   // Responses from the server (code and message)
                   serverResponseCode = conn.getResponseCode();
                   String serverResponseMessage = conn.getResponseMessage();
                     
                   Log.i("uploadFile", "HTTP Response is : "
                           + serverResponseMessage + ": " + serverResponseCode);
                    
                   if(serverResponseCode == 200){
                        
                       runOnUiThread(new Runnable() {
                            public void run() {
                                //Toast.makeText(Register.this, "Image Upload Complete.", Toast.LENGTH_SHORT).show();
                              
                            }
                        });                
                   }    
                   //close the streams //
                   fileInputStream.close();
                   dos.flush();
                   dos.close();
                     
              } catch (MalformedURLException ex) {
                   
                   dialog.dismiss();
                  runOnUiThread(new Runnable() {
                      public void run() {
                          // Toast.makeText(Register.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                      }
                  });
                   
                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
              } catch (Exception e) {
                  dialog.dismiss();
                   e.printStackTrace();
                  runOnUiThread(new Runnable() {
                      public void run() {
                          // Toast.makeText(Register.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                      }
                  });
                  Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
              }
               dialog.dismiss();
               return serverResponseCode; 
               
           } // End else block 
         }
 
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;

    }
}
