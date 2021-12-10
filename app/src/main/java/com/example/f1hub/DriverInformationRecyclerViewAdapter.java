package com.example.f1hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f1hub.data.DriverInfo;

import java.util.List;

public class DriverInformationRecyclerViewAdapter extends RecyclerView.Adapter<DriverInformationRecyclerViewAdapter.DriverInformationViewHolder> {

    // member variables for the adapter
    private final Context context;
    private List<DriverInfo> driverInfo;

    private static final String TAG = "DriverInformationRecyclerViewAdapter";

    /**
     * Creates a new link {@link DriverInformationRecyclerViewAdapter}
     * @param context that the adapter works in
     * @param driverInfo data to be put into the recyclerView
     */

    public DriverInformationRecyclerViewAdapter(Context context, List<DriverInfo> driverInfo){
        this.context = context;
        this.driverInfo = driverInfo;
    }

    @NonNull
    @Override
    public DriverInformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.data_display_list_item, parent, false);
        DriverInformationViewHolder viewHolder = new DriverInformationViewHolder(itemView,this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DriverInformationViewHolder holder, int position) {
        // get the data list item at position

        DriverInfo driverInfo = this.driverInfo.get(position);

        // update all the items within the data_display_list_item

        // driver name
        TextView tvDriverName = holder.driverInformationItemView.findViewById(R.id.tvDriversNames);
        tvDriverName.setText(driverInfo.getDriverName());

        // driver position
        TextView tvDriverPos = holder.driverInformationItemView.findViewById(R.id.tvDriverPosition);
        tvDriverPos.setText(driverInfo.getDriverPos());

        // driver points
        TextView tvDriverPoints = holder.driverInformationItemView.findViewById(R.id.tvDriverPoints);
        tvDriverPoints.setText(driverInfo.getDriverPoints());

        // driver wins
        TextView tvDriverPodiums = holder.driverInformationItemView.findViewById(R.id.tvDriverPodiums);
        tvDriverPodiums.setText(driverInfo.getDriverWins());


    }

    @Override
    public int getItemCount() {
        return this.driverInfo.size();
    }

    /**@NonNull
    @Override
    public driver**/

    class DriverInformationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View driverInformationItemView;
        private DriverInformationRecyclerViewAdapter adapter;

        /**
         *
         * @param driverInformationItemView The root View that is being displayed
         *                                  this is the fragment that gets duplicated
         * @param adapter The adapter that will hold the view holder
         */

        public DriverInformationViewHolder(View driverInformationItemView,DriverInformationRecyclerViewAdapter adapter){
            super(driverInformationItemView);
            this.driverInformationItemView = driverInformationItemView;
            this.adapter = adapter;
            this.driverInformationItemView.setOnClickListener(this);
        }
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // get the driverInformation item at the position
            DriverInfo driverInfo = this.adapter.driverInfo.get(position);

            // do this
            // future changes might have each driver in the RV open to their wiki's

        }
    }
}
