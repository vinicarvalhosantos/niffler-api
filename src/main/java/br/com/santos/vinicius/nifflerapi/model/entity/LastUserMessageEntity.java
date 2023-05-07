package br.com.santos.vinicius.nifflerapi.model.entity;

import br.com.santos.vinicius.nifflerapi.util.StringUtil;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@DynamoDBTable(tableName = "last_user_message")
public class LastUserMessageEntity {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id = UUID.randomUUID().toString();

    @DynamoDBAttribute(attributeName = "user_id")
    private Long userId;

    @DynamoDBAttribute(attributeName = "last_message")
    private String lastMessage;

    public LastUserMessageEntity(Long userId, String lastMessage) {
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public double compareMessages(String recentMessage) {
        if (recentMessage.length() > this.lastMessage.length()) {
            return StringUtil.similarity(recentMessage, this.lastMessage);
        }

        return StringUtil.similarity(this.lastMessage, recentMessage);
    }
}
