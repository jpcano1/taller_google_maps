package model.data_structures;

import java.util.Iterator;

/**
 * @author Juan Pablo Cano
 * @param <T> The generic type
 */
public class Stack<T extends Comparable<T>>
{
    /**
     * @author Juan Pablo Cano
     * The Node class
     * @param <T> The generic Type
     */
    @SuppressWarnings("hiding")
    public class Node<T extends Comparable<T>>
    {
        Node<T> next;
        T element;

        public Node(T pElement)
        {
            next = null;
            element = pElement;
        }

        public Node<T> getNext()
        {
            return next;
        }

        public T getElement()
        {
            return element;
        }

        public void setNext(Node<T> pNext)
        {
            next = pNext;
        }

        public void setElement(T pElement)
        {
            element = pElement;
        }
    }

    private Node<T> head;

    private int size;

    public Stack()
    {
        head = null;
        size = 0;
    }

    public Node<T> getHead()
    {
        return head;
    }

    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            Node<T> actual = head;

            @Override
            public boolean hasNext()
            {
                return actual != null;
            }

            @Override
            public T next()
            {
                if(hasNext())
                {
                    T data = actual.getElement();
                    actual = actual.getNext();
                    return data;
                }
                return null;
            }
        };
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return size;
    }

    public void push(T t)
    {
        try
        {
            Node<T> nuevo = new Node<T>(t);
            if(head != null)
            {
                nuevo.setNext(head);
            }
            head = nuevo;
            size++;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public T pop()
    {
        if(head != null)
        {
            T actual = head.getElement();
            head = head.getNext();
            size--;
            return actual;
        }
        return null;
    }
}

