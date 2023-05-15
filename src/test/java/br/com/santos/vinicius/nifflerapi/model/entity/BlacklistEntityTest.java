package br.com.santos.vinicius.nifflerapi.model.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BlacklistEntityTest {

    @Test
    public void it_should_test_entity(){
        BlacklistEntity blacklist = new BlacklistEntity("zvinniie", 55448L);
        UserEntity user = new UserEntity(55448L, "zvinniie", "zvinniie",
                0.0, 0.0);

        assertTrue(blacklist.equalsTwitchUser(user));
        assertTrue(blacklist.equalsTwitchUser(blacklist));
        assertFalse(blacklist.equalsTwitchUser(null));
    }


}
