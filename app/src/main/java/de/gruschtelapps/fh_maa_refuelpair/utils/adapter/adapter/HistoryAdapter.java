package de.gruschtelapps.fh_maa_refuelpair.utils.adapter.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.RefuelModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.ServiceModel;


/**
 * Create by Eric Werner
 */
public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    private final int VIEWTYPE_ERROR = -1;
    private final int VIEWTYPE_REFUELING = 1;
    private final int VIEWTYPE_SERVICES = 2;
    private final int VIEWTYPE_CRASH = 3;

    public interface OnItemClickListener {
        void onItemClick(View view, JsonModel object);
    }

    // ===========================================================
    // Fields
    // ===========================================================
    private List<JsonModel> mItemList;
    private Activity mContext;
    private OnItemClickListener mListener;


    // ===========================================================
    // Constructors
    // ===========================================================
    public HistoryAdapter(List<JsonModel> itemList, Activity context) {
        mContext = context;
        mItemList = itemList;
    }

    public HistoryAdapter(Activity context, List<JsonModel> itemList, OnItemClickListener listener) {
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
        if (vModel instanceof RefuelModel) return VIEWTYPE_REFUELING;
        if (vModel instanceof ServiceModel) return VIEWTYPE_SERVICES;
        if (vModel instanceof CrashModel) return VIEWTYPE_CRASH;


        // Todo: später auf VIEWTYPE_ITEM_TEXT ändern
        return VIEWTYPE_REFUELING;
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case VIEWTYPE_REFUELING:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_adapter_history_refuel, viewGroup, false);
                return new HistoryRefuelItemViewHolder(v);
            case VIEWTYPE_SERVICES:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_adapter_history_service, viewGroup, false);
                return new HistoryServiceItemViewHolder(v);
            case VIEWTYPE_CRASH:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_adapter_history_crash, viewGroup, false);
                return new HistoryCrashItemViewHolder(v);

            default:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_adapter_history_refuel, viewGroup, false);
                return new HistoryRefuelItemViewHolder(v);

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final JsonModel vModel = mItemList.get(position);
        String year, month, day, totalCost;
        Calendar c = Calendar.getInstance();

        if (vModel == null) return;
        //
        // RefuelModel
        //
        if (vModel instanceof RefuelModel) {
            ((HistoryRefuelItemViewHolder) viewHolder).textPlace.setText(((RefuelModel) vModel).getLocal());
            if (((RefuelModel) vModel).getOdometer() == ConstError.ERROR_LONG) {
                ((HistoryRefuelItemViewHolder) viewHolder).textOdometer.setText("");
                ((HistoryRefuelItemViewHolder) viewHolder).text_customInformation_odometerValue.setVisibility(View.GONE);
            } else {
                ((HistoryRefuelItemViewHolder) viewHolder).textOdometer.setText(String.valueOf(((RefuelModel) vModel).getOdometer()));
            }
            ((HistoryRefuelItemViewHolder) viewHolder).textLiter.setText(String.valueOf(((RefuelModel) vModel).getLiter()));

            totalCost = String.valueOf(((RefuelModel) vModel).getTotalCost());
            ((HistoryRefuelItemViewHolder) viewHolder).textCost.setText(totalCost);

            c.setTimeInMillis(((RefuelModel) vModel).getDate());

            year = setNullVorne(c.get(Calendar.YEAR), 4);
            month = setNullVorne(c.get(Calendar.MONTH), 2);
            day = setNullVorne(c.get(Calendar.DAY_OF_MONTH), 2);

            ((HistoryRefuelItemViewHolder) viewHolder).textDate.setText(String.format(mContext.getResources().getString(R.string.value_date),
                    year,
                    month,
                    day));

            if (position == 0) {
                ((HistoryRefuelItemViewHolder) viewHolder).imageStreet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_street_finish));
            }
            if (position == mItemList.size() - 1) {
                ((HistoryRefuelItemViewHolder) viewHolder).imageStreet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_street_start));
            }
        }
        //
        // ServiceModel
        //
        if (vModel instanceof ServiceModel) {
            ((HistoryServiceItemViewHolder) viewHolder).textPlace.setText(((ServiceModel) vModel).getLocal());

            if (((ServiceModel) vModel).getOdometer() == ConstError.ERROR_LONG) {
                ((HistoryServiceItemViewHolder) viewHolder).textOdometer.setText("");
                ((HistoryServiceItemViewHolder) viewHolder).text_customInformation_odometerValue.setVisibility(View.GONE);
            } else {
                ((HistoryServiceItemViewHolder) viewHolder).textOdometer.setText(String.valueOf(((ServiceModel) vModel).getOdometer()));
            }
            ((HistoryServiceItemViewHolder) viewHolder).textService.setText(((ServiceModel) vModel).getService());

            totalCost = String.valueOf(((ServiceModel) vModel).getTotalCost());
            ((HistoryServiceItemViewHolder) viewHolder).textCost.setText(totalCost);

            c.setTimeInMillis(((ServiceModel) vModel).getDate());

            year = setNullVorne(c.get(Calendar.YEAR), 4);
            month = setNullVorne(c.get(Calendar.MONTH), 2);
            day = setNullVorne(c.get(Calendar.DAY_OF_MONTH), 2);

            ((HistoryServiceItemViewHolder) viewHolder).textDate.setText(String.format(mContext.getResources().getString(R.string.value_date),
                    year,
                    month,
                    day));

            if (position == 0) {
                ((HistoryServiceItemViewHolder) viewHolder).imageStreet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_street_finish));
            }
            if (position == mItemList.size() - 1) {
                ((HistoryServiceItemViewHolder) viewHolder).imageStreet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_street_start));
            }
        }
        //
        // RefuelModel
        //
        if (vModel instanceof CrashModel) {
            ((HistoryCrashItemViewHolder) viewHolder).textLocal.setText(((CrashModel) vModel).getLocal());

            c.setTimeInMillis(((CrashModel) vModel).getDate());

            year = setNullVorne(c.get(Calendar.YEAR), 4);
            month = setNullVorne(c.get(Calendar.MONTH), 2);
            day = setNullVorne(c.get(Calendar.DAY_OF_MONTH), 2);

            ((HistoryCrashItemViewHolder) viewHolder).textDate.setText(String.format(mContext.getResources().getString(R.string.value_date),
                    year,
                    month,
                    day));

            if (position == 0) {
                ((HistoryCrashItemViewHolder) viewHolder).imageStreet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_street_finish));
            }
            if (position == mItemList.size() - 1) {
                ((HistoryCrashItemViewHolder) viewHolder).imageStreet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_street_start));
            }
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
    public static String setNullVorne(int zahl, int laenge) {
        StringBuilder tmp = new StringBuilder(Integer.toString(zahl));
        int leng = tmp.length();
        if (leng < laenge) {
            for (; leng < laenge; leng++) {
                tmp.insert(0, "0");
            }
        }
        return tmp.toString();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public class HistoryRefuelItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textPlace, textOdometer, textLiter, textCost, textDate, text_customInformation_odometerValue;
        private ImageView imageStreet;

        // Init UI-Elemente des Cardviews
        HistoryRefuelItemViewHolder(final View itemView) {
            super(itemView);
            textPlace = itemView.findViewById(R.id.text_customInformation_local);
            textOdometer = itemView.findViewById(R.id.text_customInformation_odometer);
            text_customInformation_odometerValue = itemView.findViewById(R.id.text_customInformation_odometerValue);
            textLiter = itemView.findViewById(R.id.text_customInformation_fuelLiter);
            textCost = itemView.findViewById(R.id.text_customInformation_costValue);
            textDate = itemView.findViewById(R.id.text_customInformation_date);
            imageStreet = itemView.findViewById(R.id.image_adapterViewHistory_street);
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

    public class HistoryServiceItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textPlace, textOdometer, textService, textCost, textDate, text_customInformation_odometerValue;
        private ImageView imageStreet;

        // Init UI-Elemente des Cardviews
        HistoryServiceItemViewHolder(final View itemView) {
            super(itemView);
            textPlace = itemView.findViewById(R.id.text_customInformation_local);
            textOdometer = itemView.findViewById(R.id.text_customInformation_odometer);
            text_customInformation_odometerValue = itemView.findViewById(R.id.text_customInformation_odometerValue);
            textService = itemView.findViewById(R.id.text_customInformation_fuelLiter);
            textCost = itemView.findViewById(R.id.text_customInformation_costValue);
            textDate = itemView.findViewById(R.id.text_customInformation_date);
            imageStreet = itemView.findViewById(R.id.image_adapterViewHistory_street);
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

    public class HistoryCrashItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textLocal, textDate;
        private ImageView imageStreet;

        // Init UI-Elemente des Cardviews
        HistoryCrashItemViewHolder(final View itemView) {
            super(itemView);
            // Todo:
            textLocal = itemView.findViewById(R.id.text_customInformation_local);
            textDate = itemView.findViewById(R.id.text_customInformation_date);
            imageStreet = itemView.findViewById(R.id.image_adapterViewHistory_street);
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
