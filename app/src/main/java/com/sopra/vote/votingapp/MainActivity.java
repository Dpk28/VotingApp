package com.sopra.vote.votingapp;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sopra.vote.votingapp.R.id.textView;

public class MainActivity extends AppCompatActivity {

    private Button submit;
    private Button result;
    private RadioGroup options;
    final private String URL = "https://c2f5087bd8ec424db92ff564fcf3b374-vp0.us.blockchain.ibm.com:5004/chaincode";
    private String body = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button) findViewById(R.id.button);
        result = (Button) findViewById(R.id.button2);
        options = (RadioGroup) findViewById(R.id.radioGroup);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int radioButtonID = options.getCheckedRadioButtonId();
                String opt;
                if(radioButtonID==0)
                    opt = "pizza";
                else
                    opt = "icecream";
                body = "{\n" +
                        "  'jsonrpc': '2.0',\n" +
                        "  'method': 'invoke',\n" +
                        "  'params': {\n" +
                        "    'type': 1,\n" +
                        "    'chaincodeID': {\n" +
                        "      'name': '580e824e83568470a6d8aa19119e8d5736ff15ba51eaeca3bf3f200b8596e4af0970b164319fffee43824a2ef8a40bcf105226d42a0a645667941bb17d5dbe93'\n" +
                        "    },\n" +
                        "    'ctorMsg': {\n" +
                        "      'function': 'string',\n" +
                        "      'args': [\n" +
                        "        '" + opt + "'\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    'secureContext': 'user_type1_0'\n" +
                        "  },\n" +
                        "  'id': 0\n" +
                        "}";
                CastVote task = new CastVote();
                task.execute(new String[]{URL, body});
            }
        });

    }
    class CastVote extends AsyncTask<String, Void, String> {
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected String doInBackground(String... url) {
            // we use the OkHttp library from https://github.com/square/okhttp
            try {
                RequestBody body = RequestBody.create(JSON, url[1]);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url[0])
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (Exception e) {
                return "failed";
            }
            return "failed";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, result,
                    Toast.LENGTH_LONG).show();
        }

    }

}


