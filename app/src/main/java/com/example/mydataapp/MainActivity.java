package com.example.mydataapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etNim, etNama, etProdi, etKelas, etAlamat, etEmail;
    private Button btnTambah, btnLogout;
    private LinearLayout listContainer;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String DATA_KEY = "User_Data_List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Security check: If not logged in, go back to Login
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        etNim = findViewById(R.id.et_nim);
        etNama = findViewById(R.id.et_nama);
        etProdi = findViewById(R.id.et_prodi);
        etKelas = findViewById(R.id.et_kelas);
        etAlamat = findViewById(R.id.et_alamat);
        etEmail = findViewById(R.id.et_email);
        btnTambah = findViewById(R.id.btn_tambah);
        btnLogout = findViewById(R.id.btn_logout);
        listContainer = findViewById(R.id.list_container);

        loadSavedData();

        btnTambah.setOnClickListener(v -> {
            String nim = etNim.getText().toString().trim();
            String nama = etNama.getText().toString().trim();
            String prodi = etProdi.getText().toString().trim();
            String kelas = etKelas.getText().toString().trim();
            String alamat = etAlamat.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty() || kelas.isEmpty() || alamat.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                saveData(nim, nama, prodi, kelas, alamat, email);
                addDataToView(nim, nama, prodi, kelas, alamat, email, true);
                clearForm();
                Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveData(String nim, String nama, String prodi, String kelas, String alamat, String email) {
        try {
            String existingData = sharedPreferences.getString(DATA_KEY, "[]");
            JSONArray jsonArray = new JSONArray(existingData);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nim", nim);
            jsonObject.put("nama", nama);
            jsonObject.put("prodi", prodi);
            jsonObject.put("kelas", kelas);
            jsonObject.put("alamat", alamat);
            jsonObject.put("email", email);

            jsonArray.put(jsonObject);

            sharedPreferences.edit().putString(DATA_KEY, jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedData() {
        try {
            String existingData = sharedPreferences.getString(DATA_KEY, "[]");
            JSONArray jsonArray = new JSONArray(existingData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                addDataToView(
                        obj.getString("nim"),
                        obj.getString("nama"),
                        obj.getString("prodi"),
                        obj.getString("kelas"),
                        obj.getString("alamat"),
                        obj.getString("email"),
                        false
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addDataToView(String nim, String nama, String prodi, String kelas, String alamat, String email, boolean atTop) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        cardView.setLayoutParams(params);
        cardView.setRadius(12f);
        cardView.setCardElevation(2f);
        cardView.setContentPadding(20, 20, 20, 20);
        cardView.setCardBackgroundColor(Color.WHITE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(createTextView("NIM: " + nim, true));
        layout.addView(createTextView("Nama: " + nama, false));
        layout.addView(createTextView("Prodi: " + prodi, false));
        layout.addView(createTextView("Kelas: " + kelas, false));
        layout.addView(createTextView("Alamat: " + alamat, false));
        layout.addView(createTextView("Email: " + email, false));

        cardView.addView(layout);
        if (listContainer != null) {
            if (atTop) {
                listContainer.addView(cardView, 0);
            } else {
                listContainer.addView(cardView);
            }
        }
    }

    private TextView createTextView(String text, boolean isBold) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(isBold ? Color.parseColor("#4F46E5") : Color.parseColor("#374151"));
        if (isBold) {
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textView.setPadding(0, 0, 0, 4);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setPadding(0, 2, 0, 2);
        }
        return textView;
    }

    private void clearForm() {
        if (etNim != null) etNim.setText("");
        if (etNama != null) etNama.setText("");
        if (etProdi != null) etProdi.setText("");
        if (etKelas != null) etKelas.setText("");
        if (etAlamat != null) etAlamat.setText("");
        if (etEmail != null) etEmail.setText("");
        if (etNim != null) etNim.requestFocus();
    }
}
