package de.diavololoop.util.linked;

/**
 * Created by Chloroplast on 05.07.2017.
 */
public class LinkedElement<T> {

    T payload;

    LinkedElement<T> previous;
    LinkedElement<T> next;

    public LinkedElement(T payload){
        this.payload = payload;
    }

}
