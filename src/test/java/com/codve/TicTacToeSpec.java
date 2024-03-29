package com.codve;

import com.codve.mongo.TicTacToeBean;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class TicTacToeSpec {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private TicTacToe ticTacToe;

    private TicTacToeCollection collection;

    @Before
    public final void before() throws UnknownHostException {
        collection = mock(TicTacToeCollection.class);
        doReturn(true).when(collection).saveMove(any(TicTacToeBean.class));
        doReturn(true).when(collection).drop();
        ticTacToe = new TicTacToe(collection);
    }

    /**
     * 如果棋子超过 x 轴的边界, 将引发 RuntimeException
     */
    @Test
    public void whenXOutsideBoardThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(5, 2);
    }

    /**
     * 如果棋子超过 y 轴的边界, 将引发 RuntimeException
     */
    @Test
    public void whenYOutsideBoardThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(2, 5);
    }

    /**
     * 如果棋子 x 轴是负数, 将引发 RuntimeException
     */
    @Test
    public void whenXIsNegativeThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(-1, 0);
    }

    /**
     * 如果棋子放的地方已有棋子, 将引发 RuntimeException
     */
    @Test
    public void whenOccupiedThenRuntimeException() {
        ticTacToe.play(2, 1);
        exception.expect(RuntimeException.class);
        ticTacToe.play(2, 1);
    }

    /**
     * 玩家 X 先下
     */
    @Test
    public void givenFirstTurnWhenNextPlayerThenX() {
        assertEquals('X', ticTacToe.nextPlayer());
    }

    /**
     * 如果第一次是玩家 X 下的, 接下来应轮到玩家 O
     */
    @Test
    public void givenLastTurnWasXWhenNextPlayerThenO() {
        ticTacToe.play(1, 1);
        assertEquals('O', ticTacToe.nextPlayer());
    }

    /**
     * 如果最后一次是玩家 Y 下的, 接下来应轮到玩家 X
     * 这个测试不用修改实现就能通过, 应当删除
     */
    @Ignore("没有用处, 应当被删除.")
    @Test
    public void givenLastTurnWasOWhenNextPlayerThenX() {
        ticTacToe.play(1,1);
        ticTacToe.play(2, 1);
        assertEquals('X', ticTacToe.nextPlayer());
    }

    /**
     * 无人获胜
     */
    @Test
    public void whenPlayThenNoWinner() {
        String actual = ticTacToe.play(1, 1);
        assertEquals("No winner", actual);
    }

    @Test
    /**
     * 一个玩家占据整条水平线就获胜
     */
    public void whenPlayAndWholeHorizontalLineThenWinner() {
        ticTacToe.play(1, 1); // x
        ticTacToe.play(1, 2); // o
        ticTacToe.play(2, 1); // x
        ticTacToe.play(2, 2); // y
        String actual = ticTacToe.play(3, 1); // x
        assertEquals("X is the winner", actual);
    }

    /**
     * 一个玩家占据整条垂直线就获胜
     */
    @Test
    public void whenPlayAndWholeVerticalLineThenWinner() {
        ticTacToe.play(1, 1); // x
        ticTacToe.play(2, 1); // o
        ticTacToe.play(1, 2); // x
        ticTacToe.play(2, 2); // o
        String actual = ticTacToe.play(1, 3); // x
        assertEquals("X is the winner", actual);
    }

    /**
     * 一个玩家占据从左上到右下的斜对角线就获胜
     */
    @Test
    public void whenPlayAndTopBottomDiagonalLineThenWinner() {
        ticTacToe.play(1, 1); // x
        ticTacToe.play(2, 1); // y
        ticTacToe.play(2, 2); // x
        ticTacToe.play(1, 2); // y
        String actual = ticTacToe.play(3, 3); // x
        assertEquals("X is the winner", actual);
    }

    /**
     * 一个玩家占据从左下到右上的斜对角线就获胜
     */
    @Test
    public void whenPlayAndBottomTopDiagonalLineThenWinner() {
        ticTacToe.play(1, 3);
        ticTacToe.play(1, 1);
        ticTacToe.play(2, 2);
        ticTacToe.play(2, 3);
        String actual = ticTacToe.play(3, 1);
        assertEquals("X is the winner", actual);
    }

    /**
     * 格子都被填满了, 平局
     */
    @Test
    public void whenAllBoxesFilledThenDraw() {
        ticTacToe.play(1, 1);
        ticTacToe.play(1, 2);
        ticTacToe.play(1, 3);
        ticTacToe.play(2, 1);
        ticTacToe.play(2, 3);
        ticTacToe.play(2, 2);
        ticTacToe.play(3, 1);
        ticTacToe.play(3, 3);
        String actual = ticTacToe.play(3, 2);
        assertEquals("The result is draw", actual);
    }

    /**
     * 游戏开始后数据库需存在
     */
    @Test
    public void whenInitThenDbIsSet() {
        assertNotNull(ticTacToe.getTicTacToeCollection());
    }

    /**
     * 每一次下棋, 都会保存操作入库
     */
    @Test
    public void whenPlayThenSaveIsInvoke() {
        TicTacToeBean move = new TicTacToeBean(1, 1, 3, 'X');
        ticTacToe.play(move.getX(), move.getY());
        verify(collection, times(1)).saveMove(move);
    }

    /**
     * 下棋后无法存储抛出异常
     */
    @Test
    public void whenPlayAndSaveReturnFalseThenThrowException() {
        doReturn(false).when(collection).saveMove(any(TicTacToeBean.class));
        TicTacToeBean move = new TicTacToeBean(1, 1, 3, 'X');
        exception.expect(RuntimeException.class);
        ticTacToe.play(move.getX(), move.getY());
    }

    /**
     * 每次下棋后轮次增加 1
     */
    @Test
    public void whenPlayInvokeThenTurnIncrease() {
        TicTacToeBean move1 = new TicTacToeBean(1, 1, 1, 'X');
        ticTacToe.play(move1.getX(), move1.getY());
        verify(collection, times(1)).saveMove(move1);

        TicTacToeBean move2 = new TicTacToeBean(2, 1, 2, 'O');
        ticTacToe.play(move1.getX(), move2.getY());
        verify(collection, times(1)).saveMove(move2);
    }

    /**
     * 游戏初始化后删除旧的数据库
     */
    @Test
    public void whenInitThenDrpInvoke() {
        verify(collection, times(1)).drop();
    }

    /**
     * 数据库删除失败时抛出异常
     */
    @Test
    public void whenDropFailedThenRuntimeException() {
        doReturn(false).when(collection).drop();
        exception.expect(RuntimeException.class);
        new TicTacToe(collection);
    }
}
