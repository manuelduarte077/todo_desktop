package dev.donmanuel.fakeapi.adapters;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.donmanuel.fakeapi.models.BaseModel;

/**
 * Base adapter interface for RecyclerView adapters
 * Following the Interface Segregation Principle
 */
public interface BaseAdapter<T extends BaseModel, VH extends RecyclerView.ViewHolder> {
    void updateData(List<T> items);
    void addItem(T item);
    void removeItem(T item);
    T getItem(int position);
}
