package de.diavololoop.util.linked;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by Chloroplast on 05.07.2017.
 */
public class LinkedIterator<T> implements Iterator<T>, ListIterator<T> {


    LinkedElement<T> current;
    boolean desc;
    int index;

    public LinkedIterator(LinkedElement<T> first, boolean desc, int index) {
        current = first;
        this.index = index;
    }

    @Override
    public boolean hasNext() {
        return (current != null);
    }

    @Override
    public T next() {

        T element = current.payload;

        if(desc){
            current = current.previous;
            --index;
        }else{
            current = current.next;
            ++index;
        }


        return element;
    }


    @Override
    public boolean hasPrevious() {
        return current != null;
    }

    @Override
    public T previous() {
        T element = current.payload;

        if(desc){
            current = current.next;
            ++index;
        }else{
            current = current.previous;
            --index;
        }


        return element;
    }

    @Override
    public int nextIndex() {
        return index;
    }

    @Override
    public int previousIndex() {
        return index;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException();
    }
}
