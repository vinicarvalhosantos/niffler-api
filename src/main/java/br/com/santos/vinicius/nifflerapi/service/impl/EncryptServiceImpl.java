package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.service.EncryptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptServiceImpl implements EncryptService {

    @Value("${encryptor.secret.key}")
    String encryptorSecretKey;
    final private int iterations = 200000;
    final private int hashWidth = 256;


    @Override
    public String encrypt(String data) {
        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(encryptorSecretKey, iterations, hashWidth);
        passwordEncoder.setEncodeHashAsBase64(true);

        return passwordEncoder.encode(data);
    }

    @Override
    public boolean matches(String data, String encodedData) {
        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(encryptorSecretKey, iterations, hashWidth);
        passwordEncoder.setEncodeHashAsBase64(true);
        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(encodedData))
            return false;

        return passwordEncoder.matches(data, encodedData);
    }
}
