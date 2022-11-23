package com.creativehazio.notepadapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creativehazio.notepadapp.R;
import com.creativehazio.notepadapp.model.Note;

import java.util.ArrayList;

public class NotesRecyclerView extends RecyclerView.Adapter<NotesRecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Note> noteArrayList;
    private ArrayList<Note> filteredNoteArrayList;
    private Listener listener;
    private LongClickListener longClickListener;

    public NotesRecyclerView(Context context, ArrayList<Note> noteArrayList) {
        this.context = context;
        this.noteArrayList = noteArrayList;
        this.filteredNoteArrayList = new ArrayList<>(noteArrayList);
    }

    public void filterList(ArrayList<Note> filteredNoteArrayList){
        this.noteArrayList = filteredNoteArrayList;
        notifyDataSetChanged();
    }

    public interface Listener {
        void onClick(int position);
    }
    public interface LongClickListener{
        void onLongClick(int position);

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.note_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView currentDate = cardView.findViewById(R.id.date);
        currentDate.setText(noteArrayList.get(position).getDate());

        TextView title = cardView.findViewById(R.id.title);
        title.setText(noteArrayList.get(position).getTitle());

        TextView contents = cardView.findViewById(R.id.contents);
        contents.setText(noteArrayList.get(position).getContent());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick(holder.getAdapterPosition());
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (longClickListener != null){
                    Animation fadeAnimation = AnimationUtils
                            .loadAnimation(holder.cardView.getContext(),
                                    R.anim.rotate_animation);
                    cardView.startAnimation(fadeAnimation);
                    longClickListener.onLongClick(holder.getAdapterPosition());
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }
}
