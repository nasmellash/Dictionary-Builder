package dictionary;

import org.junit.jupiter.api.Test;

import static dictionary.DictionaryBuilder.isPrime;
import static org.junit.jupiter.api.Assertions.*;

class DictionaryBuilderStudentTest {

    @Test
    public void testFindClosetGaussianPrime() {
        // Test 1: Check if isPrime method works properly
        // using a random non-prime (composite) and prime number
        assertFalse(isPrime(4));
        assertTrue(isPrime(7));

    }

}