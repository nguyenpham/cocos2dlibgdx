package org.cclgdx.protocols;

public interface CacheMapProtocol<T> {
	boolean containsKey(String key);

	T get(String key);

	boolean put(String key, T t);

	T create(String key);

	void clear();

	int size();

	void remove(String key);

}
