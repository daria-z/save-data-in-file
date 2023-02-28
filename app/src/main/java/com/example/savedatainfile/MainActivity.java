package com.example.savedatainfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savedatainfile.CurrencyAdapter;
import com.example.savedatainfile.CurrencyXmlParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private final static String FILENAME = "content.txt";
    String uri = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    String content = "";
    ArrayList<Currency> currenciesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            try{
                content = getContent(uri);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(content));

                CurrencyXmlParser currencyParser = new CurrencyXmlParser(xpp);
                currenciesList = currencyParser.getCurrencyList();

                CurrencyAdapter adapter = new CurrencyAdapter(this, currenciesList);
                ListView listView = findViewById(R.id.listView);
                listView.post(() -> listView.setAdapter(adapter));

            }
            catch (IOException ex){

            } catch (XmlPullParserException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Button btnSave = findViewById(R.id.savedBtn);
        btnSave.setOnClickListener(v -> {
            write(content);
        });

        Button btnShow = findViewById(R.id.showBtn);
        btnShow.setOnClickListener(v -> {
            read();
        });
    }

    public void write(String textData) {
        try {
            FileOutputStream fileOutput = openFileOutput(FILENAME, MODE_PRIVATE);
            fileOutput.write(textData.getBytes());
            fileOutput.close();
            Log.v("CONTENT", textData);
            Toast.makeText(MainActivity.this, "Текст сохранён", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read() {
        TextView contentView = findViewById(R.id.content);
        try {
            FileInputStream fileInput = openFileInput(FILENAME);
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;

            while ((lines = buffer.readLine()) != null ) {
                strBuffer.append(lines + "\n");
            }

            contentView.setText(strBuffer.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getContent(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            Log.v("Reader", String.valueOf(reader));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }

            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}


//сохранить как arrayList (через objectStream) сохранить как xml
// в preferences зафиксировать дату обновления