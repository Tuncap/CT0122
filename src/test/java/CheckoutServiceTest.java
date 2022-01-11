import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckoutServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("Should throw error for renting under 1 day.")
    void testProcessWithZeroDaysRented() {
        CheckoutRequest request = new CheckoutRequest("CHNS", "1/10/22", 0, "0%");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.process(request);
        });
        Assertions.assertEquals("0 day(s) to rent is invalid.  Days to rent equipment must be greater than 0.", exception.getMessage());
    }

    @Test
    @DisplayName("Should process checkout request without error")
    void testProcessWithOutError() {
        CheckoutRequest request = new CheckoutRequest("JAKR", "3/9/15", 5, "100%");
        CheckoutService.process(request);
        assertEquals("Rental Agreement\n" +
                        "Tool code: JAKR\n" +
                        "Tool type: Jackhammer\n" +
                        "Tool brand: Ridgid\n" +
                        "Rental days: 5\n" +
                        "Check out date: 03/09/15\n" +
                        "Due date: 03/14/15\n" +
                        "Daily rental charge: $2.99\n" +
                        "Charge days: 4\n" +
                        "Pre-discount charge: $11.96\n" +
                        "Discount percent: 100%\n" +
                        "Discount amount: $11.96\n" +
                        "Final charge: $0.00\n",
                outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("1. Should throw error for test exceeding 100%")
    void testProcessWith101Discount() {
        CheckoutRequest request = new CheckoutRequest("JAKR", "9/3/15", 5, "101%");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.process(request);
        });
        Assertions.assertEquals("101%  is out of range.  Discount must within 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("2. Should print Rental Agreement with 10% discount and not charge holiday.")
    void testProcessWith10Discount() {
        CheckoutRequest request = new CheckoutRequest("LADW", "7/2/20", 3, "10%");
        CheckoutService.process(request);
        assertEquals("Rental Agreement\n" +
                "Tool code: LADW\n" +
                "Tool type: Ladder\n" +
                "Tool brand: Werner\n" +
                "Rental days: 3\n" +
                "Check out date: 07/02/20\n" +
                "Due date: 07/05/20\n" +
                "Daily rental charge: $1.99\n" +
                "Charge days: 2\n" +
                "Pre-discount charge: $3.98\n" +
                "Discount percent: 10%\n" +
                "Discount amount: $0.40\n" +
                "Final charge: $3.58\n", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("3. Should print Rental Agreement with 25% discount excluding weekends.")
    void testProcessWith25Discount() {
        CheckoutRequest request = new CheckoutRequest("CHNS", "7/2/15", 5, "25%");
        CheckoutService.process(request);
        assertEquals("Rental Agreement\n" +
                "Tool code: CHNS\n" +
                "Tool type: Chainsaw\n" +
                "Tool brand: Stihl\n" +
                "Rental days: 5\n" +
                "Check out date: 07/02/15\n" +
                "Due date: 07/07/15\n" +
                "Daily rental charge: $1.49\n" +
                "Charge days: 3\n" +
                "Pre-discount charge: $4.47\n" +
                "Discount percent: 25%\n" +
                "Discount amount: $1.12\n" +
                "Final charge: $3.35\n", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("4. Should print Rental Agreement without charging weekends and holiday in September.")
    void testProcessWith0Discount() {
        CheckoutRequest request = new CheckoutRequest("JAKD", "9/3/15", 6, "0%");
        CheckoutService.process(request);
        assertEquals("Rental Agreement\n" +
                "Tool code: JAKD\n" +
                "Tool type: Jackhammer\n" +
                "Tool brand: DeWalt\n" +
                "Rental days: 6\n" +
                "Check out date: 09/03/15\n" +
                "Due date: 09/09/15\n" +
                "Daily rental charge: $2.99\n" +
                "Charge days: 3\n" +
                "Pre-discount charge: $8.97\n" +
                "Discount percent: 0%\n" +
                "Discount amount: $0.00\n" +
                "Final charge: $8.97\n", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("5. Should print Rental Agreement without charging weekends and holiday in July.")
    void testProcessWith0DiscountFor9Days() {
        CheckoutRequest request = new CheckoutRequest("JAKR", "7/2/15", 9, "0%");
        CheckoutService.process(request);
        assertEquals("Rental Agreement\n" +
                "Tool code: JAKR\n" +
                "Tool type: Jackhammer\n" +
                "Tool brand: Ridgid\n" +
                "Rental days: 9\n" +
                "Check out date: 07/02/15\n" +
                "Due date: 07/11/15\n" +
                "Daily rental charge: $2.99\n" +
                "Charge days: 5\n" +
                "Pre-discount charge: $14.95\n" +
                "Discount percent: 0%\n" +
                "Discount amount: $0.00\n" +
                "Final charge: $14.95\n", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("6. Should print Rental Agreement with 50% discount and charge weekdays excluding 1 weekday for holiday.")
    void testProcessWith50DiscountFor4Days() {
        CheckoutRequest request = new CheckoutRequest("JAKR", "7/2/20", 4, "50%");
        CheckoutService.process(request);
        assertEquals("Rental Agreement\n" +
                "Tool code: JAKR\n" +
                "Tool type: Jackhammer\n" +
                "Tool brand: Ridgid\n" +
                "Rental days: 4\n" +
                "Check out date: 07/02/20\n" +
                "Due date: 07/06/20\n" +
                "Daily rental charge: $2.99\n" +
                "Charge days: 1\n" +
                "Pre-discount charge: $2.99\n" +
                "Discount percent: 50%\n" +
                "Discount amount: $1.50\n" +
                "Final charge: $1.49\n", outputStreamCaptor.toString());
    }

}
