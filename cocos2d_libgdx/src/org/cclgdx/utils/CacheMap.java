package org.cclgdx.utils;

import java.util.HashMap;

import org.cclgdx.protocols.CacheMapProtocol;

import com.badlogic.gdx.utils.Disposable;

public abstract class CacheMap<T extends Disposable> implements CacheMapProtocol<T> {
	private final HashMap<String, T> cacheMap;

	protected CacheMap() {
		cacheMap = new HashMap<String, T>();
	}

	@Override
	public T get(String key) {
		if (cacheMap.containsKey(key)) {
			return cacheMap.get(key);
		}

		T t = create(key);
		cacheMap.put(key, t);
		return t;
	}

	@Override
	public boolean put(String key, T t) {
		if (cacheMap.containsKey(key)) {
			return false;
		}

		cacheMap.put(key, t);
		return true;
	}

	@Override
	public boolean containsKey(String key) {
		return cacheMap.containsKey(key);
	}

	public HashMap<String, T> getCacheMap() {
		return cacheMap;
	}

	@Override
	public void clear() {
		for (String key : cacheMap.keySet()) {
			T t = cacheMap.get(key);
			t.dispose();
		}
		cacheMap.clear();
	}

	@Override
	public int size() {
		return cacheMap.size();
	}

	@Override
	public void remove(String key) {
		if (cacheMap.containsKey(key)) {
			T t = cacheMap.get(key);
			t.dispose();
			cacheMap.remove(key);
		}
	}
}
