import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RentalAgreementTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("Should log to console in the correct format.")
    void print(){
        RentalAgreement rentalAgreement = new RentalAgreement("JAKR", 5, 10, LocalDate.of(2015, 3, 9));
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
                "Discount percent: 10%\n" +
                "Discount amount: $1.50\n" +
                "Final charge: $13.45\n",  outputStreamCaptor.toString());
    }
}
