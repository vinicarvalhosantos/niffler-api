package br.com.santos.vinicius.nifflerapi.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("user_id")
    private Long userId;

    public boolean isValidDto() {
        return !StringUtils.isBlank(this.displayName) || this.isValidUserId();
    }

    public boolean isValidUserId() {
        return this.userId != null && this.userId != 0;
    }
}
