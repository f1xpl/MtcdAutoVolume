package com.microntek.f1x.mtcdautovolume.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.microntek.f1x.mtcdautovolume.R;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelsGenerator;

public class VolumeLevelRangeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getText(R.string.ProvideRange));
        setContentView(R.layout.activity_volume_level_range);

        final int volumeLevelsCount = getIntent() != null ? getIntent().getIntExtra(VOLUME_LEVELS_COUNT_EXTRA, 0) : 0;
        final EditText startRangeEditText = (EditText)this.findViewById(R.id.editTextStartRange);
        final EditText endRangeEditText = (EditText)this.findViewById(R.id.editTextEndRange);

        Button okButton = (Button)this.findViewById(R.id.buttonOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] range = null;

                try {
                    final Integer startRange = Integer.parseInt(startRangeEditText.getText().toString());
                    final Integer endRange = Integer.parseInt(endRangeEditText.getText().toString());
                    range = VolumeLevelsGenerator.generate(startRange, endRange, volumeLevelsCount);
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                }

                if(range == null) {
                    Toast.makeText(VolumeLevelRangeActivity.this, VolumeLevelRangeActivity.this.getString(R.string.InvalidRange), Toast.LENGTH_SHORT).show();
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(GENERATED_RANGE_EXTRA, range);
                    VolumeLevelRangeActivity.this.setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        Button cancelButton = (Button)this.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolumeLevelRangeActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public static final String GENERATED_RANGE_EXTRA = "GeneratedRange";
    public static final String VOLUME_LEVELS_COUNT_EXTRA = "VolumeLevelsCount";
}
