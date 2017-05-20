package pl.gumyns.sample.java;

import pl.gumyns.sample.java.generated.Config;

public class Main {
    public static void main(String[] args) {
        System.out.println(Config.message);
        if (Config.showAnswer) {
            System.out.println(Config.answer);
        }
    }
}
