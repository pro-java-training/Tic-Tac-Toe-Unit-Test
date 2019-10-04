package com.codve;

import com.codve.mongo.TicTacToeBean;

import java.net.UnknownHostException;

public class TicTacToe {

    public static final Object No_WINNER = "No winner";
    private static final int SIZE = 3;

    private Character[][] board = {
            {'\0', '\0', '\0'},
            {'\0', '\0', '\0'},
            {'\0', '\0', '\0'}
    };

    private char lastPlayer = '\0';

    private TicTacToeCollection ticTacToeCollection;

    private int turn = 0;

    // 使用实例化的对象
    public TicTacToe() throws UnknownHostException {
        this(new TicTacToeCollection());
    }

    // 使用模拟的对象
    protected TicTacToe(TicTacToeCollection collection) {
        ticTacToeCollection = collection;
        drop();
    }


    protected TicTacToeCollection getTicTacToeCollection() {
        return ticTacToeCollection;
    }


    public String play(int x, int y) {
        checkAxis(x);
        checkAxis(y);
        lastPlayer = nextPlayer();
        setBox(new TicTacToeBean(turn++, x, y, lastPlayer));
        if (isWin(x, y)) {
            return lastPlayer + " is the winner";
        } else if (isDraw()) {
            return "The result is draw";
        } else {
            return "No winner";
        }
    }

    private boolean isWin(int x, int y) {
        int playerTotal = lastPlayer * 3;
        char horizontal, vertical, diagonal1, diagonal2;
        horizontal = vertical = diagonal1 = diagonal2 = 0;
        for (int i = 0; i < SIZE; i++) {
            horizontal += board[i][y - 1];
            vertical += board[x - 1][i];
            diagonal1 += board[i][i];
            diagonal2 += board[i][SIZE - i - 1];
        }
        return horizontal == playerTotal ||
                vertical == playerTotal ||
                diagonal1 == playerTotal ||
                diagonal2 == playerTotal ;
    }

    private boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkAxis(int axis) {
        if (axis < 1 || axis > 3) {
            throw new RuntimeException("axis is outside board.");
        }
    }

    private void setBox(TicTacToeBean bean) {
        if (board[bean.getX() - 1][bean.getY() - 1] != '\0') {
            throw new RuntimeException("box is occupied.");
        } else {
            board[bean.getX() - 1][bean.getY() - 1] = lastPlayer;
            boolean result = getTicTacToeCollection().saveMove(bean);
            if (!result) {
                throw new RuntimeException("save to db failed.");
            }
        }
    }

    public char nextPlayer() {
        if (lastPlayer == 'X') {
            return 'O';
        }
        return 'X';
    }

    public void drop() {
        boolean result = getTicTacToeCollection().drop();
        if (!result) {
            throw new RuntimeException("drop db failed.");
        }
    }
}
