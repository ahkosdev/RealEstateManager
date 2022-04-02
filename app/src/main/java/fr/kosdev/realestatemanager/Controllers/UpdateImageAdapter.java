package fr.kosdev.realestatemanager.Controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.kosdev.realestatemanager.R;

public class UpdateImageAdapter extends RecyclerView.Adapter<UpdateImageAdapter.UpdateImageViewHolder> {

    private List<String> imagesUri;


    public UpdateImageAdapter(List<String> imagesUri) {

        this.imagesUri = imagesUri;
    }

    @NonNull
    @Override
    public UpdateImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_image_list_item, parent , false);
        return new UpdateImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateImageAdapter.UpdateImageViewHolder holder, int position) {
        holder.getPropertyImages(imagesUri.get(position));
        //holder.deleteImage(imagesUri.get(position));

    }

    @Override
    public int getItemCount() {
        return imagesUri.size();
    }


    public class UpdateImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_update_item_img)
        ImageView imageUpdateItem;
        @BindView(R.id.delete_check)
        CheckBox deleteCheck;
        //@BindView(R.id.image_card)
        //CardView imageCard;


        public UpdateImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void getPropertyImages(String selectedImage){

            Glide.with(imageUpdateItem.getContext()).load(selectedImage).into(imageUpdateItem);
        }

        //@OnClick(R.id.delete_image_fab)
        //public void deleteImage(String selectedImage){
            //imagesUri.remove(selectedImage);
        //}
    }
}
