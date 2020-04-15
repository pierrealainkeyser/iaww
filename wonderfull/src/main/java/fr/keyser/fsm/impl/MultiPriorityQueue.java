package fr.keyser.fsm.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.TreeMap;

public class MultiPriorityQueue<T> {

	private NavigableMap<Integer, Queue<T>> data = new TreeMap<>();

	public void add(T object) {
		add(0, object);
	}

	public void add(int priority, T object) {
		data.computeIfAbsent(priority, k -> new LinkedList<>()).add(object);
	}

	public T remove() {
		Iterator<Queue<T>> it = data.values().iterator();

		Queue<T> current = it.next();
		T value = current.remove();
		if (current.isEmpty())
			it.remove();
		return value;
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

}
