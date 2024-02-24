import java.sql.*;
import java.util.*;

/**
 * Класс для работы с консольным CRUD (Create, Read, Update, Delete) приложением.
 */
public class ConsoleCRUD {

    public static void main(String[] args) {

        /**
         * @param connection представляет соединение с базой данных
         * @param DB_URL URL для подключения к базе данных
         * @param USER имя пользователя для доступа к базе данных
         * @param PASS пароль для доступа к базе данных
         * @see DatabaseConfig
         * @param statement Используется для выполнения SQL запросов к базе данных.
         * @param sc Считывает ввода пользователя с консоли
         */
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASS);
             Statement statement = connection.createStatement();
             Scanner sc = new Scanner(System.in)) {

            displayMenu();
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    addProduct(statement, sc);
                    break;
                case "2":
                    viewProduct(statement, sc);
                    break;
                case "3":
                    updateProduct(statement, sc);
                    break;
                case "4":
                    deleteProduct(statement, sc);
                    break;
                default:
                    System.out.println("Некорректный выбор. Пожалуйста, выберите действие от 1 до 4.");
            }

        } catch (Exception e) {
            System.out.println("\u001B[31m" + "ERROR: " + "\u001B[0m" + e.getMessage());
        }
    }

    /**
     * Отображает меню действий для работы с базой данных.
     */
    private static void displayMenu() {
        System.out.println("Меню:");
        System.out.println("1. Добавить продукт");
        System.out.println("2. Просмотреть продукт");
        System.out.println("3. Обновить продукт");
        System.out.println("4. Удалить продукт" + "\n");
        System.out.print("Выберите действие: ");
    }

    /**
     * Добавляет товар в базу данных.
     * @param statement Объект Statement для выполнения SQL запросов.
     * @param sc Объект Scanner для считывания ввода пользователя.
     * @throws SQLException Если произошла ошибка при выполнении SQL запроса.
     */
    private static void addProduct(Statement statement, Scanner sc) throws SQLException {
        System.out.print("Введите наименование товара: ");
        String nameProduct = sc.nextLine();
        System.out.print("Введите остаток на складе: ");
        String quantity = sc.nextLine();
        System.out.print("Введите цену за одну штуку: ");
        String price = sc.nextLine();

        String sql = "INSERT INTO shoppinglist (nameProduct, quantity, price) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = statement.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, nameProduct);
            pstmt.setString(2, quantity);
            pstmt.setString(3, price);
            pstmt.executeUpdate();
            System.out.println("Товар успешно добавлен.");
        }
    }

    /**
     * Выводит информацию о выбранном товаре.
     * @param statement Объект Statement для выполнения SQL запросов.
     * @param sc Объект Scanner для считывания ввода пользователя.
     * @throws SQLException Если произошла ошибка при выполнении SQL запроса.
     */
    private static void viewProduct(Statement statement, Scanner sc) throws SQLException {
        System.out.print("Введите id: ");
        long id = Long.parseLong(sc.nextLine());

        String sql = "SELECT * FROM shoppinglist WHERE id = ?";
        try (PreparedStatement pstmt = statement.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Артикул: " + rs.getInt("id"));
                System.out.println("Товар: " + rs.getString("nameProduct"));
                System.out.println("Цена: " + rs.getString("price"));
            } else {
                System.out.println("Такого товара нет!");
            }
        }
    }

    /**
     * Обновляет информацию о выбранном товаре.
     * @param statement Объект Statement для выполнения SQL запросов.
     * @param sc Объект Scanner для считывания ввода пользователя.
     * @throws SQLException Если произошла ошибка при выполнении SQL запроса.
     */
    private static void updateProduct(Statement statement, Scanner sc) throws SQLException {
        System.out.print("Введите ID товар для изменения: ");
        long id = Long.parseLong(sc.nextLine());

        System.out.print("Введите новое наименование товара: ");
        String nameProduct = sc.nextLine();
        System.out.print("Введите новую цену за одну штуку: ");
        String price = sc.nextLine();
        System.out.print("Введите новый остаток на складе: ");
        String quantity = sc.nextLine();

        String sql = "UPDATE shoppinglist SET nameproduct = ?, price = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement pstmt = statement.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, nameProduct);
            pstmt.setString(2, price);
            pstmt.setString(3, quantity);
            pstmt.setLong(4, id);
            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("Вы успешно обновили карточку товара");
            } else {
                System.out.println("Такого товара нет!");
            }
        }
    }

    /**
     * Удаляет выбранный товар из базы данных.
     * @param statement Объект Statement для выполнения SQL запросов.
     * @param sc Объект Scanner для считывания ввода пользователя.
     * @throws SQLException Если произошла ошибка при выполнении SQL запроса.
     */
    private static void deleteProduct(Statement statement, Scanner sc) throws SQLException {
        System.out.print("Введите ID товара для удаления: ");
        long id = Long.parseLong(sc.nextLine());

        String sql = "DELETE FROM shoppinglist WHERE id = ?";
        try (PreparedStatement pstmt = statement.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("Вы успешно удалили товар");
            } else {
                System.out.println("Такого товара нет!");
            }
        }
    }
}
