package com.example.money_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tryText;
    TextView usdText;
    TextView cardText;
    TextView jpyText;
    TextView chfText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryText = findViewById(R.id.tryText);
        usdText = findViewById(R.id.usdText);
        cardText = findViewById(R.id.cardText);
        jpyText = findViewById(R.id.jpyText);
        chfText = findViewById(R.id.chfText);
    }

    public void getRates(View view) {
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://data.fixer.io/api/latest?access_key=7ffd7f67981e0f5e4df795da2edb3ff3&format=1");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = inputStreamReader.read();
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String base = jsonObject.getString("base");
                JSONObject rates = jsonObject.getJSONObject("rates");

                String turkishLira = rates.getString("TRY");
                tryText.setText("TRY: " + turkishLira);

                String usd = rates.getString("USD");
                usdText.setText("USD: " + usd);

                String cad = rates.getString("CAD");
                cardText.setText("CAD: " + cad);

                String chf = rates.getString("CHF");
                chfText.setText("CHF: " + chf);

                String jpy = rates.getString("JPY");
                jpyText.setText("JPY: " + jpy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
