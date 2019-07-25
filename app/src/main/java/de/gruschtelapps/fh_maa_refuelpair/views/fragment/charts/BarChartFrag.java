package de.gruschtelapps.fh_maa_refuelpair.views.fragment.charts;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.base.BaseChartFragment;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.RefuelModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.ServiceModel;
import timber.log.Timber;

/*
 * Create by Eric Werner
 */
public class BarChartFrag extends BaseChartFragment implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private BarChart chart;
    private SeekBar seekBarX;
    private TextView tvX;
    private LoadDataAsyncTask loadDataAsyncTask;

    int groupCount;
    int startYear;
    int endYear;

    ArrayList<BarEntry> values1;
    ArrayList<BarEntry> values2;

    @NonNull
    public static Fragment newInstance() {
        return new BarChartFrag();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_bar, container, false);

        // get UI
        tvX = v.findViewById(R.id.tvXMax);
        tvX.setTextSize(10);
        seekBarX = v.findViewById(R.id.seekBar1);
        chart = v.findViewById(R.id.chart1);

        // set Listener
        seekBarX.setOnSeekBarChangeListener(this);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);


        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(true);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(true);

        // Set legend
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        // Set Axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        xAxis.setEnabled(true);

        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute((Void) null);

        return v;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        float groupSpace = 0.08f;
        float barSpace = 0.06f; // x4 DataSet
        float barWidth = 0.4f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        tvX.setText(String.format(Locale.ENGLISH, "%d-%d", startYear, endYear));

        groupCount = seekBarX.getProgress() + 1;
        endYear = startYear + groupCount - 1;


        if (addModels.size() <= 0) {
            seekBarX.setVisibility(View.GONE);
            return;
        } else {
            seekBarX.setVisibility(View.VISIBLE);
        }

        values1 = new ArrayList<>();
        values2 = new ArrayList<>();

        // Set Date & Get Values für einen Zeitraum
        Calendar cDate = Calendar.getInstance();
        for (int a = startYear; a <= endYear; a++) {
            float cost1, cost2;
            cost1 = 0f;
            cost2 = 0f;
            for (int i = 0; i < addModels.size(); i++) {
                if (addModels.get(i) instanceof RefuelModel) {
                    cDate.setTimeInMillis(((RefuelModel) addModels.get(i)).getDate());
                    if (cDate.get(Calendar.YEAR) == a)
                        cost1 += Float.valueOf(String.valueOf(((RefuelModel) addModels.get(i)).getTotalCost()));
                } else if (addModels.get(i) instanceof ServiceModel) {
                    cDate.setTimeInMillis(((ServiceModel) addModels.get(i)).getDate());
                    if (cDate.get(Calendar.YEAR) == a)
                        cost2 += Float.valueOf(String.valueOf(((ServiceModel) addModels.get(i)).getTotalCost()));
                }
            }
            values1.add(new BarEntry(a, cost1));
            values2.add(new BarEntry(a, cost2));
        }

        tvX.setText(String.format(Locale.ENGLISH, "%d-%d", startYear, endYear));
        groupCount = seekBarX.getProgress() + 1;
        endYear = startYear + groupCount;

        BarDataSet set1, set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, getContext().getResources().getString(R.string.title_refuel));
            set1.setColor(getContext().getResources().getColor(R.color.colorGreenDark));
            set2 = new BarDataSet(values2, getContext().getResources().getString(R.string.title_service));
            set2.setColor(getContext().getResources().getColor(R.color.colorBlueDark));

            BarData data = new BarData(set1, set2);
            data.setValueFormatter(new LargeValueFormatter());

            chart.setData(data);
        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startYear + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(startYear, groupSpace, barSpace);
        chart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    List<JsonModel> addModels;

    /**
     * Starts a background task, which all needed Data for Fragment
     */
    @SuppressLint("StaticFieldLeak")
    public class LoadDataAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // Load Data
            SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getContext()));
            long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);
            if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
                DBHelper dbHelper = new DBHelper(getContext());
                addModels = dbHelper.getGet().selectAllAdds(id);
            } else {
                return false;
            }
            if (addModels.size() <= 0) {
                seekBarX.setVisibility(View.GONE);
                return false;
            } else {
                seekBarX.setVisibility(View.VISIBLE);
            }
            groupCount = 0;
            startYear = 0;
            endYear = 0;

            // get first year and latest year
            Calendar cStart = Calendar.getInstance();
            Calendar cEnde = Calendar.getInstance();
            Calendar testTime = Calendar.getInstance();

            long timeStart = cStart.getTimeInMillis();
            long timeEnde = cStart.getTimeInMillis();

            // Load Data
            for (int i = 0; i < addModels.size(); i++) {
                if (addModels.get(i) instanceof RefuelModel) {
                    testTime.setTimeInMillis(((RefuelModel) addModels.get(i)).getDate());
                    //
                    Timber.d("RefuelModel: %s", String.valueOf(testTime.getTimeInMillis()));
                    Timber.d(timeEnde + " > " + testTime.getTimeInMillis());
                    //
                    if (timeEnde <= testTime.getTimeInMillis()) {
                        timeEnde = testTime.getTimeInMillis();
                    }
                    //
                    Timber.d(timeStart + " < " + testTime.getTimeInMillis());
                    if (timeStart > testTime.getTimeInMillis()) {
                        timeStart = testTime.getTimeInMillis();
                    }
                }

                if (addModels.get(i) instanceof ServiceModel) {
                    testTime.setTimeInMillis(((ServiceModel) addModels.get(i)).getDate());
                    //
                    Timber.d("ServiceModel: %s", String.valueOf(testTime.getTimeInMillis()));
                    Timber.d(timeEnde + " > " + testTime.getTimeInMillis());
                    //
                    if (timeEnde <= testTime.getTimeInMillis()) {
                        timeEnde = testTime.getTimeInMillis();
                    }
                    //
                    Timber.d(timeStart + " < " + testTime.getTimeInMillis());
                    if (timeStart > testTime.getTimeInMillis()) {
                        timeStart = testTime.getTimeInMillis();
                    }
                }
            }

            // set Data
            cStart.setTimeInMillis(timeStart);
            cEnde.setTimeInMillis(timeEnde);

            // set Progress
            seekBarX.setMax(cEnde.get(Calendar.YEAR) - cStart.get(Calendar.YEAR));
            seekBarX.setProgress(cEnde.get(Calendar.YEAR) - cStart.get(Calendar.YEAR));

            groupCount = seekBarX.getProgress() + 1;
            startYear = cStart.get(Calendar.YEAR);
            endYear = cEnde.get(Calendar.YEAR);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.error_BarChart), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}

