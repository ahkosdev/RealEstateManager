package fr.kosdev.realestatemanager.Controllers;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.kosdev.realestatemanager.R;

public class PropertyDetailPhotoAdapter extends RecyclerView.Adapter<PropertyDetailPhotoAdapter.DetailPhotosViewHolder> {

    private List<String> propertyPhotos;

    public PropertyDetailPhotoAdapter(List<String> propertyPhotos) {
        this.propertyPhotos = propertyPhotos;
    }

    @NonNull
    @Override
    public DetailPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_list_item, parent, false);
        return new DetailPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPhotosViewHolder holder, int position) {
        holder.getPropertyPhotos(propertyPhotos.get(position));

    }

    @Override
    public int getItemCount() {
        return propertyPhotos.size();
    }

    public class DetailPhotosViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.photo_item_img)ImageView photoItem;

        public DetailPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void getPropertyPhotos(String photos){
            Glide.with(photoItem.getContext()).load(photos).into(photoItem);
        }
    }



}
