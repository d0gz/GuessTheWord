import java.util.ArrayList;

/**
 *  Classe Word - Esta classe cria um objeto "word" que é uma palavra. Word recebe um ArrayList de palavras e utiliza Math.Random para escolher uma palavra aleatoriamente.
 */
public class Word {

    String word;

    public Word()
    {
        word = "";
    }

    public Word(ArrayList<String> words)
    {
        int lower = 0;
        int upper = words.size() - 1;
        int range = (upper - lower);
        int i = (int) (Math.random() * range);
        word = words.get(i);
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }
}
