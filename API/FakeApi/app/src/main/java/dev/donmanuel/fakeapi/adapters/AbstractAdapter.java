package dev.donmanuel.fakeapi.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.fakeapi.models.BaseModel;

/**
 * Abstract adapter that implements common functionality for all adapters
 * Following the Open/Closed Principle and Single Responsibility Principle
 */
public abstract class AbstractAdapter<T extends BaseModel, VH extends RecyclerView.ViewHolder> 
        extends RecyclerView.Adapter<VH> implements BaseAdapter<T, VH> {
    
    protected List<T> items;
    
    public AbstractAdapter() {
        this.items = new ArrayList<>();
    }
    
    public AbstractAdapter(List<T> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    @Override
    public void updateData(List<T> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    @Override
    public void addItem(T item) {
        if (item != null) {
            items.add(item);
            notifyItemInserted(items.size() - 1);
        }
    }
    
    @Override
    public void removeItem(T item) {
        if (item != null) {
            int position = -1;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getId() == item.getId()) {
                    position = i;
                    break;
                }
            }
            
            if (position >= 0) {
                items.remove(position);
                notifyItemRemoved(position);
            }
        }
    }
    
    @Override
    public T getItem(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }
}
