package com.orphanages.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orphanages.R;
import com.orphanages.objects.JSONParser;
import com.orphanages.objects.Public;

public class Login extends Activity implements OnClickListener
{

	EditText username;
	EditText password;
	Button login;
	Button signup;
	Button forgotPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.signup);
		signup = (Button) findViewById(R.id.button2);
		forgotPassword = (Button) findViewById(R.id.Button1);

		SpannableString content = new SpannableString("Sign Up");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		signup.setText(content);

		SpannableString content2 = new SpannableString("Forgot Password?");
		content2.setSpan(new UnderlineSpan(),0,content2.length(), 0);
		forgotPassword.setText(content2);

		login.setOnClickListener(this);
		signup.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if (v == login)
		{
			String username = this.username.getText().toString();
			String password = this.password.getText().toString();

			if (username.isEmpty())
			{
				this.username.setError("username could'nt be empty");
			} else if (password.isEmpty())
			{
				this.password.setError("password could'nt be empty");

			} else
			{
				new LoginTask().execute();
			}
		} else if (v == signup)
		{
			startActivity(new Intent(Login.this, Register.class));
			finish();
		} else if (v == forgotPassword)
		{
			forgotPassword();
		}
	}

	public void forgotPassword()
	{
		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		View promptsView = li.inflate(R.layout.edittext, null);

		final EditText mail = (EditText) promptsView.findViewById(R.id.username);

		AlertDialog mailAlert = new AlertDialog.Builder(this)
				.setView(promptsView)
				.setTitle("Your e-mail address").setIcon(R.drawable.attach_mail_pink)
				.setPositiveButton("Send mail",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog ,
									int which)
							{
								if(!mail.getText().toString().isEmpty())
								{
									new Thread(new Runnable()
									{
										
										@Override
										public void run()
										{
											// TODO Auto-generated method stub
											List<NameValuePair> params = new ArrayList<NameValuePair>();
											params.add(new BasicNameValuePair("tag", "forgotPassword"));
											params.add(new BasicNameValuePair("mail", mail.getText().toString()));
											 new JSONParser().getJSONFromUrl(Public.url
													+ "forgotPassword.php", params);
										}
									}).start();
								}
								else
									mail.setError("Email address cannot be empty");
							}
						}).create();
		mailAlert.show();
	}

	private class LoginTask extends AsyncTask<Void, String, String>
	{

		ProgressDialog d;

		@Override
		protected void onPreExecute()
		{
			d = ProgressDialog.show(Login.this, "", "Please Wait ..");
		}

		@Override
		protected String doInBackground(Void ... args)
		{
			try
			{
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("tag", "login"));
				params.add(new BasicNameValuePair("username", username
						.getText().toString()));
				params.add(new BasicNameValuePair("password", (password
						.getText().toString())));
				JSONObject json = new JSONParser().getJSONFromUrl(Public.url
						+ "login.php", params);
				return json.getString("response");
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(String response)
		{
			d.dismiss();
			
			if (response.equalsIgnoreCase("1"))
			{
				Public.name = username.getText().toString();
				Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(Login.this, Dashboard.class));
				finish();
			} else
			{
				username.setError("username not valid");
				password.setError("password not valid");
			}

		}
	}
}
