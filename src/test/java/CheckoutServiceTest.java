import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(PowerMockRunner.class)
public class CheckoutServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("Should throw error for renting under 1 day.")
    void testProcessWithZeroDaysRented() {
        CheckoutRequest request = new CheckoutRequest("CHNS", 0, 0, LocalDate.now());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.process(request);
        });
        Assertions.assertEquals("0 day(s) to rent is invalid.  Days to rent equipment must be greater than 0.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw error for discount being outside of 0 - 100.")
    void testProcessWithOutOfRangeDiscount() {
        CheckoutRequest request = new CheckoutRequest("CHNS", 1, 101, LocalDate.now());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.process(request);
        });
        Assertions.assertEquals("101%  is out of range.  Discount must within 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Should process checkout request without error")
    void testProcessWithOutError() {
        RentalAgreement rentalAgreement = new RentalAgreement("JAKR", 5, 100, LocalDate.of(2015, 3, 9));
        rentalAgreement.print();
        assertEquals("Rental Agreement\n" +
                "Tool code: JAKR\n" +
                "Tool type:  Jackhammer\n" +
                "Tool brand:  Ridgid\n" +
                "Rental days:  5\n" +
                "Check out date: 3/9/15\n" +
                "Due date: 3/14/15\n" +
                "Daily rental charge: $2.99\n" +
                "Charge days: 5\n" +
                "Pre-discount charge: $14.95\n" +
                "Discount percent: 100%\n" +
                "Discount amount: $14.95\n" +
                "Final charge: $0.00\n",  outputStreamCaptor.toString());
    }


}
