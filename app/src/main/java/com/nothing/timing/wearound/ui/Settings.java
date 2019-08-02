package com.nothing.timing.wearound.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nothing.timing.wearound.R;
import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;

public class Settings extends AppCompatActivity {

    private static final String TAG = "Settings";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private RadioGroup group;

    private RadioButton radioOne;
    private RadioButton radioTwo;

    private Toolbar toolbar;
    private TextView number;
    private SeekBar seek;
    private Button save;

    private boolean radioChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        group = findViewById(R.id.radio_group);

        radioOne = findViewById(R.id.radio_one);
        radioTwo = findViewById(R.id.radio_two);

        number = findViewById(R.id.number);
        seek = findViewById(R.id.seek);
        save = findViewById(R.id.save);

        populate();
        setFunctionality();
    }

    private void populate() {

        sharedPref = getSharedPreferences(StaticData.getPrefTitle(), MODE_PRIVATE);
        editor = sharedPref.edit();

        int startValue = sharedPref.getInt(StaticData.getPrefRadiusKey(), 5000);

        if (sharedPref.getBoolean(StaticData.getPrefDistanceTypeKey(), false) ) {

            radioOne.toggle();

        } else if (!sharedPref.getBoolean(StaticData.getPrefDistanceTypeKey(), true) ) {

            radioTwo.toggle();

        } else {

            Log.e(TAG, "not km and not miles");
        }

        number.setText(String.valueOf(startValue)+"m");
        seek.setProgress(startValue);
    }

    private void setFunctionality() {

        seek.setOnSeekBarChangeListener(seekChange);
        save.setOnClickListener(saveClick);
        group.setOnCheckedChangeListener(groupCheck);
    }

    private RadioGroup.OnCheckedChangeListener groupCheck = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            radioChecked = true;
        }
    };

    private View.OnClickListener saveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (radioChecked) {

                if (radioOne.isChecked()) {

                    editor.putBoolean(StaticData.getPrefDistanceTypeKey(), true);

                } else if (radioTwo.isChecked()) {

                    editor.putBoolean(StaticData.getPrefDistanceTypeKey(), false);
                }
            }

            editor.putInt(StaticData.getPrefRadiusKey(), seek.getProgress());

            editor.apply();

            Intent intent = new Intent(Settings.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Settings.this.startActivity(intent);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            number.setText(String.valueOf(progress+"m"));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}
