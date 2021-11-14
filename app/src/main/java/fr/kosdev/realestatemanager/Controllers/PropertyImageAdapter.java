package fr.kosdev.realestatemanager.Controllers;

import android.net.Uri;
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

public class PropertyImageAdapter extends RecyclerView.Adapter<PropertyImageAdapter.ImageViewHolder> {

    private List<Uri> imageUris;

    public PropertyImageAdapter(List<Uri> imageUris) {
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.getPropertyImage(imageUris.get(position));

    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_item_img)
        ImageView imageItem;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void getPropertyImage( Uri selectedImage){
            Glide.with(imageItem.getContext()).load(selectedImage).into(imageItem);
        }
    }
}
