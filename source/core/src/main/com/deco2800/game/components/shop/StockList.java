package com.deco2800.game.components.shop;

import com.deco2800.game.components.shop.artefacts.Artefact;

public class StockList {
    public class Node {
        Artefact artefact;
        Node next;
        Node prev;

        public Node(Artefact artefact) {
            this.artefact = artefact;
        }
    }

    public Node head = null;
    public Node tail = null;

    public void add(Artefact artefact) {
        Node newNode = new Node(artefact);
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
