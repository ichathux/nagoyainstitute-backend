package com.nagoyainstitute.backendnagoyainstitute.service;

import com.nagoyainstitute.backendnagoyainstitute.dto.RegisterRequest;
import com.nagoyainstitute.backendnagoyainstitute.dto.RegisterRequestInit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private String generatePassword() {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[8];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < 8; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        log.info("password generated :" + new String(password));
        return new String(password);
    }

    public RegisterRequest createRegisterRequest(RegisterRequestInit registerRequestInit) {
        return new RegisterRequest(
                registerRequestInit.getNic() ,
                registerRequestInit.getCity() ,
                generatePassword());

    }


}
