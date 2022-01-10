import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ToolPricingRepo {

    private static final String YES = "YES";
    private final Map<String, ToolPricing> toolPricingMap = new HashMap<>();

    public ToolPricingRepo() {
        try{
            loadFile();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    private void loadFile() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("/tool_pricing.csv").toURI());
        try(Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                String[] details = line.split("\\s*,\\s*", 5);
                String key = getProcessedString(details[0]);
                toolPricingMap.put(key,  new ToolPricing(
                        new BigDecimal(details[1].trim().replaceAll("\\$", "")),
                        YES.equalsIgnoreCase(details[2].trim()),
                        YES.equalsIgnoreCase(details[3].trim()),
                        YES.equalsIgnoreCase(details[4].trim())));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ToolPricing getPricingByToolType(String toolType){
        return toolPricingMap.get(toolType);
    }

    public Map<String, ToolPricing> getPricingMap(){
        return toolPricingMap;
    }

    private String getProcessedString(String value){
        return value.replace("\uFEFF", "").trim();
    }
}
