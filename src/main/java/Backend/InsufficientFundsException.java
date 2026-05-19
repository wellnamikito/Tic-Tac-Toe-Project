package Backend;

/**
 • Исключение, которое выбрасывается, если у игрока не хватает денег.
 • Фронтенд: Обязательно ловите эту ошибку в try-catch и показывайте текст ошибки игроку.
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
