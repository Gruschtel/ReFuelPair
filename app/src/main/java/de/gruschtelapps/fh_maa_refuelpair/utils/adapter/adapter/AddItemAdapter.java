package de.gruschtelapps.fh_maa_refuelpair.utils.adapter.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.httpModel.PetrolStationsModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.CarTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.FuelTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.ManufactureModel;
import timber.log.Timber;


/**
 * Create by Eric Werner
 * Verwaltet Items für die SelectedListActivity
 */
public class AddItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // https://guides.codepath.com/android/using-the-recyclerview
    // ===========================================================
    // Interfaces
    // ===========================================================
    public interface OnItemClickListener {
        void onItemClick(View view, Object object);
    }

    // ===========================================================
    // Constants
    // ===========================================================
    private final int VIEWTYPE_ITEM_TEXT = 0;
    private final int VIEWTYPE_ITEM_IMAGE = 1;
    private final int VIEWTYPE_ITEM_MULTI = 2;
    private final int VIEWTYPE_ITEM_PETROL_STATION = 3;

    // ===========================================================
    // Fields
    // ===========================================================
    private List<JsonModel> mItemList;
    private Activity mContext;
    private OnItemClickListener mListener;

    // ===========================================================
    // Constructors
    // ===========================================================
    public AddItemAdapter(List<JsonModel> itemList, Activity context) {
        mContext = context;
        mItemList = itemList;
    }

    public AddItemAdapter(Activity context, List<JsonModel> itemList, OnItemClickListener listener) {
        mContext = context;
        mItemList = itemList;
        mListener = listener;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public int getItemViewType(int position) {
        JsonModel vModel = mItemList.get(position);
        if (vModel instanceof CarTypeModel) return VIEWTYPE_ITEM_IMAGE;
        if (vModel instanceof ManufactureModel) return VIEWTYPE_ITEM_IMAGE;
        if (vModel instanceof FuelTypeModel) return VIEWTYPE_ITEM_IMAGE;
        if (vModel instanceof PetrolStationsModel) return VIEWTYPE_ITEM_PETROL_STATION;

        // Todo: später auf VIEWTYPE_ITEM_TEXT ändern
        return VIEWTYPE_ITEM_IMAGE;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case VIEWTYPE_ITEM_IMAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_adapter_item_image, viewGroup, false);
                return new ItemImageViewHolder(v);
            case VIEWTYPE_ITEM_PETROL_STATION:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_adapter_item_petrol_station, viewGroup, false);
                return new PetrolStationViewHolder(v);
            case VIEWTYPE_ITEM_TEXT:
                break;
        }
        // default
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_adapter_item_image, viewGroup, false);
        return new ItemImageViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final JsonModel vModel = mItemList.get(position);
        if (vModel == null) return;

        //
        // CarTypeModel
        //
        if (vModel instanceof CarTypeModel) {
            ((ItemImageViewHolder) viewHolder).textInformation.setText(((CarTypeModel) vModel).getName());
            ((ItemImageViewHolder) viewHolder).imageIcon.setImageDrawable(mContext.getDrawable(getReferenceId(((CarTypeModel) vModel).getImageName())));
        }

        //
        // ManufactureModel
        //
        if (vModel instanceof ManufactureModel) {
            ((ItemImageViewHolder) viewHolder).textInformation.setText(((ManufactureModel) vModel).getName());
            ((ItemImageViewHolder) viewHolder).imageIcon.setImageDrawable(mContext.getDrawable(getReferenceId(((ManufactureModel) vModel).getImageName())));
        }

        //
        // FuelTypeModel
        //
        if (vModel instanceof FuelTypeModel) {
            ((ItemImageViewHolder) viewHolder).textInformation.setText(((FuelTypeModel) vModel).getName());
            ((ItemImageViewHolder) viewHolder).imageIcon.setImageDrawable(mContext.getDrawable(getReferenceId(((FuelTypeModel) vModel).getImageName())));
        }

        //
        // FuelTypeModel
        //
        if (vModel instanceof PetrolStationsModel) {
            SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(mContext));
            String mType = pref.getPrefString(ConstPreferences.PREF_COMPARISON_TYPE);
            if (mType.equals(ConstError.ERROR_STRING)) {
                mType = "e10";
            }

            // Set Fuel Type Name
            String mPriceValue = "";
            if (pref.getPrefString(ConstPreferences.PREF_COMPARISON_SORT).equals("dist")) {
                switch (mType) {
                    case "diesel":
                        mPriceValue = String.valueOf(((PetrolStationsModel) vModel).getDiesel());
                        Timber.d(String.valueOf(((PetrolStationsModel) vModel).getDiesel()));
                        break;
                    case "e5":
                        mPriceValue = String.valueOf(((PetrolStationsModel) vModel).getE5());
                        Timber.d(String.valueOf(((PetrolStationsModel) vModel).getE5()));
                        break;
                    case "e10":
                        mPriceValue = String.valueOf(((PetrolStationsModel) vModel).getE10());
                        Timber.d(String.valueOf(((PetrolStationsModel) vModel).getE10()));
                        break;
                }
            } else {
                mPriceValue = String.valueOf(((PetrolStationsModel) vModel).getPrice());
                Timber.d("%s", ((PetrolStationsModel) vModel).getPrice());
            }

            // Standard information
            ((PetrolStationViewHolder) viewHolder).textPrice.setText(mPriceValue);
            ((PetrolStationViewHolder) viewHolder).textBrandname.setText(((PetrolStationsModel) vModel).getName());
            ((PetrolStationViewHolder) viewHolder).textAddress.setText(((PetrolStationsModel) vModel).getStreet() + ", " + ((PetrolStationsModel) vModel).getHousenumber());
            ((PetrolStationViewHolder) viewHolder).textPlace.setText(((PetrolStationsModel) vModel).getPostCode() + ", " + ((PetrolStationsModel) vModel).getPlace());
            ((PetrolStationViewHolder) viewHolder).textOdometer.setText(String.valueOf(((PetrolStationsModel) vModel).getDistance()));
        }
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) {
            return 0;
        }
        return mItemList.size();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Gibt eine MODEL_ID einer Resource zurück, welche an einer bestimmten Position der eingelesenen Referenzen befindet
     *
     * @param drawableName; Drawable nach der gesucht werden soll
     * @return id einer Ressource der Klasse R
     */
    private int getReferenceId(String drawableName) {
        return mContext.getResources().getIdentifier(drawableName, "drawable", mContext.getPackageName());
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Erzeugt einen ImageViewHolder (Bild + Icon)
     */
    public class ItemImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageIcon;
        private TextView textInformation;

        // Init UI-Elemente des Cardviews
        ItemImageViewHolder(final View itemView) {
            super(itemView);
            this.imageIcon = itemView.findViewById(R.id.image_icon);
            this.textInformation = itemView.findViewById(R.id.text_vehicleData_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                JsonModel jsonModel = mItemList.get(position);
                // We can access the data within the views
                Intent intent = new Intent();
                intent.putExtra(ConstExtras.EXTRA_OBJECT_ADD, jsonModel);
                mContext.setResult(Activity.RESULT_OK, intent);
                mContext.finish();
                //Toast.makeText(mContext, jsonModel.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Erzeugt einen Tankstellen ViewHolder (Tanstelle + Preis + Fueltype + Entfernung)
     */
    public class PetrolStationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textPrice, textBrandname, textAddress, textPlace, textOdometer;

        // Init UI-Elemente des Cardviews
        PetrolStationViewHolder(final View itemView) {
            super(itemView);
            // Todo:
            textPrice = itemView.findViewById(R.id.text_petrolStation_price);
            textBrandname = itemView.findViewById(R.id.text_petrolStation_stationName);
            textAddress = itemView.findViewById(R.id.text_petrolStation_street);
            textPlace = itemView.findViewById(R.id.text_petrolStation_place);
            textOdometer = itemView.findViewById(R.id.text_petrolStation_odometerValue);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                if (mListener != null) {
                    mListener.onItemClick(v, mItemList.get(position));
                }
            }
        }
    }
}
