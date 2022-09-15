package com.deco2800.game.components.storyline;

/**
 * The functionality is inspired from Satya Bhasale work in /shop/CircularLinkedList.java
 */

class Node<F> {
    F f;
    Node<F> next;
    public Node(F f) {
        this.f = f;
    }
}

class StoryLinkedList<F> {

    Node<F> header = null;

    public void add(F f) {
        Node<F> newNode = new Node<>(f);
        newNode.next = null;

        if (header == null) {
            header = newNode;
        }
        else {
            Node<F> n = header;
            while ( n.next != null) {
                n = n.next;
            }
            n.next = newNode;
        }
    }
}

