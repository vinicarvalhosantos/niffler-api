package br.com.santos.vinicius.nifflerapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class EmoteModel {

    private final List<String> writtenEmotes;

    private final int emotesNumber;

}
