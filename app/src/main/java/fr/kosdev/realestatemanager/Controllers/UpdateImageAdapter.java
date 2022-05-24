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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.kosdev.realestatemanager.Controllers.Activities.UpdatePropertyActivity;
import fr.kosdev.realestatemanager.R;

public class UpdateImageAdapter extends RecyclerView.Adapter<UpdateImageAdapter.UpdateImageViewHolder> {

    private List<String> imagesUri;
    UpdatePropertyActivity updateProperty;


    public UpdateImageAdapter(List<String> imagesUri, UpdatePropertyActivity updateProperty) {

        this.imagesUri = imagesUri;
        this.updateProperty = updateProperty;
    }

    @NonNull
    @Override
    public UpdateImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_image_list_item, parent , false);
        return new UpdateImageViewHolder(view, updateProperty);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateImageAdapter.UpdateImageViewHolder holder, int position) {
        holder.getPropertyImages(imagesUri.get(position));
        if (updateProperty.isContexualModeEnable){
            holder.deleteCheck.setVisibility(View.VISIBLE);
        }else {
            holder.deleteCheck.setVisibility(View.GONE);
            holder.deleteCheck.setChecked(false);
        }
        //holder.deleteImage(imagesUri.get(position));

    }

    @Override
    public int getItemCount() {
        return imagesUri.size();
    }

    public void removeImage(ArrayList<String> deletedImagesList){
        for (int i = 0; i< deletedImagesList.size(); i++){
            imagesUri.remove(deletedImagesList.get(i));
            notifyDataSetChanged();
        }
    }


    public class UpdateImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_update_item_img)
        ImageView imageUpdateItem;
        @BindView(R.id.delete_check)
        CheckBox deleteCheck;
        //@BindView(R.id.image_card)
        //CardView imageCard;


        public UpdateImageViewHolder(@NonNull View itemView, UpdatePropertyActivity updateProperty) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(updateProperty);
            deleteCheck.setOnClickListener(this);
        }


        public void getPropertyImages(String selectedImage){

            Glide.with(imageUpdateItem.getContext()).load(selectedImage).into(imageUpdateItem);
        }

        @Override
        public void onClick(View view) {
            updateProperty.makeSelection(view, getAdapterPosition());
        }



        //@OnClick(R.id.delete_image_fab)
        //public void deleteImage(String selectedImage){
            //imagesUri.remove(selectedImage);
        //}
    }
}
