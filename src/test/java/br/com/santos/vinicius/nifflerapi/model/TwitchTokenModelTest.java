package br.com.santos.vinicius.nifflerapi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(TwitchTokenModel.class)
public class TwitchTokenModelTest {

    @Test
    public void it_Should_test_model() {
        TwitchTokenModel tokenModel = new TwitchTokenModel();
        tokenModel.setToken_type("TYPE TEST");
        tokenModel.setAccess_token("ACCESS TOKEN TEST");
        tokenModel.setExpires_in(544444444L);

        assertEquals("TYPE TEST", tokenModel.getToken_type());
        assertEquals("ACCESS TOKEN TEST", tokenModel.getAccess_token());
        assertEquals(544444444L, tokenModel.getExpires_in().longValue());
    }

}
