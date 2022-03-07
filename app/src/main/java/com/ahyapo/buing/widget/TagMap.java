package com.ahyapo.buing.widget;

import androidx.annotation.NonNull;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TagMap<K,V> extends AbstractMap<K,V>{

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        Iterator<Entry<K,V>> i = entrySet().iterator();
        if (! i.hasNext())
            return "";

        StringBuilder sb = new StringBuilder();
        for (;;) {
            Entry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(value == this ? "(this Map)" : value);
            if (! i.hasNext())
                return sb.append(' ').toString();
            sb.append(',').append(' ');
        }
    }
}
