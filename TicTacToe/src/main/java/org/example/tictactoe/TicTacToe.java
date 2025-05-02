package org.example.tictactoe;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private enum Difficulty { EASY, MEDIUM, HARD }
    private Difficulty difficulty;
    private boolean xTurn = true;
    private boolean vsComputer = false;
    private Button[][] board = new Button[3][3];
    private Button userVuser;
    private Button userVcomp;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        Label versus = new Label("Choose opponent: ");
        String selectedStyle = "-fx-padding: 5; -fx-background-color: #a1c4fd; -fx-border-color: #333; -fx-border-width: 1px; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-weight: bold;";
        String defaultStyle = "-fx-padding: 5; -fx-background-color: #d2d6d6; -fx-border-color: #333; -fx-border-width: 1px; -fx-border-radius: 10; -fx-background-radius: 10;";
        // create buttons to choose opponent
        userVcomp = new Button("User vs Computer");
        userVuser = new Button("User vs User");
        userVcomp.setStyle("-fx-padding: 5; -fx-background-color: #d2d6d6; -fx-border-color: #333; -fx-border-width: 1px; -fx-border-radius: 10; -fx-background-radius: 10;");
        userVuser.setStyle("-fx-padding: 5; -fx-background-color: #d2d6d6; -fx-border-color: #333; -fx-border-width: 1px; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label difficultyLabel = new Label("Select Difficulty: ");
        // create difficulty buttons
        Button easyBtn = new Button("Easy");
        Button mediumBtn = new Button("Medium");
        Button hardBtn = new Button("Hard");
        easyBtn.setStyle(defaultStyle);
        mediumBtn.setStyle(defaultStyle);
        hardBtn.setStyle(defaultStyle);

        // create box for buttons
        HBox diffBox = new HBox(10, difficultyLabel, easyBtn, mediumBtn, hardBtn);
        diffBox.setVisible(false); // Hide by default
        diffBox.setAlignment(Pos.CENTER); // Center align
        // default to easy
        difficulty = Difficulty.EASY;
        easyBtn.setStyle(selectedStyle);
        // when user selects user as opponent
        userVuser.setOnAction(e -> {
            vsComputer = false;
            diffBox.setVisible(false);
            userVuser.setStyle(selectedStyle);
            userVcomp.setStyle(defaultStyle);
            resetBoard();
        });
        // when user selects computer as opponent
        userVcomp.setOnAction(e -> {
            vsComputer = true;
            diffBox.setVisible(true);
            userVcomp.setStyle(selectedStyle);
            userVuser.setStyle(defaultStyle);
            resetBoard();
        });

        // set difficulty to easy
        easyBtn.setOnAction(e -> {
            difficulty = Difficulty.EASY;
            easyBtn.setStyle(selectedStyle);
            mediumBtn.setStyle(defaultStyle);
            hardBtn.setStyle(defaultStyle);
        });
        // set difficulty to medium
        mediumBtn.setOnAction(e -> {
            difficulty = Difficulty.MEDIUM;
            easyBtn.setStyle(defaultStyle);
            mediumBtn.setStyle(selectedStyle);
            hardBtn.setStyle(defaultStyle);
        });
        // set difficulty to hard
        hardBtn.setOnAction(e -> {
            difficulty = Difficulty.HARD;
            easyBtn.setStyle(defaultStyle);
            mediumBtn.setStyle(defaultStyle);
            hardBtn.setStyle(selectedStyle);
        });

        // create grid for tic tac toe boxes
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        grid.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f9; -fx-border-color: #333; -fx-border-width: 2px; -fx-border-radius: 10; -fx-background-radius:10;");

        for (int row = 0; row< 3; row ++){
            for (int col = 0; col<3; col++){
                Button cell = new Button();
                cell.setPrefSize(100,100);
                cell.setFont(new Font(24));

                cell.setStyle("-fx-background-color: #fff; -fx-border-color: #333; -fx-border-width: 2px; -fx-font-weight: bold; -fx-text-fill: #333;");
                final int r = row;
                final int c = col;

                cell.setOnAction(e->handleMove(r,c));

                board[row][col]= cell;
                grid.add(cell, col, row);
            }
        }
        HBox buttonBox = new HBox(10); // 10 = spacing between buttons
        buttonBox.getChildren().addAll(versus, userVuser, userVcomp);
        buttonBox.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(buttonBox, diffBox, grid);

        Scene scene = new Scene(layout,360,400);
        stage.setTitle("My TicTacToe Game");
        stage.setScene(scene);
        stage.show();
    }

    private void handleMove(int row, int col) {
        if (board[row][col].getText().isEmpty()){

            board[row][col].setText(xTurn ? "X" : "O");

            if (xTurn){
                board[row][col].setStyle("-fx-background-color: #d1e7ff; -fx-border-color: #333; -fx-border-width: 2px; -fx-font-weight: bold; -fx-text-fill: #004085;");
            }else {
                board[row][col].setStyle("-fx-background-color: #ffdce0; -fx-border-color: #333; -fx-border-width: 2px; -fx-font-weight: bold; -fx-text-fill: #721c24;");
            }

            if (checkWinner()){
                showAlert((xTurn ? "X" : "O") + " wins!");
                resetBoard();
            } else if (isBoardFull()){
                showAlert("It's A Draw!!");
            }else {
                xTurn = !xTurn;
                if(!xTurn && vsComputer) {
                    makeComputerMove(); // computer plays with user
                }
            }
        }

    }
    // check if board is full
    private boolean isBoardFull() {
        for (int row = 0; row<3; row ++){
            for (int col = 0; col<3; col++){
                if (board[row][col].getText().isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }
    // control computer moves
    private void makeComputerMove() {
        switch (difficulty) {
            case EASY:
                makeRandomMove();
                break;
            case MEDIUM:
                if (Math.random() < 0.5) {
                    makeRandomMove();
                } else {
                    makeBestMove(); // Minimax
                }
                break;
            case HARD:
                makeBestMove(); // Full Minimax
                break;
        }
    }
    // easy difficulty
    private void makeRandomMove() {
        List<int[]> available = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getText().isEmpty()) {
                    available.add(new int[]{r, c});
                }
            }
        }
        if (!available.isEmpty()) {
            int[] move = available.get((int)(Math.random() * available.size()));
            handleMove(move[0], move[1]);
        }
    }
    // hard difficulty
    private void makeBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getText().isEmpty()) {
                    board[r][c].setText("O"); // Computer is "O"
                    int score = minimax(0, false);
                    board[r][c].setText("");
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = r;
                        bestCol = c;
                    }
                }
            }
        }

        if (bestRow != -1 && bestCol != -1) {
            handleMove(bestRow, bestCol);
        }
    }

    // computer finds best possible move
    private int minimax(int depth, boolean isMaximizing) {
        if (checkWinner("O")) return 1;
        if (checkWinner("X")) return -1;
        if (isBoardFull()) return 0;
        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].getText().isEmpty()) {
                        board[r][c].setText("O");
                        best = Math.max(best, minimax(depth + 1, false));
                        board[r][c].setText("");
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].getText().isEmpty()) {
                        board[r][c].setText("X");
                        best = Math.min(best, minimax(depth + 1, true));
                        board[r][c].setText("");
                    }
                }
            }
            return best;
        }
    }

    // reset board
    private void resetBoard() {
        for (int row = 0; row<3; row ++){
            for (int col = 0; col<3; col++){
                board[row][col].setText("");
                board[row][col].setStyle("-fx-background-color: #fff; -fx-border-color: #333; -fx-border-width: 2px; -fx-font-weight: bold; -fx-text-fill: #333;");
            }
        }
        xTurn = true;
    }
    // announce results of game
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        resetBoard();
    }
    // check who won
    private boolean checkWinner() {
        for (int i = 0; i<3; i++){
            if (checkLine(board[i][0], board[i][1], board[i][2]) ||
                    checkLine(board[0][i],board[1][i],board[2][i])){
                return true;
            }
        }
        return  checkLine(board[0][0], board[1][1], board[2][2]) ||
                checkLine(board[0][2], board[1][1], board[2][0]);
    }

    private boolean checkWinner(String player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].getText().equals(player) &&
                    board[i][1].getText().equals(player) &&
                    board[i][2].getText().equals(player)) return true;

            if (board[0][i].getText().equals(player) &&
                    board[1][i].getText().equals(player) &&
                    board[2][i].getText().equals(player)) return true;
        }

        if (board[0][0].getText().equals(player) &&
                board[1][1].getText().equals(player) &&
                board[2][2].getText().equals(player)) return true;

        if (board[0][2].getText().equals(player) &&
                board[1][1].getText().equals(player) &&
                board[2][0].getText().equals(player)) return true;

        return false;
    }
    //
    private boolean checkLine(Button b1, Button b2, Button b3) {
        return  !b1.getText().isEmpty() && b1.getText().equals(b2.getText()) &&b2.getText().equals(b3.getText());
    }
}
