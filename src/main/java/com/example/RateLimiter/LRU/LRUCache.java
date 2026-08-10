package com.example.RateLimiter.LRU;

// LRU (Least Recently Use) is a small storage area that keeps the things you most use recently. Least recently used gets removed first.

/*
HashMap
→ Find the item quickly

Doubly Linked List
→ Keep track of usage order quickly

         HashMap
            ↓
       Find Node A
            ↓
     Doubly Linked List
            ↓
       Move A to front

0(1) get and put

synchronized protects linked list and hashmap***

 */

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LRUCache {

    private HashMap<String, Node> map = new HashMap<>();

    private int capacity;

    private Node head; // most recently used

    private Node tail; // least recently used

    public LRUCache(int capacity) {
        this.capacity = capacity;

        head = new Node("Head", "Head",0);
        tail = new Node("Tail", "Tail",0);

        head.next = tail;
        tail.prev = head;
    }

    public synchronized void  put(String key, String value, long ttlSeconds) {

        Node node = map.get(key);
        long expirationTime = Instant.now().getEpochSecond() + ttlSeconds;

        if(node != null) {

            node.value = value;
            node.expirationTime = expirationTime;

            //remove from current position
            removeNode(node);
            addToFront(node);

        }
        else{

            node = new Node(key, value, expirationTime);
            map.put(key, node);
            addToFront(node);

            if(map.size() > capacity) {

                Node leastRecentlyUsed = tail.prev;

               removeNode(leastRecentlyUsed);

                map.remove(leastRecentlyUsed.key);

            }
            
        }


    }

    public synchronized String get(String key) {

      Node node = map.get(key);

      if(node == null) {
          return null;
      }

      long currentTime = Instant.now().getEpochSecond();

      if(currentTime >= node.expirationTime) {

          removeNode(node);
          map.remove(key);
          return null;

      }

        //Remove node from current position
     removeNode(node);

      //Add node to front
     addToFront(node);

      return node.value;

    }

    private void removeNode(Node node){

        node.prev.next = node.next;
        node.next.prev = node.prev;

    }

    private void addToFront(Node node){

        node.prev = head;
        node.next = head.next;

        head.next.prev = node;
        head.next = node;

    }

    public synchronized int size() {
        return map.size();
    }


}

class Node{

    String key;
    String value;
    long expirationTime;


    Node prev;
    Node next;

    Node(String key, String value, long expirationTime) {
        this.key = key;
        this.value = value;
        this.expirationTime = expirationTime;
    }

}
