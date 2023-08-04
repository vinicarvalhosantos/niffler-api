package br.com.santos.vinicius.nifflerapi.util;

import org.junit.Assert;
import org.junit.Test;

public class MessageUtilTest {

    @Test
    public void whenMessageIsPolite() {
        String message = "Bom dia";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "bodia";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "bodos";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Boa tarde";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Batarde";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Boa noite";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "banoite";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
    }

    @Test
    public void whenMessageIsPercentagePolite() {
        String message = "Bomdia";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Bodiaaa";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Boodiaa";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Batardee";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Baatardee";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Boom diaa chaat";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Booa tardee chaat";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Booa noite chaat";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "Ooi chat, boaa tardee pro ceis AYAYA";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
        message = "batardee batardee";
        Assert.assertTrue(MessageUtil.isMessagePolite(message));
    }

}
