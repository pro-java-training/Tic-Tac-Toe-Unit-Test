package com.codve;

import com.codve.mongo.TicTacToeBean;
import com.mongodb.MongoException;
import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TicTacToeCollectionSpec {

    private TicTacToeCollection collection;
    private TicTacToeBean bean;
    MongoCollection mongoCollection;

    @Before
    public void before() throws UnknownHostException {
        collection = spy(new TicTacToeCollection());
        bean = new TicTacToeBean(3, 2, 1, 'Y');
        mongoCollection = mock(MongoCollection.class);
    }

    /**
     * 测试数据库名
     */
    @Test
    public void whenInitThenMongoDBNamed() {
        assertEquals("tic-tac-toe",
                collection.getMongoCollection().getDBCollection().getDB().getName());
    }

    /**
     * 测试表名
     */
    @Test
    public void whenInitThenMongoCollectionNamed() {
        assertEquals("game", collection.getMongoCollection().getName());
    }

    /**
     * 测试数据库存储是否调用
     */
    @Test
    public void whenSaveThenInvokeMongoCollectionSave() {
        doReturn(mongoCollection).when(collection).getMongoCollection();
        collection.saveMove(bean);
        verify(mongoCollection, times(1)).save(bean);
    }

    /**
     * 测试数据库存储是否成功
     */
    @Test
    public void whenSaveThenReturnTrue() {
        doReturn(mongoCollection).when(collection).getMongoCollection();
        assertTrue(collection.saveMove(bean));
    }

    /**
     * 存储失败时返回 false
     */
    @Test
    public void givenExceptionWhenSaveThenReturnFalse() {
        doThrow(new MongoException("error"))
                .when(mongoCollection)
                .save(any(TicTacToeBean.class));
        doReturn(mongoCollection).when(collection).getMongoCollection();
        collection.saveMove(bean);
        assertFalse(collection.saveMove(bean));
    }

    /**
     * 游戏重新开始时删除旧的数据
     */
    @Test
    public void whenDropThenInvokeMongoDrop() {
        doReturn(mongoCollection).when(collection).getMongoCollection();
        collection.drop();
        verify(mongoCollection, times(1)).drop();
    }
}
