package com.deco2800.game.components.shop;

class Node<T> {
    T t;
    Node<T> next;
    Node<T> prev;

    public Node(T t) {
        this.t = t;
    }
}

class CircularLinkedList<T> {

    public Node<T> head = null;
    public Node<T> tail = null;

    public void add(T t) {
        Node<T> newNode = new Node<T>(t);
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
            newNode.prev = tail;
        } else {
            newNode.next = head;
            head.prev = newNode;
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
    }
}
