package dictionary;

import org.junit.jupiter.api.Test;

import static dictionary.DictionaryBuilder.findClosetGaussianPrime;
import static dictionary.DictionaryBuilder.isPrime;
import static org.junit.jupiter.api.Assertions.*;

class DictionaryBuilderStudentTest {

    @Test
    public void testPrimalityAndCongruence() {
        // Test 1: Check if isPrime method works properly
        // using a random non-prime (composite) and prime number
        assertFalse(isPrime(4));
        assertTrue(isPrime(7));

        // Test 2: Check if findClosetGaussianPrime works
        // properly by testing 3000 (provided in instructions)
        // 3000 closet Gaussian prime is 3011
        assertEquals(3011, findClosetGaussianPrime(3000));

    }

}