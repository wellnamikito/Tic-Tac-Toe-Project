package Backend;

import java.util.*;

/**
 * BalanceManager — менеджер баланса пользователей.
 *
 * <p>📌 Отвечает за хранение и управление всеми валютами пользователя.</p>
 *
 * <p>Используется для:
 * <ul>
 *   <li>получения баланса</li>
 *   <li>начисления средств</li>
 *   <li>списания средств</li>
 * </ul>
 *
 * <p>📌 Поддерживаемые валюты описаны в {@link CurrencyType}.</p>
 *
 * <p>📡 Важно для фронтенда:
 * все операции списания могут выбрасывать {@link InsufficientFundsException},
 * которую необходимо обрабатывать и отображать пользователю.</p>
 */
public class BalanceManager {

    /**
     * Хранилище балансов пользователей.
     *
     * <p>Структура:
     * <ul>
     *   <li>key = userId</li>
     *   <li>value = карта валют и их значений</li>
     * </ul>
     */
    private final Map<String, Map<CurrencyType, Double>> balances = new HashMap<>();

    /**
     * Возвращает текущий баланс пользователя.
     *
     * <p>📌 Используется фронтендом для отображения UI кошелька.</p>
     *
     * @param userId ID пользователя
     * @param type тип валюты
     * @return текущий баланс (0.0 если не существует)
     */
    public double getBalance(String userId, CurrencyType type) {
        return balances.getOrDefault(userId, new EnumMap<>(CurrencyType.class))
                .getOrDefault(type, 0.0);
    }

    /**
     * Добавляет средства пользователю.
     *
     * <p>📌 Используется при:
     * <ul>
     *   <li>покупке монет</li>
     *   <li>наградах</li>
     *   <li>донате</li>
     * </ul>
     *
     * @param userId ID пользователя
     * @param type тип валюты
     * @param amount сумма для добавления
     */
    public void addFunds(String userId, CurrencyType type, double amount) {
        balances.computeIfAbsent(userId, k -> new EnumMap<>(CurrencyType.class))
                .merge(type, amount, Double::sum);
    }

    /**
     * Списывает средства у пользователя.
     *
     * <p>📌 Используется при покупках в магазине.</p>
     *
     * <p>⚠️ Может выбросить {@link InsufficientFundsException},
     * если недостаточно средств.</p>
     *
     * @param userId ID пользователя
     * @param type тип валюты
     * @param amount сумма списания
     * @throws InsufficientFundsException если баланс меньше требуемой суммы
     */
    public void spend(String userId, CurrencyType type, double amount)
            throws InsufficientFundsException {

        double current = getBalance(userId, type);

        if (current < amount) {
            throw new InsufficientFundsException(
                    "Недостаточно средств. Нужно: " + amount + ", у вас: " + current
            );
        }

        balances.get(userId).put(type, current - amount);
    }
}