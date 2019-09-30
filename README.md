# Tic-Tac-Toe-Unit-Test

使用测试驱动开发(Test-Driven Development, TDD)的方式完成三连棋的编码. 测试工具为 JUnit, 覆盖率检测工具为 Jacoco.

**添加 gradle 依赖**
---
    plugins {
        ...
        id 'jacoco'
    }
    
    ...
    
    dependencies {
        testCompile group: 'junit', name: 'junit', version: '4.12'
    }




TDD 的开发模式为 测试 => 开发 => 重构, 并以此循环.

### 需求 1
定义棋盘的边界
> 该需求可以分为 3 个测试:
>1. 如果棋子放在超出了 x 轴的地方, 就引发 RuntimeException 异常
>2. 如果棋子放在超出了 Y 轴的地方, 就引发 RuntimeException 异常
>3. 如果棋子放在了已经有棋子的地方, 就会引发 RuntimeExceptin 异常

第 1 个测试

```java
    /**
     * 如果棋子超过 x 轴的边界, 将引发 RuntimeException
     */
    @Test
    public void whenXOutsideBoardThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(5, 2);
    }
```

