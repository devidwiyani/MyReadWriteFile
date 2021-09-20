package com.example.myreadwritefile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnNew;
    Button btnOpen;
    Button btnSave;
    EditText editContent;
    EditText editTanggal;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    File path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNew = (Button) findViewById(R.id.button_new);
        btnOpen = (Button) findViewById(R.id.button_open);
        btnSave = (Button) findViewById(R.id.button_save);
        editContent = (EditText) findViewById(R.id.edit_file);
        editTanggal = (EditText) findViewById(R.id.edit_tanggal);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        editTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnNew.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        path = getFilesDir();
    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 Calendar newDate =Calendar.getInstance();
                 newDate.set(year, month, dayOfMonth);
                 editTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_new:
                newFile();
                break;
            case R.id.button_open:
                openFile();
                break;
            case R.id.button_save:
                saveFile();
                break;
        }
    }

    public void newFile() {

        editTanggal.setText("");
        editContent.setText("");

        Toast.makeText(this, "Clearing file", Toast.LENGTH_SHORT).show();
    }

    private void loadData(String title) {
        String text = FileHelper.readFromFile(this, title);
        editTanggal.setText(title);
        editContent.setText(text);
        Toast.makeText(this, "Loading " + title + " data", Toast.LENGTH_SHORT).show();
    }

    public void openFile() {
        showList();
    }

    private void showList() {
        final ArrayList<String> arrayList = new ArrayList<String>();
        for (String file : path.list()) {
            arrayList.add(file);
        }

        final CharSequence[] items = arrayList.toArray(new CharSequence[arrayList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Assignment");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                loadData(items[item].toString());
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void saveFile() {
        if (editTanggal.getText().toString().isEmpty()) {
            Toast.makeText(this, "Title harus diisi terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            String title = editTanggal.getText().toString();
            String text = editContent.getText().toString();
            FileHelper.writeToFile(title, text, this);
            Toast.makeText(this, "Saving " + editTanggal.getText().toString() + " file", Toast.LENGTH_SHORT).show();
        }
    }
}