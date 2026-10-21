package validations;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PersonValidationTest {
    static PersonValidation validator=new PersonValidation();

    @Nested
    class NameValidationTest{
        @ParameterizedTest
        @MethodSource("invalidNameInputs")
        @DisplayName("Throws Exception when name is null or blank")
        void rejectNullEmptyName(String name){
            IllegalArgumentException ex= assertThrows(IllegalArgumentException.class,
                    ()-> PersonValidation.validateName(name));
            assertEquals("Name can not be empty.",ex.getMessage());
        }
        private static Stream<String> invalidNameInputs(){
            return Stream.of(null,"");
        }

        @ParameterizedTest
        @ValueSource(strings={
                "Aman@10",
                "Ri#ya",
                "%Himanshu",
                "*aman."
        })
        @DisplayName("Throws exception when name contains invalid characters")
        void rejectInvalidName(String name){
            assertEquals("Name must only contains a~z, A~Z or .(dot)",
                    assertThrows(IllegalArgumentException.class,
                            ()-> PersonValidation.validateName(name)).getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings={
                "Aman",
                "Himanshu Kumar",
                "Mrs. Riya Sharma"
        })
        void acceptValidName(String name){
            assertDoesNotThrow(()-> PersonValidation.validateName(name));
        }
    }

    @Nested
    class PhoneNumberValidation{
        @ParameterizedTest
        @MethodSource("invalidPhoneNoInputs")
        @DisplayName("Throws Exception when phone no is null or blank")
        void rejectNullEmptyPhoneNo(String PhoneNo){
            IllegalArgumentException ex= assertThrows(IllegalArgumentException.class,
                    ()-> PersonValidation.validatePhoneNo(PhoneNo));
            assertEquals("Phone No can not be empty.",ex.getMessage());
        }
        private static Stream<String> invalidPhoneNoInputs(){
            return Stream.of(null,"");
        }

        @ParameterizedTest
        @ValueSource(strings={
                "12345678901",
                "123456789",
        })
        @DisplayName("Throws exception when length of phone no is not exactly 10")
        void rejectInvalidPhoneNoLength(String phoneNo){
            assertEquals("Phone No must be 10 digits long.",
                    assertThrows(IllegalArgumentException.class,
                            ()->PersonValidation.validatePhoneNo(phoneNo))
                            .getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings={
                "123456789@",
                "12345a6789",
                "12A3456789",
                "12345 6789",
                "123-456789"
        })
        @DisplayName("Throws exception when phone no. conatins non-numeric characters")
        void rejectPhoneNoContainingNonNumericCharacters(String phoneNo){
            assertEquals("Phone No must only contains digits",
                    assertThrows(IllegalArgumentException.class,
                            ()->PersonValidation.validatePhoneNo(phoneNo))
                            .getMessage());
        }

        @Test
        void acceptValidPhoneNo() {
            assertDoesNotThrow(()->PersonValidation.validatePhoneNo("1234567895"));
        }
    }

    @Nested
    class BloodGroupValidation {
        @ParameterizedTest
        @ValueSource(strings={
                "C+",
                "C-",
                "AB",
                "O",
                "A+1",
                "a+",
                "-A",
                "+O"
        })
        @DisplayName("Throws exception when blood group is invalid")
        void rejectInvalidBloodGroup(String bloodGroup) {
            assertEquals("Invalid Blood Group",
                    assertThrows(IllegalArgumentException.class,
                            ()->PersonValidation.validateBloodGroup(bloodGroup))
                            .getMessage());
        }

        @Test
        void acceptValidBloodGroup() {
            assertDoesNotThrow(()->PersonValidation.validateBloodGroup("A+"));
        }
    }

    @Nested
    class GenderValidation {
        @ParameterizedTest
        @ValueSource(chars = {
                'X',
                'x',
                'f',
                '1',
                '@'
        })
        @DisplayName("Throws exception when gender is invalid")
        void rejectInvalidGender(char gender) {
            assertEquals("Invalid Gender. Use 'M' for Male, 'F' for Female and 'O' for Other.",
                    assertThrows(IllegalArgumentException.class,
                            ()->PersonValidation.validateGender(gender))
                            .getMessage());
        }

        @ParameterizedTest
        @ValueSource(chars={'M','F','O'})
        void acceptValidGender(char gender) {
            assertDoesNotThrow(()->PersonValidation.validateGender(gender));
        }
    }
}
