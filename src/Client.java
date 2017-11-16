import java.io.IOException;
import java.util.List;

public interface Client {
	List<String> sendMessage(String message) throws IOException;
}
