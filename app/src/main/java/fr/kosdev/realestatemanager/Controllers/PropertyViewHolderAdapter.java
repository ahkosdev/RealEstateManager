package fr.kosdev.realestatemanager.Controllers;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import fr.kosdev.realestatemanager.Controllers.Activities.DetailsActivity;
import fr.kosdev.realestatemanager.Controllers.Fragments.DetailsFragment;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class PropertyViewHolderAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    private List<Property> propertyList;
    private long propertyId;
    private DetailsFragment detailsFragment;

    public PropertyViewHolderAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_list_items,parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.updateProperty(propertyList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                propertyId = propertyList.get(position).getId();
                if (detailsFragment == null || !detailsFragment.isVisible()){
                    Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                    intent.putExtra("KEY_DETAIL", propertyId);
                    view.getContext().startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public Property getProperty(int position){
        return this.propertyList.get(position);
    }
}
