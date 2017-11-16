import java.util.HashMap;

public class KeyValueStore {
	HashMap<String, String> map;

	public KeyValueStore() {
		map = new HashMap<String, String>();
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

	public HashMap<String, String> store() {
		return map;
	}

}
