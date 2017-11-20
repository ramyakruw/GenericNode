import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {
	ConcurrentHashMap<String, String> map;

	public KeyValueStore() {
		map = new ConcurrentHashMap<String, String>();
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public String get(String key) {
		return map.get(key);
	}

	public void delete(String key) {
		map.remove(key);
	}

	public ConcurrentHashMap<String, String> store() {
		return map;
	}

}
