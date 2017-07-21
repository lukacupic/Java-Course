package hr.fer.android.hw0036493457;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayActivity extends AppCompatActivity {

    public static final String CALC_MESSAGE = "calcMessage";

    private TextView reportView;
    private Button okButton;
    private Button sendReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        reportView = (TextView) findViewById(R.id.reportMessage);
        okButton = (Button) findViewById(R.id.ok);
        sendReportButton = (Button) findViewById(R.id.sendReport);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(CALC_MESSAGE)) {
            String message = (String) extras.getSerializable(CALC_MESSAGE);
            sendReportButton.setText(message);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this, CalculusActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent, 200);
            }
        });

        sendReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"ana@baotic.org"});
                i.putExtra(Intent.EXTRA_SUBJECT, "0036493457: dz report");
                i.putExtra(Intent.EXTRA_TEXT, reportView.getText().toString());
                try {
                    startActivity(Intent.createChooser(i, "Pošalji e-mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DisplayActivity.this, "Nije pronađen e-mail klijent.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.ok)
    void closeScreen() {
        Intent intent = new Intent();
        intent.putExtras(new Bundle());
        finish();
    }
}
