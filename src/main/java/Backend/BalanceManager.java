package Backend;

import java.util.*;

/**
 * Класс для управления кошельками пользователей.
 */
public class BalanceManager {
    private final Map<String, Map<CurrencyType, Double>> balances = new HashMap<>();

    // Фронтенд: вызывайте, чтобы показать текущий баланс игрока
    public double getBalance(String userId, CurrencyType type) {
        return balances.getOrDefault(userId, new EnumMap<>(CurrencyType.class))
                .getOrDefault(type, 0.0);
    }

    public void addFunds(String userId, CurrencyType type, double amount) {
        balances.computeIfAbsent(userId, k -> new EnumMap<>(CurrencyType.class))
                .merge(type, amount, Double::sum);
    }

    public void spend(String userId, CurrencyType type, double amount) throws InsufficientFundsException {
        double current = getBalance(userId, type);
        if (current < amount) {
            // Фронтенд получит это сообщение и сможет вывести его в UI
            throw new InsufficientFundsException("Недостаточно средств. Нужно: " + amount + ", у вас: " + current);
        }
        balances.get(userId).put(type, current - amount);
    }
}

