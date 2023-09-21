import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;

/**
 * Classe App - GuessTheWord
 * Classe principal do programa que funciona como um jogo de adivinhação de palavras.
 * @author ricardo.rossa@edu.pucrs.br
 * @version 21/09/2023 -  Version GTG 0.1.
 */

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        // color codes
        String blue = "\u001B[34m";
        String reset = "\u001B[0m";
        String red = "\u001B[31m";
        String yellow = "\u001B[33m";
        String green = "\u001B[32m";

        // gera as listas
        ArrayList<String> answersList = new ArrayList<String>();
        ArrayList<String> validGuessesList = new ArrayList<String>();

        // gera os paths
        Path validAnswersPath = Paths.get("C:\\Users\\meu\\IdeaProjects\\GuessTheWord\\src\\valid_answers.csv");
        Path validGuessesPath = Paths.get("C:\\Users\\meu\\IdeaProjects\\GuessTheWord\\src\\valid_guesses.csv");


        // respostas carregando
        try(BufferedReader br = Files.newBufferedReader(validAnswersPath))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String upperCaseLine = line.toUpperCase();
                answersList.add(upperCaseLine);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        // "achados" carregando
        try(BufferedReader br = Files.newBufferedReader(validGuessesPath))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String upperCaseLine = line.toUpperCase();
                validGuessesList.add(upperCaseLine);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }


        // cria nova palavra ja usando random e seta ela como a hiddenWord
        Word wordSet = new Word(answersList);
        String hiddenWord = wordSet.getWord();

        // loop pra fazer o textinho de boas vindas colorido.
        StringBuilder welcomeText = new StringBuilder();
        String text = "GuessTheWord!";
        welcomeText.append(yellow).append("Welcome to ").append(reset);
        for(int i = 0; i < text.length(); i++)
        {
            if(i % 2 == 0)
            {
                welcomeText.append(green).append(text.charAt(i)).append(reset);
            }
            else
            {
                welcomeText.append(red).append(text.charAt(i)).append(reset);
            }
        }
        System.out.println(welcomeText);
        Thread.sleep(500);

        // Textinho depois do boas vindas, pra dar conteudo pro usuario
        System.out.println("You Have 5 Guesses to get the correct answer!");
        Thread.sleep(1200);
        System.out.println("Word Generated!");
        System.out.println();

        // variavel guess - quantidade de guesses q o user fez // variavel n - controle para printar as linhas de quantas guesses faltam // boolean found - se acertar, a palavra fica true
        int guess = 0;
        int n = 4;
        boolean found = false;

        // array de Strings que guarda os "chutes" do usuario
        String[] coloredHolder = new String[5];

        // variavel de input
        String word = "";


        // Aqui se le o input do usuario com o BufferedReader e o InputStream e checa se o input é valido (ce ta no csv de inputs validos). Se nao estiver, joga uma excecao.
        BufferedReader inputReader = new BufferedReader((new InputStreamReader(System.in)));
     do {
            try
            {
                // while loop para caso a palavra seja valida, então é executado o break; caso a palavra seja invalida, o "throw" pula direto para o codigo do catch e depois para o inicio do loop para testar a condição novamente.
                while(true)
                {
                    System.out.println("=====================================================");
                    System.out.print("Guess a Word: ");
                    Thread.sleep(500);
                    word = inputReader.readLine().trim().toUpperCase();

                        if (!validGuessesList.contains(word))
                        {
                            // Se coloca word = ""; como vazio para que a frente do codigo seja checado esta condicao. caso word esteja vazia o loop que controla as guesses passa direto, sem "contar" a guess nem contar a palavra (pois era invalida)
                            word = "";
                            throw new InvalidGuessInputException("Invalid Guess - This Word is Not Valid!");
                        }
                        break;

                }
            }
            // Excecao personalizada que manda uma uma mensagem pedindo pro usuario colocar novamente uma palavra pois a outra era invalida
            catch (InvalidGuessInputException e)
            {
                System.out.println(e.getMessage());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

         // condicao que confirma se a palavra é valida ou nao. Caso esteja vazia ""; é sinal de que a palavra ja foi checada como invalida e deve-se apenas passar pelo codigo sem executar nada e voltar para o início do loop (do...)
         if(word.isEmpty())
         {
             continue;
         }

         // array booleano que armazena os caracteres corretos. aramzena 26 caracteres, o alfabeto completo.
         boolean[] correctChars = new boolean[26];
         StringBuilder coloredWord = new StringBuilder();
         for (int i = 0; i < word.length(); i++) {
             char currentChar = word.charAt(i);
             char hiddenChar = hiddenWord.charAt(i);

             if (currentChar == hiddenChar)
             {
                 // Aqui caso true, se insere no final da SB coloredWord a cor verde, o currentChar, e o reset.
                 coloredWord.append(green).append(currentChar).append(reset);
                 // Aqui é adicionado o valor true para o caractere na posição dele mesmo no alfabeto utilizando os valores ASCII a subtração entre o currentChar e 'A'.
                 // O valor resultante sempre será a posição correta, evitando repetidos pois eles sempre irão cair na mesma posição "se substituindo", assim  mantendo-se únicos.
                 correctChars[currentChar - 'A'] = true;
             }
             else
             {
                 // Caso o currentChar não esteja presente na array de caracteres corretos e se ele ESTIVER na palavra a ser descoberta, ele é marcado como "à descobrir" -> amarelo.
                 if (!correctChars[currentChar - 'A'] && InWord(i, currentChar, hiddenWord))
                 {
                     // Aqui caso true, se insere no final da SB coloredWord a cor amarela, o currentChar, e o reset.
                     coloredWord.append(yellow).append(currentChar).append(reset);
                 }
                 else
                 {
                     // Aqui se insere no final da SB coloredWord a cor vermelha, o currentChar, e o reset.
                     coloredWord.append(red).append(currentChar).append(reset);
                 }
             }
         }

         // Adiciona-se a SB coloredWord com as cores setadas na array que armazena cadda palavra "chutada" em uma posição.
         coloredHolder[guess] = String.valueOf(coloredWord);

         // Caso a palavra "chutada" seja a hiddenWord (palavra escondida) o programa printa uma parabenização, seta found como true e sai do loop, terminando sua execução.
         if(word.equals(hiddenWord))
         {
             System.out.println("=====================================================");
             System.out.println(coloredHolder[guess]);
             System.out.println("Congratulations! You guessed the correct word!");
             System.out.println("Guess Attempts: " + guess);
             found = true;
             break;
         }

         //Método que printa as palavras no tipo StringBuilder coloridas
         printGuess(coloredHolder);

         //Método que printa o numero de linhas para indicar quantas guesses o usuario ainda tem.
         printLines(n);

         // decrementa o numero de linhas a serem printadas, pois a guess ja foi processada e agora sera uma nova guess.
         n--;

         // aumenta o numero de "guess" indicando que o usuario gastou mais uma tentativa, condicao essencial para o do-while loop.
         guess++;
     } while(guess < 5);

     // Caso o usuário não acerte a palavra, o boolean found continuará como foi inicializado, false. Portanto, printa uma mensagem dizendo que ele não conseguiu.
     if(!found)
     {
         System.out.println("=====================================================");
         System.out.println("Good Luck Next Time!");
         StringBuilder coloredWord = new StringBuilder();
         coloredWord.append(red).append(hiddenWord).append(reset);
         System.out.println("Your Guesses did not match the Word: " + coloredWord);

     }

    }

    public static boolean charAlreadyAppeared (int index, StringBuilder word)
    {
       for(int i = 0; i < index; i++)
       {
           if(word.charAt(i) == word.charAt(index))
           {
               return true;
           }
       }
       return false;
    }

    public static int charAlreadyAppearedIndex (int index, String word)
    {
        for(int i = 0; i < index; i++)
        {
            if(word.charAt(i) == word.charAt(index))
            {
                return i;
            }
        }
        return -1;
    }

    public static boolean InWord(int index, char currentChar, String hiddenWord)
    {
        {
           for(int j = 0; j < hiddenWord.length(); j++)
           {
               if(currentChar == hiddenWord.charAt(j) && index != j)
               {
                   return true;
               }
           }
        }
        return false;
    }

    public static void printLines(int n)
    {
        for(int i = 0; i < n; i++)
        {
            System.out.println("_____");
        }
    }
    public static void printGuess(String[] w)
    {
        for(int i = 0; i < w.length; i++)
        {
            if(w[i] != null)
            {
                System.out.println(w[i]);
            }
        }
    }

}