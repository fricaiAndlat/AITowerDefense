package de.diavololoop.util.linked;

import java.util.*;

/**
 * Created by Chloroplast on 05.07.2017.
 */
public class Linked<T> implements List<T>, Deque<T> {


    private int size;
    private LinkedElement<T> first;
    private LinkedElement<T> last;

    private Object lockFirst = new Object();
    private Object lockLast = new Object();




    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if(size == 0){
            return false;
        }

        LinkedIterator iterator = this.iterator();

        while(iterator.hasNext()){
            if(o.equals(iterator.next())){
                return true;
            }
        }
        return false;
    }

    @Override
    public LinkedIterator<T> iterator() {
        return new LinkedIterator<T>(first, false, 0);
    }

    @Override
    public LinkedIterator<T> descendingIterator() {
        return new LinkedIterator<T>(last, true, size-1);
    }

    @Override
    public Object[] toArray() {

        Object[] array = new Object[size];

        LinkedIterator<T> iterator = this.iterator();

        int cc = 0;

        while(iterator.hasNext()){

            if(cc >= array.length){
                break;
            }
            array[cc++] = iterator.next();
        }

        if(cc < array.length){

            Object[] outArray = new Object[cc];

            for(int i = 0; i < cc; ++i){
                outArray[i] = array[i];
            }

            return outArray;

        }

        return array;
    }

    @Override
    public <E> E[] toArray(E[] a) {
        LinkedIterator<T> iterator = this.iterator();

        int cc = 0;

        while(iterator.hasNext()){

            if(cc >= a.length){
                break;
            }
            a[cc++] = (E)iterator.next();
        }

        if(cc < a.length){

            E[] outArray = (E[]) new Object[cc];

            for(int i = 0; i < cc; ++i){
                outArray[i] = a[i];
            }

            return outArray;

        }

        return a;
    }

    @Override
    public void addFirst(T t) {

        size++;

        LinkedElement<T> element = new LinkedElement<T>(t);

        if(first == null){

            synchronized (lockFirst){
                synchronized (lockLast){
                    first = element;
                    last = element;
                }
            }

        }else{

            synchronized(lockFirst){

                element.next = first;
                first.previous = element;

                first = element;
            }

        }




    }

    @Override
    public void addLast(T t) {
        size++;

        LinkedElement<T> element = new LinkedElement<T>(t);

        if(last == null){

            synchronized (lockFirst){
                synchronized (lockLast){
                    first = element;
                    last = element;
                }
            }

        }else{

            synchronized(lockLast){

                element.previous = last;
                last.next = element;

                last = element;
            }

        }
    }

    @Override
    public boolean offerFirst(T t) {
        addFirst(t);
        return true;
    }

    @Override
    public boolean offerLast(T t) {
        addLast(t);
        return true;
    }

    @Override
    public T removeFirst() {

        T result;

        if(first == last){

            synchronized (lockFirst){
                synchronized (lockLast){
                    result = first.payload;

                    first = null;
                    last = null;
                }
            }

        }else{
            synchronized (lockFirst) {
                result = first.payload;

                first.next.previous = null;
                first = first.next;

            }
        }



        --size;

        return result;
    }

    @Override
    public T removeLast() {
        T result;

        if(first == last){

            synchronized (lockFirst){
                synchronized (lockLast){
                    result = last.payload;

                    first = null;
                    last = null;
                }
            }

        }else{
            synchronized (lockLast) {
                result = last.payload;

                last.previous.next = null;
                last = last.previous;

            }
        }



        --size;

        return result;
    }

    @Override
    public T pollFirst() {
        if(first == null){
            return null;
        }

        return removeFirst();
    }

    @Override
    public T pollLast() {
        if(last == null){
            return null;
        }

        return removeLast();
    }

    @Override
    public T getFirst() {
        return first.payload;
    }

    @Override
    public T getLast() {
        return last.payload;
    }

    @Override
    public T peekFirst() {
        if(first == null){
            return null;
        }

        return first.payload;
    }

    @Override
    public T peekLast() {
        if(last == null){
            return null;
        }

        return last.payload;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {

        LinkedIterator<T> iterator = iterator();

        while(iterator.hasNext()){

            T next = iterator.current.payload;

            if(o == null){
               if(next == null) {

                   remove(iterator.current);
                   return true;

               }
            }else{

                if(o.equals(next)){
                    remove(iterator.current);
                    return true;
                }

            }

            iterator.next();

        }


        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        LinkedIterator<T> iterator = descendingIterator();

        while(iterator.hasNext()){

            T next = iterator.current.payload;

            if(o == null){
                if(next == null) {

                    remove(iterator.current);
                    return true;

                }
            }else{

                if(o.equals(next)){
                    remove(iterator.current);
                    return true;
                }

            }

            iterator.next();

        }


        return false;
    }

    @Override
    public boolean add(T t) {
        addLast(t);
        return true;
    }

    @Override
    public boolean offer(T t) {
        offerLast(t);
        return true;
    }

    @Override
    public T remove() {
        return removeFirst();
    }

    @Override
    public T poll() {
        return pollFirst();
    }

    @Override
    public T element() {
        return getFirst();
    }

    @Override
    public T peek() {
        return peekFirst();
    }

    @Override
    public void push(T t) {
        addFirst(t);
    }

    @Override
    public T pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        synchronized (lockFirst){
            synchronized (lockLast){

                first = null;
                last = null;

            }
        }
    }

    @Override
    public T get(int index) {

        Iterator<T> iterator = iterator();

        int cc = 0;
        while(iterator.hasNext()){

            T element = iterator.next();

            if(cc == index){
                return element;
            }


            ++cc;
        }

        throw new IndexOutOfBoundsException();

    }

    @Override
    public T set(int index, T element) {

        LinkedIterator<T> iterator = iterator();

        int cc = 0;
        while(iterator.hasNext()){


            if(cc == index){
                T current = iterator.current.payload;
                iterator.current.payload = element;
                return current;
            }

            iterator.next();
            ++cc;
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public void add(int index, T element) {

        if(index == 0){
            addFirst(element);
            return;
        }
        if(index == size){
            addLast(element);
            return;
        }



        LinkedIterator<T> iterator = iterator();

        int cc = 0;
        while(iterator.hasNext()){


            if(cc == index){

                if(iterator.current.previous == null){
                    addFirst(element);
                    return;
                }

                if(iterator.current.next == null){
                    addLast(element);
                    return;
                }

                LinkedElement<T> insert = new LinkedElement<T>(element);
                insert.next = iterator.current;
                insert.previous = iterator.current.previous;

                iterator.current.previous.next = insert;
                iterator.current.previous = insert;
                return;


            }

            iterator.next();
            ++cc;
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public T remove(int index) {

        LinkedIterator<T> iterator = iterator();

        int cc = 0;
        while(iterator.hasNext()){


            if(cc == index){
                T current = iterator.current.payload;
                remove(iterator.current);
                return current;
            }

            iterator.next();
            ++cc;
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public int indexOf(Object o) {
        LinkedIterator<T> iterator = iterator();

        int cc = 0;
        while(iterator.hasNext()){

            if(o == null){
                if(iterator.current.payload == null){
                    return cc;
                }
            }else{
                if(o.equals(iterator.current.payload)){
                    return cc;
                }
            }


            iterator.next();
            ++cc;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        LinkedIterator<T> iterator = descendingIterator();

        int cc = size - 1;
        while(iterator.hasNext()){

            if(o == null){
                if(iterator.current.payload == null){
                    return cc;
                }
            }else{
                if(o.equals(iterator.current.payload)){
                    return cc;
                }
            }


            iterator.next();
            --cc;
        }

        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return iterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException();
        }

        LinkedIterator<T> iterator = iterator();

        int cc = 0;
        while(iterator.hasNext()){


            if(cc == index){
                break;
            }


            iterator.next();
            ++cc;
        }

        return iterator;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {

        if(fromIndex < 0 || toIndex > size || fromIndex > toIndex){
            throw new IndexOutOfBoundsException();
        }

        Linked linked = new Linked();


        LinkedIterator<T> iterator = iterator();

        int cc = 0;
        while(iterator.hasNext()){

            if(cc == fromIndex){
                linked.first = iterator.current;
            }

            if(cc == toIndex - 1){
                linked.last = iterator.current;
                break;
            }


            iterator.next();
            ++cc;
        }

        return linked;
    }



    private void remove(LinkedElement<T> element){

        if(element.previous == null){
            removeFirst();
            return;
        }

        if(element.next == null){
            removeLast();
            return;
        }

        element.previous.next = element.next;
        element.next.previous = element.previous;

    }
}
