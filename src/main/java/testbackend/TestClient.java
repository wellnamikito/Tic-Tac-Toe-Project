package testbackend;

import java.io.*;
import java.net.Socket;
import java.util.*;

import Backend.*;

/**

 * TestClient — тестовый клиент и встроенный тестовый фреймворк backend системы.

 *

 * <p>📌 Поддерживает два режима работы:

 *

 * <h2>1. AUTO режим (автотесты)</h2>

 * Запуск:

 * <pre>

 * java testbackend.TestClient auto

 * </pre>

 *

 * <p>В этом режиме выполняются автоматические тесты backend-логики:

 * <ul>

 *   <li>BalanceManager</li>

 *   <li>InventoryManager</li>

 *   <li>ShopService</li>

 *   <li>MinimaxSolver</li>

 * </ul>

 *

 * <p>Результат:

 * <ul>

 *   <li>PASSED — тест пройден</li>

 *   <li>FAILED — тест провален</li>

 * </ul>

 *

 * <h2>2. MANUAL режим (подключение к серверу)</h2>

 * Запуск:

 * <pre>

 * java testbackend.TestClient

 * </pre>

 *

 * <p>Подключается к серверу:

 * <b>localhost:12345</b>

 *

 * <p>📡 Поддерживаемые команды:

 * <ul>

 *   <li>MOVE x y — сделать ход</li>

 *   <li>CHAT_ALL text — общий чат</li>

 *   <li>CHAT_PRIVATE nick text — личное сообщение</li>

 *   <li>RESTART — перезапуск игры</li>

 * </ul>

 *

 * <p>📌 Используется для:

 * <ul>

 *   <li>ручного тестирования игры</li>

 *   <li>проверки сетевого взаимодействия</li>

 *   <li>отладки серверной логики</li>

 *   <li>автоматической проверки backend компонентов</li>

 * </ul>

 */
public class TestClient {

    // =========================================================================
    // ENTRY POINT
    // =========================================================================

    public static void main(String[] args) {
        boolean autoMode = args.length > 0 && args[0].equalsIgnoreCase("auto");

        if (autoMode) {
            TestRunner runner = new TestRunner();
            runner.runAll();
        } else {
            runManualClient();
        }
    }

    // =========================================================================
    // MANUAL SERVER CLIENT (оригинальный режим)
    // =========================================================================

