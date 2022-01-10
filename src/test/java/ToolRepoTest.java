import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ToolRepoTest {

    private ToolRepo toolRepo;

    @BeforeEach
    void initialize(){
        toolRepo = new ToolRepo();
    }

    @Test
    void testLoadingFile(){
        assertFalse(toolRepo.getToolMap().isEmpty());
    }

    @Test
    @DisplayName("Should return the matching tool stored in the resource file.")
    void testGetToolByCode(){
        final String chainsawByStihl = "CHNS";
        assertEquals(new Tool("CHNS", "Chainsaw", "Stihl"), toolRepo.getToolByCode(chainsawByStihl));
    }

}

