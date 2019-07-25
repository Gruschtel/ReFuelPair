package de.gruschtelapps.fh_maa_refuelpair.views.fragment.add;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.adapter.CrashPhotoAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstAction;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.ItemListDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment.MessageFragmentDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.StorageImageManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NewCrashPhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCrashPhotosFragment extends Fragment implements View.OnClickListener, ItemListDialog.DialogListener, MessageFragmentDialog.DialogListener {
    // Fragments/ViewPager
    // https://stackoverflow.com/questions/30721664/android-toolbar-adding-menu-items-for-different-fragments
    // https://stackoverflow.com/questions/42302656/communication-objects-between-multiple-fragments-in-viewpager
    // Camera
    // https://developer.android.com/training/camera/photobasics#java
    // Dialoge
    // https://developer.android.com/guide/topics/ui/dialogs
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private int flag;
    private int count = 1;
    private CrashModel mCrashModel;

    protected int
            display_width,
            display_height;

    private RecyclerView mRecyclerView;
    private List<String> crashPhotoModels;
    private CrashPhotoAdapter adapter;

    // ===========================================================
    // Constructors
    // ===========================================================
    public NewCrashPhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VehicleFragment.
     */
    public static NewCrashPhotosFragment newInstance() {
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return new NewCrashPhotosFragment();
    }

    public static NewCrashPhotosFragment newInstance(CrashModel i) {
        NewCrashPhotosFragment fragment = new NewCrashPhotosFragment();
        Bundle args = new Bundle();
        args.putParcelable("flag", i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCrashModel = getArguments().getParcelable("flag");
            if (mCrashModel != null)
                flag = 1;
        }
        // ...
        Timber.d("%s created", getClass().getSimpleName());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_crash_photos, container, false);

        // Get UI
        getDisplayDimensions();

        // set recyvlerView
        mRecyclerView = v.findViewById(R.id.recyclerview_addCrash_photos);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);

        // Set span for grid layout
        // standard 3 span
        // if display_with between 500 and 300px then 2 span
        // else 1 span
        if (display_width < (500) && display_width >= (300)) {
            mGridLayoutManager.setSpanCount(2);
        } else if (display_width < (300)) {
            mGridLayoutManager.setSpanCount(1);
        }

        // set UI
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // init variables
        crashPhotoModels = new ArrayList<>();
        crashPhotoModels.add("");

        // load data
        if (flag == 1) {
            crashPhotoModels = new ArrayList<>();
            crashPhotoModels = mCrashModel.getPhotos();
        }

        // set OnItemClickListener
        adapter = new CrashPhotoAdapter(getActivity(), crashPhotoModels, new CrashPhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String photoPath, int position) {
                if (position == 0) {
                    confirmPhotoDialog();
                } else {
                    StorageImageManager storageImageManager = new StorageImageManager(Objects.requireNonNull(getContext()));
                    storageImageManager.deleteImage(photoPath);
                    crashPhotoModels.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mRecyclerView.setAdapter(adapter);


        return v;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // if edit than delete icon
        // if new than finish icon
        if (flag == 1) {
            inflater.inflate(R.menu.menue_add_delete, menu);
        } else {
            inflater.inflate(R.menu.menue_add, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // nothing ...
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            // if finish safe data and go back
            case R.id.menue_add_finish:
                // First Crash
                CrashModel mCrashShare = new CrashModel();
                if (flag == 1) {
                    // wenn bearbeiten änderungen hier
                    intent.setAction(ConstAction.ACTION_ADD_FINISH);
                } else {
                    // wenn new dann code here
                    intent.setAction(ConstAction.ACTION_ADD_FINISH);
                }

                // save data
                mCrashShare.setPhotos(crashPhotoModels);

                // send broadcast to parent activity with car data
                bundle.putParcelable(ConstBundle.BUNDLE_CRASH_PHOTO_SHARE, mCrashShare);
                intent.putExtras(bundle);
                Objects.requireNonNull(getContext()).sendBroadcast(intent);

                return true;

            // if delete -> delete data and go back
            case R.id.menue_add_delete:
                FragmentManager fm = getFragmentManager();
                MessageFragmentDialog messageDialog = MessageFragmentDialog.newInstance(ConstRequest.REQUEST_DIALOG_DELETE, R.string.title_button_delete, R.string.msg_button_delete);
                messageDialog.setTargetFragment(this, ConstRequest.REQUEST_DIALOG_POSITIV);
                messageDialog.show(Objects.requireNonNull(Objects.requireNonNull(fm)), "messageDialog");
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemDialogClick(DialogInterface dialog, int pos, int item) {
        switch (item) {
            case R.array.takephoto:
                /*
                <string-array name="takephoto" >
                    <item>@string/action_takePhotoNew</item>    (pos == 0)
                    <item>@string/action_choosePhotoNew</item>  (pos == 1)
                </string-array>
                 */
                if (pos == 0) {
                    openCamera();
                }
                if (pos == 1) {
                    openGallery();
                }
                break;
        }
    }

    @Override
    public void onButtonClick(int action, int flag) {

    }

    @Override
    public void onDialogItemClick(DialogInterface dialog, int pos, int item) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data)
            switch (requestCode) {

                // start device gallery
                case ConstRequest.REQUEST_IMAGE_GALLERY:
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), data.getData());
                        getImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                // start device camera
                case ConstRequest.REQUEST_IMAGE_CAMERA:
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    getImage(bitmap);
                    break;
            }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDialogButtonClick(int action, int flag) {
        // If the user responds positively to the deletion request, the object is to be deleted.
        if (action == ConstRequest.REQUEST_DIALOG_DELETE)
            if (flag == ConstRequest.REQUEST_DIALOG_POSITIV) {
                final DBHelper mDbHelper = new DBHelper(getContext());
                mDbHelper.getDelete().deleteItem(mCrashModel.getId());
                Intent finishIntent = new Intent();
                Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, finishIntent);
                getActivity().finish();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case ConstRequest.REQUEST_PERMISSION_OPEN_CAMERA:
                // If request is cancelled, the result arrays are empty.
                //noinspection StatementWithEmptyBody
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    dispatchTakePictureIntent();
                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                }
                break;
            case ConstRequest.REQUEST_PERMISSION_OPEN_GALLERY:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_openGallery)), ConstRequest.REQUEST_IMAGE_GALLERY);
                // other 'case' statements for other permssions
                break;
        }
    }


    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Saves a captured bitmap and assigns it to an object
     *
     * @param bitmap
     */
    private void getImage(Bitmap bitmap) {
        StorageImageManager storageImageManager = new StorageImageManager(Objects.requireNonNull(getContext()));
        String mSaveImage = storageImageManager.saveImage(bitmap);
        crashPhotoModels.add(mSaveImage);

        adapter.notifyItemChanged(adapter.getItemCount() - 1, null);
    }

    /**
     * start photo dialog
     */
    public void confirmPhotoDialog() {
        // Open Camera
        FragmentManager fm = getFragmentManager();
        ItemListDialog dialogFragment = ItemListDialog.newInstance(R.string.action_choosePhotoNew, R.array.takephoto);
        dialogFragment.setTargetFragment(this, ConstRequest.REQUEST_DIALOG_PHOTO);
        dialogFragment.show(Objects.requireNonNull(fm), "ItemListDialog");
    }

    /**
     * Start device camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, ConstRequest.REQUEST_IMAGE_CAMERA);
        }
    }

    /**
     * Permission checks to use camera
     */
    private void openCamera() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // User may have declined earlier, ask Android if we should show him a reason
                //noinspection StatementWithEmptyBody
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // show an explanation to the user
                    // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
                } else {
                    // request the permission.
                    // CALLBACK_NUMBER is a integer constants
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            ConstRequest.REQUEST_PERMISSION_OPEN_CAMERA);
                    // The callback method gets the result of the request.
                }
            } else {
                // got permission use it
                dispatchTakePictureIntent();
            }
        } else {
            // lower than VERSION_CODES.M
            dispatchTakePictureIntent();
        }
    }

    /**
     * Permission checks to use gallery
     */
    private void openGallery() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // User may have declined earlier, ask Android if we should show him a reason
                //noinspection StatementWithEmptyBody
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // show an explanation to the user
                    // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
                } else {
                    // request the permission.
                    // CALLBACK_NUMBER is a integer constants
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            ConstRequest.REQUEST_PERMISSION_OPEN_GALLERY);
                    // The callback method gets the result of the request.
                }
            } else {
                // got permission use it
                // Open Gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_openGallery)), ConstRequest.REQUEST_IMAGE_GALLERY);
            }
        } else {
            // lower than VERSION_CODES.M
            // Open Gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_openGallery)), ConstRequest.REQUEST_IMAGE_GALLERY);
        }
    }

    /**
     * Calculates display sizes (height, width, density)
     */
    private void getDisplayDimensions() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        display_width = size.x;
        display_height = size.y;

        int density = getResources().getDisplayMetrics().densityDpi;

        Timber.d("width x height: " + display_width + "x" + display_height + "\nDensity: " + density);
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
