package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.service.EncryptService;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import org.bouncycastle.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class EncryptServiceImpl implements EncryptService {

    @Autowired
    AWSKMS awskmsClient;

    @Value("${aws.kms.key.id}")
    String keyId;

    @Override
    public String encrypt(String data) {
        ByteBuffer plainText = ByteBuffer.wrap(data.getBytes());

        EncryptRequest req = new EncryptRequest().withKeyId(keyId)
                .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256).withPlaintext(plainText);
        ByteBuffer cipherText = awskmsClient.encrypt(req).getCiphertextBlob();

        byte[] bytes = Base64.getEncoder().encode(cipherText.array());

        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public String decrypt(String data) {
        byte[] bytes = Base64.getDecoder().decode(data);

        ByteBuffer plainText = ByteBuffer.wrap(bytes);

        DecryptRequest dereq = new DecryptRequest().withKeyId(keyId)
                .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                .withCiphertextBlob(plainText);

        DecryptResult decryptResult = awskmsClient.decrypt(dereq);

        return StandardCharsets.UTF_8.decode(decryptResult.getPlaintext()).toString();
    }
}
