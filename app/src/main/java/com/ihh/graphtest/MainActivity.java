package com.ihh.graphtest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText itemEditText;
    private EditText priceEditText;
    private Button addButton;
    private BarChart barChart;

    private List<String> itemList;
    private List<Float> priceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemEditText = findViewById(R.id.itemEditText);
        priceEditText = findViewById(R.id.priceEditText);
        addButton = findViewById(R.id.addButton);
        barChart = findViewById(R.id.barChart);

        //품목명, 가격이 저장되는 배열
        itemList = new ArrayList<>();
        priceList = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                String item = itemEditText.getText().toString().trim();
                String priceString = priceEditText.getText().toString().trim();

                if (!item.isEmpty() && !priceString.isEmpty()) {
                    float price = Float.parseFloat(priceString);

                    itemList.add(item);
                    priceList.add(price);

                    itemEditText.setText("");
                    priceEditText.setText("");

                    showBarGraph();
                }
            }
        });
    }

    private void showBarGraph() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < priceList.size(); i++) {
            entries.add(new BarEntry(i, priceList.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Price");
        dataSet.setColor(Color.BLUE);

        ArrayList<String> xLabels = new ArrayList<>(itemList);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new LabelFormatter(xLabels));

        // Set the maximum value for the y-axis
        float maxYValue = getMaxYValue(priceList);
        barChart.getAxisLeft().setAxisMaximum(maxYValue);

        // Set the minimum value for the y-axis
        barChart.getAxisLeft().setAxisMinimum(0f);

        // Customize the x-axis appearance
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setAxisLineColor(Color.BLACK);
        barChart.getXAxis().setTextColor(Color.BLACK);

        // Set the x-axis label rotation and position
        barChart.getXAxis().setLabelRotationAngle(45f);
        barChart.getXAxis().setLabelCount(xLabels.size());
        barChart.getXAxis().setXOffset(10f);
        barChart.getXAxis().setYOffset(10f);

        // Customize the y-axis appearance
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisLineColor(Color.BLACK);
        barChart.getAxisLeft().setTextColor(Color.BLACK);

        barChart.getDescription().setText("");
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private float getMaxYValue(List<Float> values) {
        float max = 0;
        for (Float value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max + 1000;
    }

    private static class LabelFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {
        private final List<String> labels;

        LabelFormatter(List<String> labels) {
            this.labels = labels;
        }

        @Override
        public String getFormattedValue(float value) {
            int index = (int) value;
            if (index >= 0 && index < labels.size()) {
                return labels.get(index);
            }
            return "";
        }
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

}
