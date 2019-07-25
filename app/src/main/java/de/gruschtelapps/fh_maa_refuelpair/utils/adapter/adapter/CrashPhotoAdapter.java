package de.gruschtelapps.fh_maa_refuelpair.utils.adapter.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.StorageImageManager;

/**
 * Create by Eric Werner
 */
public class CrashPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View view, String modelObjekt, int position);
    }

    // ===========================================================
    // Constants
    // ===========================================================
    private final int VIEWTYPE_ERROR = ConstError.ERROR_INT;
    private final int VIEWTYPE_NEW = 1;
    private final int VIEWTYPE_FILLED = 2;
    // ===========================================================
    // Fields
    // ===========================================================
    private List<String> mPhotoPath;
    private Activity mContext;
    private OnItemClickListener mListener;

    // ===========================================================
    // Constructors
    // ===========================================================
    public CrashPhotoAdapter(List<String> photoPath, Activity context) {
        mContext = context;
        mPhotoPath = photoPath;
    }

    public CrashPhotoAdapter(Activity context, List<String> photoPath, OnItemClickListener listener) {
        mContext = context;
        mPhotoPath = photoPath;
        mListener = listener;
    }
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_NEW;
        } else {
            return VIEWTYPE_FILLED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case VIEWTYPE_NEW:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_image_new, viewGroup, false);
                return new NewItemViewHolder(v);

            case VIEWTYPE_FILLED:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_image_filled, viewGroup, false);
                return new FilledItemViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final String vModel = mPhotoPath.get(position);
        if (position > 0) {
            StorageImageManager storageImageManager = new StorageImageManager(Objects.requireNonNull(mContext));
            Bitmap mSaveBitmap = storageImageManager.getImage(vModel);
            ((FilledItemViewHolder) viewHolder).imageView.setImageBitmap(mSaveBitmap);
        }
    }

    @Override
    public int getItemCount() {
        if (mPhotoPath == null) {
            return 0;
        }
        return mPhotoPath.size();
    }
    // ===========================================================
    // Methods
    // ===========================================================


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Erzeugt ein leerer Photo-platz in Crash Photos (Ohne Bild)
     */
    public class NewItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton imageButton;

        // Init UI-Elemente des Cardviews
        NewItemViewHolder(final View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.button_addCar_addPhoto);
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                if (mListener != null) {
                    mListener.onItemClick(v, mPhotoPath.get(position), position);
                }
            }
        }
    }

    /**
     * Erzeugt ein gefüllter Photo-platz in Crash Photos (mit Bild)
     */
    public class FilledItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout buttonDelete;
        ImageView imageView;

        // Init UI-Elemente des Cardviews
        FilledItemViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_addCrash_image);
            buttonDelete = itemView.findViewById(R.id.frame_customIconRound);
            buttonDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                if (mListener != null) {
                    mListener.onItemClick(v, mPhotoPath.get(position), position);
                }
            }
        }
    }

}
