/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.fastgive;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.jwetherell.quick_response_code.result.ResultHandler;
import com.jwetherell.quick_response_code.result.ResultHandlerFactory;

import JsonRequest.JSONParser;
import JsonRequest.SendRequest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Example Capture Activity.
 * 
 * @author Justin Wetherell (phishman3579@gmail.com)
 */
public class CaptureActivity extends DecoderActivity {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet.of(ResultMetadataType.ISSUE_NUMBER, ResultMetadataType.SUGGESTED_PRICE,
            ResultMetadataType.ERROR_CORRECTION_LEVEL, ResultMetadataType.POSSIBLE_COUNTRY);

//    private TextView statusView = null;
    private View resultView = null;
    private boolean inScanMode = false;
    public static CaptureActivity captureInstance;
    public static TextView contentsTextView;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle); 
        setContentView(R.layout.capture);
        Log.v(TAG, "onCreate()");

        captureInstance = this;
        
        resultView = findViewById(R.id.result_view);
//      statusView = (TextView) findViewById(R.id.status_view);

        inScanMode = false;
    }
    
    	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inScanMode)
                finish();
            else
                onResume();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode) {
        drawResultPoints(barcode, rawResult);

        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        handleDecodeInternally(rawResult, resultHandler, barcode);
    }

    protected void showScanner() {
        inScanMode = true;
        resultView.setVisibility(View.GONE);
//        statusView.setText(R.string.msg_default_status);
//        statusView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    protected void showResults() {
        inScanMode = false;
//        statusView.setVisibility(View.GONE);
        viewfinderView.setVisibility(View.GONE);
        resultView.setVisibility(View.VISIBLE);
    }

    // Put up our own UI for how to handle the decodBarcodeFormated contents.
    private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        onPause();
        showResults();

//        ImageView barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);
//        if (barcode == null) {
//            barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon));
//        } else {
//            barcodeImageView.setImageBitmap(barcode);
//        }
//
//        TextView formatTextView = (TextView) findViewById(R.id.format_text_view);
//        formatTextView.setText(rawResult.getBarcodeFormat().toString());
//
//        TextView typeTextView = (TextView) findViewById(R.id.type_text_view);
//        typeTextView.setText(resultHandler.getType().toString());
//
//        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
//        String formattedTime = formatter.format(new Date(rawResult.getTimestamp()));
//        TextView timeTextView = (TextView) findViewById(R.id.time_text_view);
//        timeTextView.setText(formattedTime);
//
//        TextView metaTextView = (TextView) findViewById(R.id.meta_text_view);
//        View metaTextViewLabel = findViewById(R.id.meta_text_view_label);
//        metaTextView.setVisibility(View.GONE);
//        metaTextViewLabel.setVisibility(View.GONE);
//        Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
//        if (metadata != null) {
//            StringBuilder metadataText = new StringBuilder(20);
//            for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet()) {
//                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
//                    metadataText.append(entry.getValue()).append('\n');
//                }
//            }
//            if (metadataText.length() > 0) {
//                metadataText.setLength(metadataText.length() - 1);
//                metaTextView.setText(metadataText);
//                metaTextView.setVisibility(View.VISIBLE);
//                metaTextViewLabel.setVisibility(View.VISIBLE);
//            }
//        }

        
        //Qr Reader
        contentsTextView = (TextView) findViewById(R.id.contents_text_view);
        CharSequence displayContents = resultHandler.getDisplayContents();
//        contentsTextView.setText(displayContents);
        Log.d(TAG, "displayContents:"+displayContents);
        
        String resultString = ""+displayContents;
        Log.d(TAG, "resultString:"+resultString);
        if(resultString.length()>24 && resultString.length()<35 )
        {
        	 Log.d(TAG, "resultString is 34 characters");
        	 
        	 try
        	 {
        		 String resultStringresultString = resultString.substring(24, 34);
        		 Log.d(TAG, "resultString now:"+resultStringresultString);
        	 
        		 String sampleString = "JM208OB791AN578TE662VI34"; 
        		 Log.d(TAG, "First 24 characters:"+resultString.substring(0, 24));
        		 if(resultString.substring(0, 24).toLowerCase().contains(sampleString.toLowerCase()))
        		 {
        			 Log.d(TAG, "YES!!!");
        			 contentsTextView.setText("Thanks for purchasing this thicket");
             	
        			 if(JSONParser.isNetworkAvailable() == true)
        			 {
        				 AsyncTask<String, String, String> responce = new SendRequest().execute("http://117.58.246.154/fastgive/fastgive-website/event/check_ticket/"+resultStringresultString);
//        				 Log.d(TAG, "Final Result:"+responce.toString());
//        	       		 contentsTextView.setText(""+responce);
        			 }
             		 else
             		 {
              			 Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
             	   	 }
             	 }
             	 else
             	 {
             		 Log.d(TAG, "NO");
             		 contentsTextView.setText("This does not seem to be the genuine ticket. \nPlease try again.");
             	 }
        	 }
        	 catch(Exception e) 
        	 {
         		 contentsTextView.setText("This does not seem to be the genuine ticket. \nPlease try again.");
        	 }
        }
        else
        {
        	 Log.d(TAG, "resultString is more than 34 characters");
        	 contentsTextView.setText("It seems that this code was not genarated by Fastgive.");
        }
       
        // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
        int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
        contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);
    }
    
    
    public static void setData(String some)
    {
    	contentsTextView.setText(""+some);
    }
}
