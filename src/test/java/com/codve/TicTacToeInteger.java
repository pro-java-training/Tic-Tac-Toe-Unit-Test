package com.codve;

import org.junit.Test;

import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

public class TicTacToeInteger {
    /**
     * 如果数据库运行, 就不会抛出异常
     */
    @Test
    public void givenDBIsOnWhenPlayThenNoException() throws UnknownHostException {
        TicTacToe ticTacToe = new TicTacToe();
        assertEquals(TicTacToe.No_WINNER, ticTacToe.play(1, 1));

    }
}
