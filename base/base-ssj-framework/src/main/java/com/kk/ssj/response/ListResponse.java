package com.kk.ssj.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author declan
 * @param <TItem>
 */
public class ListResponse<TItem> {

    private List<TItem> items = new ArrayList<>();

    /**
     * Get the value of items
     *
     * @return the value of items
     */
    public List<TItem> getItems() {
        return items;
    }

    /**
     * Set the value of items
     *
     * @param items new value of items
     */
    public void setItems(List<TItem> items) {
        this.items = items;
    }

    /**
     * Add item to items
     *
     * @param item item to added
     */
    public void addItem(TItem item) {
        this.items.add(item);
    }

}
