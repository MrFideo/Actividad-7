package com.tecmilenio.actividad7;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private Button buttonUpdatePost, buttonFetchPost;
    private EditText editTextPostId, editTextUpdateId, editTextUpdateTitle, editTextUpdateBody;
    private TextView textViewPostId, textViewPostTitle, textViewPostBody;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonFetchPost = findViewById(R.id.button_fetch_post);
        buttonUpdatePost = findViewById(R.id.button_update_post);
        editTextPostId = findViewById(R.id.edit_text_post_id);
        editTextUpdateId = findViewById(R.id.edit_text_update_id);
        editTextUpdateTitle = findViewById(R.id.edit_text_update_title);
        editTextUpdateBody = findViewById(R.id.edit_text_update_body);
        textViewPostId = findViewById(R.id.text_view_post_id);
        textViewPostTitle = findViewById(R.id.text_view_post_title);
        textViewPostBody = findViewById(R.id.text_view_post_body);
        requestQueue = Volley.newRequestQueue(this);

        buttonFetchPost.setOnClickListener(v -> fetchPostById());
        buttonUpdatePost.setOnClickListener(v -> updatePost());


    }

    private void fetchPostById() {
        String id = editTextPostId.getText().toString();
        String url = "https://jsonplaceholder.typicode.com/posts/" + id;
        Log.d("FetchPost", "Fetching URL: " + url);
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                Log.d("FetchPost", "Response: " + response.toString());
                textViewPostId.setText("ID: " + response.getInt("id"));
                textViewPostTitle.setText("Título: " + response.getString("title"));
                textViewPostBody.setText("Cuerpo: " + response.getString("body"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("FetchPost", "JSON Parsing Error: " + e.getMessage());
            }
        }, error -> {
            Log.e("FetchPost", "Error: " + error.getMessage());
            Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
        }));
    }


    private void updatePost() {
        String id = editTextPostId.getText().toString();
        String url = "https://jsonplaceholder.typicode.com/posts/" + id;
        Log.d("UpdatePost", "Updating URL: " + url);
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("title", editTextUpdateTitle.getText().toString());
            postParams.put("body", editTextUpdateBody.getText().toString());
            Log.d("UpdatePost", "Post Params: " + postParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("UpdatePost", "JSON Error: " + e.getMessage());
        }

        requestQueue.add(new JsonObjectRequest(Request.Method.PUT, url, postParams, response ->
                Toast.makeText(this, "Post actualizado correctamente", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error al actualizar el post", Toast.LENGTH_SHORT).show()));
    }

}