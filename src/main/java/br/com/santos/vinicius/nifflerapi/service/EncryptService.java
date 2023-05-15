package br.com.santos.vinicius.nifflerapi.service;

import org.springframework.stereotype.Service;

@Service
public interface EncryptService {

    String encrypt(String data);

    String decrypt(String data);

}
