package de.gruschtelapps.fh_maa_refuelpair.views.activities.add;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.KontaktModel;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */
public class NewKontaktActivity extends AppCompatActivity {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String pattern_email = "\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b";

    // ===========================================================
    // Fields
    // ===========================================================
    MaterialEditText mTextName, mTextSurename, mTextPhone, mTextEmail, mTextAdress, mTextPolicyName, mTextPolicyNumber;
    private int flag;
    private KontaktModel mKontaktModel;

    // ===========================================================
    // Constructors
    // ===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_kontakt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get UI
        mTextName = findViewById(R.id.text_addKontakt_name);
        mTextSurename = findViewById(R.id.text_addKontakt_surename);
        mTextPhone = findViewById(R.id.text_addKontakt_phone);
        mTextEmail = findViewById(R.id.text_addKontakt_email);
        mTextAdress = findViewById(R.id.text_addKontakt_adress);


        // Load Data or New Instance
        if (getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT) != null) {

            // set UI
            KontaktModel kontaktModel = getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT);
            mTextName.setText(kontaktModel.getName());
            mTextSurename.setText(kontaktModel.getSurename());
            mTextPhone.setText(kontaktModel.getPhonenumber());
            mTextEmail.setText(kontaktModel.getMail());
            mTextAdress.setText(kontaktModel.getAdress());
        }
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menue_add, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Toolbar Button
            case android.R.id.home:
                onBackPressed();
                return true;

            // save and go back
            case R.id.menue_add_finish:
                if (Objects.equals(String.valueOf(mTextEmail.getText()), "") || pattern(String.valueOf(mTextEmail.getText()), pattern_email)) {

                    // save data
                    mKontaktModel = new KontaktModel(String.valueOf(mTextName.getText()),
                            String.valueOf(mTextSurename.getText()),
                            String.valueOf(mTextPhone.getText()),
                            String.valueOf(mTextEmail.getText()),
                            String.valueOf(mTextAdress.getText()));

                    Timber.d(mKontaktModel.toString());
                    Intent intent = new Intent();
                    intent.putExtra(ConstExtras.EXTRA_OBJECT_ADD, mKontaktModel);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    mTextEmail.setError(getResources().getString(R.string.error_email));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public String toString() {
        return "NewKontaktActivity{" +
                "pattern_email='" + pattern_email + '\'' +
                ", mTextName=" + mTextName +
                ", mTextSurename=" + mTextSurename +
                ", mTextPhone=" + mTextPhone +
                ", mTextEmail=" + mTextEmail +
                ", mTextAdress=" + mTextAdress +
                ", mTextPolicyName=" + mTextPolicyName +
                ", mTextPolicyNumber=" + mTextPolicyNumber +
                ", flag=" + flag +
                ", mKontaktModel=" + mKontaktModel +
                '}';
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Check if emal is correct
     *
     * @param value
     * @param expression
     * @return
     */
    public static boolean pattern(String value, String expression) {
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * Setzt ActionBar Title
     *
     * @param heading
     */
    private void setActionBarTitle(String heading) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(heading);
            actionBar.show();
        }
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