    private static void runManualClient() {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Connected to server");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Поток чтения сообщений от сервера
            new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server");
                }
            }).start();

            // Отправка сообщений серверу
            while (true) {
                System.out.println("\nВведите команду:");
                System.out.println("MOVE x y");
                System.out.println("CHAT_ALL текст");
                System.out.println("CHAT_PRIVATE nick текст");
                System.out.println("RESTART");
                System.out.print("> ");

                String input = scanner.nextLine();
                out.println(input);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // TEST RUNNER
    // =========================================================================

    static class TestRunner {
        private int passed = 0;
        private int failed = 0;

        void runAll() {
            System.out.println("=================================================");
            System.out.println("  BACKEND TEST SUITE");
            System.out.println("=================================================\n");

            // --- Группа 1: Balance Manager ---
            group("BalanceManager");
            testBalanceInitialZero();
            testBalanceAddFunds();
            testBalanceSpendSuccess();
            testBalanceSpendInsufficientFunds();
            testBalanceMultipleCurrencies();

            // --- Группа 2: Inventory Manager ---
            group("InventoryManager");
            testInventoryAddAndHas();
            testInventoryDoesNotHaveBeforeAdd();
            testInventoryMultipleUsers();

            // --- Группа 3: Shop Service ---
            group("ShopService");
            testBuyCoinsSuccess();
            testBuyCoinsInsufficientRealMoney();
            testPerformPurchaseSuccess();
            testPerformPurchaseNotEnoughCoins();
            testPerformPurchaseAlreadyOwned();
            testPerformPurchaseUnknownItem();

            // --- Группа 4: Minimax ---
            group("MinimaxSolver");
            testMinimaxWinningMove();
            testMinimaxBlocksOpponent();
            testMinimaxDepthZero();
            testMinimaxAlreadyWon();
            testMinimaxAlreadyLost();
            testMinimaxChoosesBestAmongEquals();

            // --- Итог ---
            System.out.println("\n=================================================");
            System.out.printf("  RESULTS: %d passed, %d failed%n", passed, failed);
            System.out.println("=================================================");
        }

        private void group(String name) {
            System.out.println("\n--- " + name + " ---");
        }

        // Регистрируем результат
        private void ok(String testName) {
            System.out.printf("  [PASSED] %s%n", testName);
            passed++;
        }

        private void fail(String testName, String reason) {
            System.out.printf("  [FAILED] %s — %s%n", testName, reason);
            failed++;
        }

        private void assertTrue(String testName, boolean condition) {
            if (condition) ok(testName);
            else fail(testName, "expected true, got false");
        }

        private void assertEquals(String testName, double expected, double actual, double delta) {
            if (Math.abs(expected - actual) <= delta) ok(testName);
            else fail(testName, "expected " + expected + ", got " + actual);
        }

        private void assertEquals(String testName, int expected, int actual) {
            if (expected == actual) ok(testName);
            else fail(testName, "expected " + expected + ", got " + actual);
        }

        // =====================================================================
        // BALANCE MANAGER TESTS
        // =====================================================================

        void testBalanceInitialZero() {
            BalanceManager bm = new BalanceManager();
            assertEquals("initial balance is 0",
                    0.0, bm.getBalance("user1", CurrencyType.COINS), 0.001);
        }

        void testBalanceAddFunds() {
            BalanceManager bm = new BalanceManager();
            bm.addFunds("user1", CurrencyType.COINS, 500.0);
            assertEquals("balance after addFunds",
                    500.0, bm.getBalance("user1", CurrencyType.COINS), 0.001);
        }

        void testBalanceSpendSuccess() {
            BalanceManager bm = new BalanceManager();
            bm.addFunds("user1", CurrencyType.COINS, 200.0);
            try {
                bm.spend("user1", CurrencyType.COINS, 100.0);
                assertEquals("balance after spend",
                        100.0, bm.getBalance("user1", CurrencyType.COINS), 0.001);
            } catch (InsufficientFundsException e) {
                fail("spend success", "unexpected exception: " + e.getMessage());
            }
        }

        void testBalanceSpendInsufficientFunds() {
            BalanceManager bm = new BalanceManager();
            bm.addFunds("user1", CurrencyType.COINS, 50.0);
            try {
                bm.spend("user1", CurrencyType.COINS, 100.0);
                fail("spend throws on insufficient funds", "exception was NOT thrown");
            } catch (InsufficientFundsException e) {
                ok("spend throws InsufficientFundsException");
            }
        }

        void testBalanceMultipleCurrencies() {
            BalanceManager bm = new BalanceManager();
            bm.addFunds("u", CurrencyType.COINS, 300.0);
            bm.addFunds("u", CurrencyType.REAL_MONEY, 9.99);
            assertEquals("coins not mixed with real money",
                    300.0, bm.getBalance("u", CurrencyType.COINS), 0.001);
            assertEquals("real money tracked separately",
                    9.99, bm.getBalance("u", CurrencyType.REAL_MONEY), 0.01);
        }

        // =====================================================================
        // INVENTORY MANAGER TESTS
        // =====================================================================

        void testInventoryDoesNotHaveBeforeAdd() {
            InventoryManager im = new InventoryManager();
            assertTrue("item absent before add",
                    !im.hasItem("user1", "BG_SPACE_01"));
        }

        void testInventoryAddAndHas() {
            InventoryManager im = new InventoryManager();
            im.addItem("user1", "BG_SPACE_01");
            assertTrue("item present after add",
                    im.hasItem("user1", "BG_SPACE_01"));
        }

        void testInventoryMultipleUsers() {
            InventoryManager im = new InventoryManager();
            im.addItem("alice", "STICKER_CAT");
            assertTrue("alice has sticker", im.hasItem("alice", "STICKER_CAT"));
            assertTrue("bob does not have sticker", !im.hasItem("bob", "STICKER_CAT"));
        }

        // =====================================================================
        // SHOP SERVICE TESTS
        // =====================================================================

        private ShopService buildShop(BalanceManager bm, InventoryManager im) {
            ShopService shop = new ShopService(bm, im);
            shop.registerItem(new ItemsShop.BackgroundItem("BG_FOREST", "Лес", 100));
            shop.registerItem(new ItemsShop.BoardDesignItem("BOARD_NEON", "Неон", 250));
            shop.registerItem(new ItemsShop.StickerItem("STICKER_CAT", "Котик", 50));
            return shop;
        }

        void testBuyCoinsSuccess() {
            BalanceManager bm = new BalanceManager();
            InventoryManager im = new InventoryManager();
            bm.addFunds("u", CurrencyType.REAL_MONEY, 10.0);
            ShopService shop = buildShop(bm, im);
            try {
                shop.buyCoins("u", 1.0); // 1.0 реал = 100 монет
                assertEquals("coins after buyCoins",
                        100.0, bm.getBalance("u", CurrencyType.COINS), 0.001);
            } catch (InsufficientFundsException e) {
                fail("buyCoins success", "unexpected exception");
            }
        }

        void testBuyCoinsInsufficientRealMoney() {
            BalanceManager bm = new BalanceManager();
            InventoryManager im = new InventoryManager();
            ShopService shop = buildShop(bm, im);
            try {
                shop.buyCoins("u", 5.0);
                fail("buyCoins throws when broke", "no exception thrown");
            } catch (InsufficientFundsException e) {
                ok("buyCoins throws InsufficientFundsException when no real money");
            }
        }

        void testPerformPurchaseSuccess() {
            BalanceManager bm = new BalanceManager();
            InventoryManager im = new InventoryManager();
            bm.addFunds("u", CurrencyType.COINS, 200.0);
            ShopService shop = buildShop(bm, im);
            try {
                shop.performPurchase("u", "BG_FOREST"); // стоит 100
                assertTrue("item added to inventory", im.hasItem("u", "BG_FOREST"));
                assertEquals("coins deducted",
                        100.0, bm.getBalance("u", CurrencyType.COINS), 0.001);
            } catch (InsufficientFundsException e) {
                fail("purchase success", "unexpected exception");
            }
        }

        void testPerformPurchaseNotEnoughCoins() {
            BalanceManager bm = new BalanceManager();
            InventoryManager im = new InventoryManager();
            bm.addFunds("u", CurrencyType.COINS, 10.0);
            ShopService shop = buildShop(bm, im);
            try {
                shop.performPurchase("u", "BOARD_NEON"); // стоит 250
                fail("purchase throws when not enough coins", "no exception thrown");
            } catch (InsufficientFundsException e) {
                ok("performPurchase throws InsufficientFundsException when not enough coins");
            }
        }

        void testPerformPurchaseAlreadyOwned() {
            BalanceManager bm = new BalanceManager();
            InventoryManager im = new InventoryManager();
            bm.addFunds("u", CurrencyType.COINS, 500.0);
            im.addItem("u", "STICKER_CAT"); // уже куплено вручную
            ShopService shop = buildShop(bm, im);
            try {
                shop.performPurchase("u", "STICKER_CAT");
                // не должны списать монеты повторно
                assertEquals("balance unchanged if already owned",
                        500.0, bm.getBalance("u", CurrencyType.COINS), 0.001);
            } catch (InsufficientFundsException e) {
                fail("already owned no exception", "unexpected exception");
            }
        }

        void testPerformPurchaseUnknownItem() {
            BalanceManager bm = new BalanceManager();
            InventoryManager im = new InventoryManager();
            bm.addFunds("u", CurrencyType.COINS, 500.0);
            ShopService shop = buildShop(bm, im);
            try {
                shop.performPurchase("u", "NONEXISTENT_ID"); // нет в каталоге
                assertEquals("balance unchanged for unknown item",
                        500.0, bm.getBalance("u", CurrencyType.COINS), 0.001);
            } catch (InsufficientFundsException e) {
                fail("unknown item no exception", "unexpected exception");
            }
        }

        // =====================================================================
        // MINIMAX TESTS
        //
        // Используем встроенную реализацию TicTacToeState — поле 3×3,
        // крестики-нолики. Позволяет тестировать MinimaxSolver без сервера.
        // =====================================================================

        void testMinimaxWinningMove() {
            // X уже почти выиграл (две X в ряд), минимакс должен вернуть +10
            //  X | X | _
            //  _ | O | _
            //  _ | _ | _
            // X ходит следующим (isMaxPlayer = true), ход в [0][2] даёт победу
            MinimaxSolver solver = new MinimaxSolver();
            GameState state = new TicTacToeState(new char[][]{
                    {'X', 'X', ' '},
                    {' ', 'O', ' '},
                    {' ', ' ', ' '}
            }, true);
            int score = solver.solve(state, 9);
            assertTrue("minimax finds winning line (score > 0)", score > 0);
        }

        void testMinimaxBlocksOpponent() {
            // O почти выиграл, X должен заблокировать → лучший результат не -10
            //  O | O | _
            //  X | _ | _
            //  _ | _ | X
            // X ходит (isMaxPlayer = true)
            MinimaxSolver solver = new MinimaxSolver();
            GameState state = new TicTacToeState(new char[][]{
                    {'O', 'O', ' '},
                    {'X', ' ', ' '},
                    {' ', ' ', 'X'}
            }, true);
            int score = solver.solve(state, 9);
            assertTrue("minimax blocks opponent (score > -10)", score > -10);
        }

        void testMinimaxDepthZero() {
            // При depth=0 должна вернуться оценка текущего состояния (0 — пустое поле)
            MinimaxSolver solver = new MinimaxSolver();
            GameState state = new TicTacToeState(new char[][]{
                    {' ', ' ', ' '},
                    {' ', ' ', ' '},
                    {' ', ' ', ' '}
            }, true);
            int score = solver.solve(state, 0);
            assertEquals("depth=0 returns evaluateScore", 0, score);
        }

        void testMinimaxAlreadyWon() {
            // Позиция: X уже победил → оценка +10, getPossibleMoves пуст
            //  X | X | X
            //  O | O | _
            //  _ | _ | _
            MinimaxSolver solver = new MinimaxSolver();
            GameState state = new TicTacToeState(new char[][]{
                    {'X', 'X', 'X'},
                    {'O', 'O', ' '},
                    {' ', ' ', ' '}
            }, false); // O ходит, но игра уже кончена
            int score = solver.solve(state, 9);
            assertEquals("already won returns +10", 10, score);
        }

        void testMinimaxAlreadyLost() {
            // O уже победил → оценка -10
            //  O | O | O
            //  X | X | _
            //  _ | _ | _
            MinimaxSolver solver = new MinimaxSolver();
            GameState state = new TicTacToeState(new char[][]{
                    {'O', 'O', 'O'},
                    {'X', 'X', ' '},
                    {' ', ' ', ' '}
            }, true); // X ходит, но игра кончена
            int score = solver.solve(state, 9);
            assertEquals("already lost returns -10", -10, score);
        }

        void testMinimaxChoosesBestAmongEquals() {
            // Симметричное поле — минимакс должен хотя бы завершиться без ошибок
            MinimaxSolver solver = new MinimaxSolver();
            GameState state = new TicTacToeState(new char[][]{
                    {'X', 'O', 'X'},
                    {'O', 'X', 'O'},
                    {' ', 'X', 'O'}
            }, false); // один ход до конца
            int score = solver.solve(state, 9);
            // Независимо от результата — метод должен вернуть число без исключений
            assertTrue("minimax completes without exception", score >= -10 && score <= 10);
        }
    }

    // =========================================================================
    // TICTACTOE STATE — реализация GameState для тестов Minimax
    //
    // Поле 3×3. Символы: 'X', 'O', ' ' (пусто).
    // Оценка: +10 если X победил, -10 если O победил, 0 иначе.
    // X — maximizing player, O — minimizing.
    // =========================================================================

    static class TicTacToeState implements GameState {
        private final char[][] board;
        private final boolean xTurn; // true = ход X (max), false = ход O (min)

        TicTacToeState(char[][] board, boolean xTurn) {
            // Глубокое копирование, чтобы ходы не изменяли оригинал
            this.board = new char[3][3];
            for (int i = 0; i < 3; i++) {
                System.arraycopy(board[i], 0, this.board[i], 0, 3);
            }
            this.xTurn = xTurn;
        }

        @Override
        public boolean isGameOver() {
            return checkWinner() != ' ' || getEmptyCells().isEmpty();
        }

        @Override
        public int evaluateScore() {
            char w = checkWinner();
            if (w == 'X') return 10;
            if (w == 'O') return -10;
            return 0;
        }

        @Override
        public List<GameState> getPossibleMoves() {
            List<GameState> moves = new ArrayList<>();
            if (isGameOver()) return moves;

            char symbol = xTurn ? 'X' : 'O';
            for (int[] cell : getEmptyCells()) {
                char[][] next = copyBoard();
                next[cell[0]][cell[1]] = symbol;
                moves.add(new TicTacToeState(next, !xTurn));
            }
            return moves;
        }

        @Override
        public boolean isMaxPlayerTurn() {
            return xTurn;
        }

        // Возвращает 'X', 'O' или ' ' (никто не выиграл)
        private char checkWinner() {
            // Строки
            for (int r = 0; r < 3; r++) {
                if (board[r][0] != ' ' &&
                        board[r][0] == board[r][1] &&
                        board[r][1] == board[r][2]) return board[r][0];
            }
            // Столбцы
            for (int c = 0; c < 3; c++) {
                if (board[0][c] != ' ' &&
                        board[0][c] == board[1][c] &&
                        board[1][c] == board[2][c]) return board[0][c];
            }
            // Диагонали
            if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2])
                return board[0][0];
            if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0])
                return board[0][2];
            return ' ';
        }

        private List<int[]> getEmptyCells() {
            List<int[]> cells = new ArrayList<>();
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (board[r][c] == ' ') cells.add(new int[]{r, c});
            return cells;
        }

        private char[][] copyBoard() {
            char[][] copy = new char[3][3];
            for (int i = 0; i < 3; i++) System.arraycopy(board[i], 0, copy[i], 0, 3);
            return copy;
        }
    }
}