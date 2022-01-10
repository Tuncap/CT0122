import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ToolPricingRepoTest {

    private ToolPricingRepo toolPricingRepo;

    @BeforeEach
    void initialize(){
        toolPricingRepo = new ToolPricingRepo();
    }


    @Test
    @DisplayName("Should successfully load resource file.")
    void testLoadingFile(){
        assertFalse(toolPricingRepo.getPricingMap().isEmpty());
    }
}
