package ru.javaapp.workmanagement.master;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.javaapp.workmanagement.R;
import ru.javaapp.workmanagement.json.JSONParser;

public class TrackingActivity extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        text = (TextView)findViewById(R.id.text);
        new JsonReadData().execute();

    }

    public class JsonReadData extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.makeHttpRequest();

            return jsonObject;
        }

        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null) {
                    ListDrawer(json);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(TrackingActivity.this,  R.style.AlertDialogStyle);
                    builder.setCancelable(false);
                    builder.setTitle("Ошибка");
                    builder.setMessage("Нет соединения с интернетом.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Отпускает диалоговое окно
                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                Toast.makeText(TrackingActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(TrackingActivity.this, "Задача прервана, попробуйте заново", Toast.LENGTH_SHORT).show();
        }
    }

    public void ListDrawer(JSONObject json) throws JSONException {
        JSONArray jsonArray = null;
        String resultText = "";

        jsonArray = json.getJSONArray("allTasks_by_worker");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String nameMaster = jsonObject.getString("name");
            String nameWorker = jsonObject.getString("nameWorker");
            String nameWhat = jsonObject.getString("nameWhat");
            String namePlace = jsonObject.getString("namePlace");
            String countPlan = jsonObject.getString("count_plan");
            String countCurrent = jsonObject.getString("count_current");
            String timeStart = jsonObject.getString("time_start");
            String timeFinish = jsonObject.getString("time_finish");
            String dateStart = jsonObject.getString("date_start");
            String dateFinish = jsonObject.getString("date_finish");
            String comment = jsonObject.getString("comment");
            resultText += nameMaster + " " + nameWorker + " " + nameWhat + " " + namePlace + " " + countPlan + " " + countCurrent + " " + timeStart + " " + timeFinish + " " + dateStart + " " + dateFinish + " " + comment;

        }
        text.setText(resultText);
    }

}
