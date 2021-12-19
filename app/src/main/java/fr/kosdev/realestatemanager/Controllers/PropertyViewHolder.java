package fr.kosdev.realestatemanager.Controllers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.type_property_txt)
    TextView propertyType;
    @BindView(R.id.address_property_txt)
    TextView propertyAddress;
    @BindView(R.id.surface_property_txt)
    TextView propertySurface;
    @BindView(R.id.price_property_txt)
    TextView propertyPrice;
    @BindView(R.id.property_img)
    ImageView propertyImage;

    public PropertyViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateProperty(Property property){
        propertyType.setText(property.getType());
        propertyAddress.setText(property.getAddress());
        propertySurface.setText(property.getSurfaceOfProperty() + "m²");
        propertyPrice.setText(property.getPrice() + "$");
        Glide.with(propertyImage.getContext()).load(property.getPhotos().get(0)).into(propertyImage);

    }


}
