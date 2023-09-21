/**
 * Classe de uma Exceção personalizada que retorna uma mensagem caso o input do usuário sejá inválido.
 */
public class InvalidGuessInputException extends Exception {

    public InvalidGuessInputException(String message)
    {
        super(message);
    }
}
