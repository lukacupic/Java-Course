package hr.fer.android.hw0036493457;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CalculusActivity extends AppCompatActivity {

    private static final String ERROR_MESSAGE = "Prilikom obavljanja operacije %s nad unosima" +
            " %s i %s došlo je do sljedeće greške: %s";

    private Spinner operationSpinner;
    private EditText firstInput;
    private EditText secondInput;
    private Button calculateButton;
    private TextView resultView;
    private String reportMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculus);

        initLayoutItems();

        calculateButton.setOnClickListener(new CalcButtonListener());
    }

    private void initLayoutItems() {
        operationSpinner = (Spinner) findViewById(R.id.operations_spinner);
        String[] items = new String[]{"+", "-", "*", "/"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        operationSpinner.setAdapter(adapter);

        firstInput = (EditText) findViewById(R.id.firstInput);
        secondInput = (EditText) findViewById(R.id.secondInput);
        calculateButton = (Button) findViewById(R.id.calculate);
        resultView = (TextView) findViewById(R.id.result);
    }

    private class CalcButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String first = firstInput.getText().toString();
            String second = secondInput.getText().toString();

            String operand = (String) operationSpinner.getSelectedItem();
            int firstNumber;
            int secondNumber;
            try {
                firstNumber = Integer.parseInt(first);
                secondNumber = Integer.parseInt(second);
            } catch (NumberFormatException ex) {
                reportMessage = String.format(ERROR_MESSAGE, operand, first, second, "Neispravan unos!");
                report(reportMessage);
                return;
            }

            double result = performOperation(operand, firstNumber, secondNumber);
            if (result == Double.NaN) {
                reportMessage = String.format(ERROR_MESSAGE, operand, first, second, "Dijeljenje s nulom!");
                report(reportMessage);
                return;
            }

            resultView.setText(String.valueOf(result));
            reportMessage = String.format("Rezultat operacije %s je %s.", operand, result);
            report(reportMessage);
        }

        private double performOperation(String operand, int firstNumber, int secondNumber) {
            double result = 0;
            switch (operand) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    result = firstNumber / secondNumber;
                    break;
            }
            return result;
        }

        private void report(String reportMessage) {
            Intent intent = new Intent(CalculusActivity.this, DisplayActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable(DisplayActivity.CALC_MESSAGE, reportMessage);
            intent.putExtras(extras);

            startActivityForResult(intent, 100);
        }
    }
}
