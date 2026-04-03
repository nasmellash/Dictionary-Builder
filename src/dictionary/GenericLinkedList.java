package dictionary;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 *A generic doubly linked list implementation with forward and backward traversal
 * using a ListIterator. Elements can be added and removed from both the head
 * and tail of the list.
 * @param <T> the type of elements held in the list (generic)
 */
public class GenericLinkedList<T> implements Iterable<T> {
    private Node head;
    private Node tail;
    private int size;

    /**
     * An inner class, generic implementation of the ListIterator for the GenericLinkedList class.
     * The iterator contains the methods necessary for traversal through the list including
     * hasNext(), next(), hasPrevious(), and previous().
     * The class also supports removal of a data type T from the list.
     */
    private class GenericIterator implements ListIterator<T> {
        private Node currentNode = head;
        private Node lastReturnedNode = null;

        public boolean hasNext() { return currentNode != null; }
        public boolean hasPrevious() { return currentNode != head; }

        public T next() {
            if(!hasNext())
                throw new NoSuchElementException("No more elements left.");
            lastReturnedNode = currentNode;
            T val = currentNode.data;
            currentNode = currentNode.next;
            return val;
        }
        public T previous() {
            if(!hasPrevious())
                throw new NoSuchElementException("No element exists.");
            if(currentNode == null) { currentNode = tail;}
            else { currentNode = currentNode.prev; }

            lastReturnedNode = currentNode;
            return currentNode.data;
        }

        public void remove() throws IllegalStateException {
            if(lastReturnedNode == null)
                throw new IllegalStateException();

            if(currentNode == lastReturnedNode)
                currentNode = lastReturnedNode.next;


            if(lastReturnedNode.prev == null && lastReturnedNode.next == null) {
                head = null;
                tail = null;
            }
            else if (lastReturnedNode.prev == null) {
                head = lastReturnedNode.next;
                head.prev = null;
            }
            else if (lastReturnedNode.next == null){
                tail = lastReturnedNode.prev;
                tail.next = null;
            }
            else {
                lastReturnedNode.prev.next = lastReturnedNode.next;
                lastReturnedNode.next.prev = lastReturnedNode.prev;
            }
            size--;
            lastReturnedNode = null;
        }

        // Unsupported methods (for this project) found in ListIterator
        @Override
        public int nextIndex() { throw new UnsupportedOperationException(); }
        @Override
        public int previousIndex() { throw new UnsupportedOperationException(); }
        @Override
        public void set(T t) { throw new UnsupportedOperationException(); }
        @Override
        public void add(T t) { throw new UnsupportedOperationException(); }
    }

    /**
     * An inner class representing a single node.
     * Each node stores data and references to the next and previous node.
     */
    private class Node {
        T data;
        int count;
        Node next;
        Node prev;

        /**
         * Constructs a new Node with the provided data.
         * @param data the data stored in the node
         */
        Node(T data) {
            this.data = data;
            count++;
        }
    }

    /**
     * Constructor for the class, empty with no elements.
     */
    public GenericLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns a ListIterator over the elements in the list.
     * @return a ListIterator starting at the head (beginning)
     */
    @Override
    public ListIterator<T> iterator() {
        return new GenericIterator();
    }

    /**
     * Returns a ListIterator over the elements in the list.
     * Includes back and forth traversal as well as element removal.
     *
     * @return a ListIterator starting at the head (beginning)
     */
    public ListIterator<T> listIterator() {
        return new GenericIterator();
    }

    /**
     * Checks if the list is empty.
     * @return true if the list is empty, otherwise false
     */
    public Boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the list.
     * @return the number of elements in the list
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the element received matches any of the elements in the list.
     *
     * @param element the element being searched
     * @return true if the element is found, otherwise false
     */
    public Boolean contains(T element) {
        ListIterator<T> iterate = listIterator();
        for(int i = 0; i < size(); i++) {
            if(element.equals(iterate.next()))
                return true;
        }
        return false;
    }

    /**
     * Converts list into an array in the same order (head to tail).
     * @return an Object array of all elements found in the list
     */
    public Object toArray() {
        Object[] result = new Object[size];
        Node toArray = head;

        for(int i = 0; i < size; i++) {
            result[i] = toArray.data;
            toArray = toArray.next;
        }
        return result;
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns the element at a certain index.
     * @param index index of element being retrieved
     * @return the element at the param index
     */
    public T get(int index) {
        if(index >= size || index < 0)
            throw new IndexOutOfBoundsException("Index doesn't exist in list.");

        ListIterator<T> iterate = listIterator();
        T result = null;
        for(int i = 0; i <= index; i++) {
            result = iterate.next();
        }

        return result;
    }

    /**
     * Returns first element in list (head).
     * @return data stored at head
     */
    public T getFirst() { return head.data; }

    /**
     * Returns the last element in the list (tail).
     * @return data stored at tail
     */
    public T getLast()  { return tail.data; }

    /**
     * Inserts the element at the head.
     * @param data the element being added to the front of the list
     */
    public void addFirst(T data) {
        Node newHead = new Node(data);
        if(isEmpty()) {
            head = newHead;
            tail = newHead;
        } else {
            newHead.next = head;
            head.prev = newHead;
            head = newHead;
        }
        size++;
    }

    /**
     * Inserts the element at the tail.
     * @param data the element being added to the end of the list.
     */
    public void addLast(T data) {
        Node newTail = new Node(data);
        if(isEmpty()) {
            head = newTail;
        } else {
            newTail.prev = tail;
            tail.next = newTail;
        }
        tail = newTail;
        size++;
    }

    /**
     * Removes and returns the element at index passed as a param.
     * If beginning or end, uses removeFirst() or removeLast().
     * Otherwise, use iterator to find element.
     * @param index the index of the element being removed
     * @return the removed element
     * @throws IndexOutOfBoundsException if index is out of range (dependent on list size)
     */
    public T remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        if(index == 0) {
            return removeFirst();
        }
        if(index == (size - 1))
            return removeLast();

        ListIterator<T> iterate = listIterator();
        int i = 0;
        while(i <= index) {
            iterate.next();
            i++;
        }
        iterate.remove();
        return null;
    }

    /**
     * Removes the element passed as a param if present.
     * @param element the element being removed
     * @return true if element was removed or false if not
     */
    public Boolean remove(T element) {
        ListIterator<T> iterate = listIterator();
        while(iterate.hasNext()) {
            if(iterate.next().equals(element)) {
                iterate.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Removes and returns the first element in the list (head).
     * @return the element stored at the removed head node
     * @throws NoSuchElementException if list = empty
     */
    public T removeFirst() throws NoSuchElementException {
        T data = head.data;
        if(head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    /**
     * Removes and returns the last element in the list (tail).
     * @return the element stored at the removed tail node
     * @throws NoSuchElementException if list = empty
     */
    public T removeLast() throws NoSuchElementException {
        T data = tail.data;
        if(head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    public void increment(T element) {
        Node current = head;
        while(current!= null) {
            if(current.data.equals(element)) {
                current.count++;
                return;
            }
            current = current.next;
        }
    }

}
