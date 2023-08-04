package br.com.santos.vinicius.nifflerapi.service;

import org.springframework.stereotype.Service;

@Service
public interface EncryptService {

    String encrypt(String data);

    boolean matches(String data, String encodedData);

}
