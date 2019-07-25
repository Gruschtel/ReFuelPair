package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import timber.log.Timber;

/*
 * Create by Alea Sauer
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImprintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImprintFragment extends Fragment {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    // Model
    protected LoadDataAsyncTask loadDataAsyncTask;
    // UI
    private TextView mImprintTextView;
    Spanned hmtlText;

    // ===========================================================
    // Constructors
    // ===========================================================
    public ImprintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImprintFragment.
     */
    public static ImprintFragment newInstance() {
        return new ImprintFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ...
        Timber.tag(LOG_TAG);
        Timber.d("%s created", LOG_TAG);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_imprint, container, false);

        // Get UI
        mImprintTextView = v.findViewById(R.id.tv_imprint_text);

        // Load Data
        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute((Void) null);

        return v;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        loadDataAsyncTask.cancel(true);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Starts a background task, which all needed Data for Fragment
     */
    @SuppressLint("StaticFieldLeak")
    public class LoadDataAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // set HTML Text
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    hmtlText = Html.fromHtml(Objects.requireNonNull(getActivity()).getResources().getString(R.string.msg_leaglDisclosure), Html.FROM_HTML_MODE_COMPACT);
                } else {
                    hmtlText = Html.fromHtml(Objects.requireNonNull(getActivity()).getResources().getString(R.string.msg_leaglDisclosure));
                }
                return true;
            } catch (Exception e) {
                Timber.e(e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                //Todo: ERROR MESSAGE
                return;
            }
            mImprintTextView.setText(hmtlText);
        }

        @Override
        protected void onCancelled() {

        }
    }

}
