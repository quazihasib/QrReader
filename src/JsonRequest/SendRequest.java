package JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.fastgive.CaptureActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class SendRequest extends AsyncTask<String, String, String>{

	/**
	* Before starting background thread show progress dialog
	*/
	public ProgressDialog pDialog;
	String jsonResponceStringStatus = null;
	public String personFor, personBy, personNumber;
	@Override
	protected void onPreExecute(){
	    super.onPreExecute();
	    pDialog = new ProgressDialog(CaptureActivity.captureInstance);
	    pDialog.setIndeterminate(false);
	    pDialog.setCancelable(false);
	    pDialog.setMessage("Please wait...");
	    pDialog.show();
	}

	/*
	* Creating user
	*/
	@Override
	protected String doInBackground(String... args)
	{
	    // getting JSON object
	    // Note that create user url accepts POST method
		JSONParser j = new JSONParser();
		JSONObject jObj = j.getJSONFromUrl(args[0]);
		
		try {
			jsonResponceStringStatus = jObj.getString("status");
            Log.d("SendRequest", "jsonResponceStringStatus:"+jsonResponceStringStatus);
            
			if(jsonResponceStringStatus.equals("This ticket has already been used once."))
			{
				Log.d("JSONParser", "status");
			}
			else 
			{
				Log.d("JSONParser", "status111");
				personFor = jObj.getString("for");
				personBy = jObj.getString("by");
				personNumber = jObj.getString("number");
				
				Log.d("SendRequest", "personFor:"+personFor);
				Log.d("SendRequest", "personBy:"+personBy);
				Log.d("SendRequest", "personNumber:"+personNumber);

			}
        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Log.d("SendRequest", "jsonResponceString:"+jsonResponceString);
//		CaptureActivity.contentsTextView.setText(""+jsonResponceString);
	    return jsonResponceStringStatus;    
	}

	    /**
	     * After completing background task Dismiss the progress dialog
	     * **/
	    @Override
	    protected void onPostExecute(String file_url) {
	        // dismiss the dialog once done
	        pDialog.dismiss();
	        
	        if(jsonResponceStringStatus.equals("found"))
	        {
	        	String a = "Thanks for purchasing this ticket\n"+"Ticket Number:"+personNumber+"\n"
	        			+"Patron Name:"+ personBy+"\n"+"Ticket Bearer:"+personFor;
	        	CaptureActivity.setData(a);
	        }
	        else
	        {
	        	CaptureActivity.setData(jsonResponceStringStatus);
//	   	        CaptureActivity.contentsTextView.setText(""+jsonResponceString);
	        }
	    }
	}