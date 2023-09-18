package br.com.santos.vinicius.nifflerapi.filter;

public class ExcludeZerosFilter {

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Integer)){
            return true;
        }

        int object = (int) obj;
        return object == 0;
    }
}
