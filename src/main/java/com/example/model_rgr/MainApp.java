package com.example.model_rgr;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainApp extends Application {
    private Maze maze = new Maze();
    private GameStats stats = new GameStats();
    private ComboBox<String> algorithmSelector;
    private TextArea gameLog;
    private Label statsLabel;
    private TextArea customAlgorithmInput;
    private TextField runCountField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Лабиринт с алгоритмами ответа");

        // Создание интерфейса
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Выбор алгоритма
        HBox controls = new HBox(10);
        algorithmSelector = new ComboBox<>();
        algorithmSelector.getItems().addAll(
                "Всегда согласен",
                "Всегда не согласен",
                "Случайный выбор",
                "Циклический алгоритм",
                "Пользовательский алгоритм"
        );
        algorithmSelector.getSelectionModel().selectFirst();

        Button newGameBtn = new Button("Новая игра");
        newGameBtn.setOnAction(e -> startNewGame());

        runCountField = new TextField();
        runCountField.setPromptText("Кол-во запусков");
        runCountField.setPrefWidth(100);


        controls.getChildren().addAll(
                new Label("Алгоритм:"), algorithmSelector, runCountField, newGameBtn
        );

        // Создаем текстовое поле для пользовательского алгоритма
        customAlgorithmInput = new TextArea();
        customAlgorithmInput.setPromptText("Введите последовательность true/false через запятую (например: true,false,true)");
        customAlgorithmInput.setVisible(false);  // Сначала скрыто

        // Показываем поле только когда выбран пользовательский алгоритм
        algorithmSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            customAlgorithmInput.setVisible("Пользовательский алгоритм".equals(newVal));
        });

        // Добавляем поле в корневой контейнер (предполагая, что root - это VBox)
        root.getChildren().add(customAlgorithmInput);

        // Лог игры
        gameLog = new TextArea();
        gameLog.setEditable(false);
        gameLog.setWrapText(true);

        // Статистика
        statsLabel = new Label("Статистика: игрок - 0, компьютер - 0, средняя длина - 0");

        root.getChildren().addAll(controls, gameLog, statsLabel);

        // Настройка сцены
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startNewGame() {
        int runCount = 1;
        try {
            runCount = Integer.parseInt(runCountField.getText().trim());
            if (runCount <= 0) runCount = 1;
        } catch (NumberFormatException e) {
            runCount = 1; // если ошибка ввода, запускаем 1 раз
        }

        gameLog.clear();
        gameLog.appendText("Запуск " + runCount + " игр.\n");

        for (int i = 1; i <= runCount; i++) {
            ResponseAlgorithm algorithm = createAlgorithm(); // ВАЖНО: создавать заново!
            gameLog.appendText("\nИгра #" + i + ". Алгоритм: " + algorithm.getAlgorithmName() + "\n");

            GameSession session = new GameSession(maze, algorithm);
            gameLog.appendText("Стартовая позиция: " + maze.getStartPoint() + "\n");
            simulateGame(session);
        }

        updateStats();
    }

    private ResponseAlgorithm createAlgorithm() {
        String selected = algorithmSelector.getValue();
        switch (selected) {
            case "Всегда согласен": return new AlwaysAgreeAlgorithm();
            case "Всегда не согласен": return new AlwaysDisagreeAlgorithm();
            case "Случайный выбор": return new RandomAlgorithm();
            case "Циклический алгоритм": return new CycleAlgorithm();
            case "Пользовательский алгоритм": return createCustomAlgorithm();
            default: return new AlwaysAgreeAlgorithm();
        }
    }

    private ResponseAlgorithm createCustomAlgorithm() {
        String input = customAlgorithmInput.getText().trim();
        if (input.isEmpty()) {
            return new AlwaysAgreeAlgorithm(); // или можно выбросить исключение
        }

        String[] parts = input.split(",");
        List<Boolean> pattern = new ArrayList<>();

        for (String part : parts) {
            try {
                pattern.add(Boolean.parseBoolean(part.trim()));
            } catch (Exception e) {
                gameLog.appendText("Ошибка ввода: '" + part + "' - используйте true/false\n");
                pattern.add(false); // или можно пропустить некорректные значения
            }
        }

        return new CustomAlgorithm(pattern);
    }

    private void simulateGame(GameSession session) {
        int currentPosition = session.getCurrentPosition();

        while (!session.isGameOver()) {
            Set<Integer> possibleMoves = maze.getPossibleMoves(currentPosition);
            // Выбираем случайный ход из возможных вместо первого
            int proposedMove = possibleMoves.stream()
                    .skip(new Random().nextInt(possibleMoves.size()))
                    .findFirst()
                    .orElse(currentPosition);

            boolean agreed = session.makeMove(proposedMove);
            gameLog.appendText(String.format(
                    "Ход %d: предложен ход в %d. Алгоритм %s. Новое положение: %d\n",
                    session.getStepsTaken(),
                    proposedMove,
                    agreed ? "согласился" : "отказался",
                    session.getCurrentPosition()
            ));

            currentPosition = session.getCurrentPosition();
        }

        stats.recordGame(session.isPlayerWon(), session.getStepsTaken());
        updateStats();

        gameLog.appendText(session.isPlayerWon()
                ? "Игрок выиграл! Достигнут выход " + currentPosition + "\n"
                : "Компьютер выиграл! Превышено количество ходов\n");
    }

    private void updateStats() {
        statsLabel.setText(String.format(
                "Статистика: игрок - %d, компьютер - %d, средняя длина - %.1f",
                stats.getPlayerWins(),
                stats.getComputerWins(),
                stats.getAverageSteps()
        ));
    }
}