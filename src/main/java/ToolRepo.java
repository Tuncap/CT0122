import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ToolRepo {
    private final Map<String, Tool> toolMap = new HashMap<>();

    ToolRepo() {
        try{
            loadFile();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    private void loadFile() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("/tools.csv").toURI());
        try(Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                String[] details = line.split("\\s*,\\s*", 3);
                String key = getProcessedString(details[0]).toUpperCase();
                toolMap.put(key, new Tool(
                        getProcessedString(details[0]).toUpperCase(),
                        getProcessedString(details[1]),
                        getProcessedString(details[2])));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Tool> getToolMap() {
        return toolMap;
    }

    public Tool getToolByCode(String code){
        return toolMap.get(code.toUpperCase());
    }

    private String getProcessedString(String value){
        return value.replace("\uFEFF", "").trim();
    }
}

