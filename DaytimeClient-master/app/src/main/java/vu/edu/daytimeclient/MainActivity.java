package vu.edu.daytimeclient;

import android.graphics.Typeface;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate was invoked");

        setContentView(R.layout.activity_main);

        final TextView time = (TextView)findViewById(R.id.time);


        final Button button1 = (Button)findViewById(R.id.button1);


        final EditText timezone = (EditText)findViewById(R.id.timezone);



        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String timezone_text = timezone.getText().toString();

                AsyncTask<Void, String, String> task = new AsyncTask<Void, String, String>() {

                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            Socket socket = new Socket("csx283.hopto.org", 1313);
                            Log.i(TAG, "doInbackgroud was invoked");

                            InputStream in = socket.getInputStream();
                            OutputStream out = socket.getOutputStream();

                            StringBuilder sb = new StringBuilder();

                            int j;
                            j = in.read();
                            sb.append((char) j);
                            while(j != '\n'){
                                j = in.read();
                                sb.append((char) j);
                                Log.i(TAG, "if is invoked");
                            }
                            publishProgress(sb.toString());
                            byte[] timezonebyte = timezone_text.getBytes();
                            //String timezone_default = "CST\n";
                            Boolean flag = false;
                            if(timezonebyte.length >= 3){
                                String timezone_show = timezone_text + '\n';
                                timezonebyte = timezone_show.getBytes();
                                out.write(timezonebyte);
                                flag = true;
                            }


                            int oldi;
                            int i = 0;
                            while (flag) {
                                oldi = i;
                                i = in.read();
                                sb.append((char) i);
                                if ((oldi == '\n') && (i == '\n')) {
                                    publishProgress(sb.toString());
                                    //Log.i(TAG, "Received text:" + sb.toString());
                                    sb.setLength(0);
                                }
                            }

                            //out.close();
                            return "Done!";

                        } catch (UnknownHostException e) {
                            String error = "Unknown host: " + e.getMessage();
                            Log.e(TAG, error);
                            e.printStackTrace();
                            return error;
                        } catch (IOException e) {
                            String error = "I/O error communicating with server: " + e.getMessage();
                            Log.e(TAG, error);
                            e.printStackTrace();
                            return error;
                        }
                    }

                    // this is called when publishProgress is invoked from withing doInBackground
                    @Override
                    protected void onProgressUpdate (String... s) {

                        time.setTypeface(Typeface.MONOSPACE);
                        time.setTextSize(8);
                        time.setText(s[0]);

                        Log.i(TAG, timezone.getText().toString());
                        Log.i(TAG, "Received text:" + s[0]);
                    }
                };


                task.execute();
            }
        });
    }
}
