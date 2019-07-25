package de.gruschtelapps.fh_maa_refuelpair.views.fragment.charts;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.base.BaseChartFragment;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.RefuelModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.ServiceModel;
import timber.log.Timber;

/*
 * Create by Alea Sauer
 * Edit by Eric Werner: color adapted
 */
public class PieChartFrag extends BaseChartFragment {

    @NonNull
    public static Fragment newInstance() {
        return new PieChartFrag();
    }

    private PieChart chart;
    private LoadDataAsyncTask loadDataAsyncTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_pie, container, false);

        // get UI
        chart = v.findViewById(R.id.pieChart1);
        chart.getDescription().setEnabled(false);

        // set UI
        chart.setCenterText(generateCenterText());
        chart.setCenterTextSize(10f);

        // radius of the center hole in percent of maximum radius
        chart.setHoleRadius(45f);
        chart.setTransparentCircleRadius(50f);

        // set legend
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        // load data
        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute((Void) null);

        return v;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString(Objects.requireNonNull(getContext()).getResources().getString(R.string.msg_charts_piiTotal));
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    List<JsonModel> addModels;
    ArrayList<PieEntry> entries1;

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
            DBHelper dbHelper = new DBHelper(getContext());
            addModels = dbHelper.getGet().selectAllAdds(id);


            if (addModels.size() <= 0) {
                return false;
            }
            int startYear = 0;
            int endYear = 0;

            // Set/Get Date
            Calendar cStart = Calendar.getInstance();
            Calendar cEnde = Calendar.getInstance();
            Calendar testTime = Calendar.getInstance();

            long timeStart = cStart.getTimeInMillis();
            long timeEnde = cStart.getTimeInMillis();

            // get first year and latest year
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

            cStart.setTimeInMillis(timeStart);
            cEnde.setTimeInMillis(timeEnde);

            startYear = cStart.get(Calendar.YEAR);
            endYear = cEnde.get(Calendar.YEAR);

            // Get car cost for each year
            entries1 = new ArrayList<>();
            Calendar cDate = Calendar.getInstance();
            Timber.d(startYear + " - " + endYear);
            for (int a = startYear; a <= endYear; a++) {

                float cost1 = 0f;

                for (int i = 0; i < addModels.size(); i++) {
                    if (addModels.get(i) instanceof RefuelModel) {
                        cDate.setTimeInMillis(((RefuelModel) addModels.get(i)).getDate());
                        if (cDate.get(Calendar.YEAR) == a)
                            cost1 += Float.valueOf(String.valueOf(((RefuelModel) addModels.get(i)).getTotalCost()));

                    } else if (addModels.get(i) instanceof ServiceModel) {
                        cDate.setTimeInMillis(((ServiceModel) addModels.get(i)).getDate());
                        if (cDate.get(Calendar.YEAR) == a)
                            cost1 += Float.valueOf(String.valueOf(((ServiceModel) addModels.get(i)).getTotalCost()));
                    }
                }
                if (cost1 > 0) {
                    entries1.add(new PieEntry(cost1, "Year:  " + a));
                    onProgressUpdate(entries1);
                }

            }

            return true;
        }

        private void onProgressUpdate(ArrayList<PieEntry> i) {
            // set Data
            PieDataSet ds1 = new PieDataSet(i, getContext().getResources().getString(R.string.msg_charts_piiTotal));
            ds1.setColors(ColorTemplate.JOYFUL_COLORS);
            ds1.setSliceSpace(2f);
            ds1.setValueTextColor(Color.WHITE);
            ds1.setValueTextSize(12f);

            PieData d = new PieData(ds1);

            chart.setData(d);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // set Data
            if (!result) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.error_BarChart), Toast.LENGTH_SHORT).show();
                return;
            }

            PieDataSet ds1 = new PieDataSet(entries1, getContext().getResources().getString(R.string.msg_charts_piiTotal));
            ds1.setColors(ColorTemplate.JOYFUL_COLORS);
            ds1.setSliceSpace(2f);
            ds1.setValueTextColor(Color.WHITE);
            ds1.setValueTextSize(12f);

            PieData d = new PieData(ds1);

            chart.setData(d);
            // https://github.com/PhilJay/MPAndroidChart/issues/3326
            chart.invalidate();
        }

        @Override
        protected void onCancelled() {

        }
    }
}
