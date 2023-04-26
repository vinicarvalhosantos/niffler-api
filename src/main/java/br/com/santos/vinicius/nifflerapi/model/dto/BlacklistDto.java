package br.com.santos.vinicius.nifflerapi.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlacklistDto {
    private String username;

    public String getUsername() {
        return username;
    }
}
