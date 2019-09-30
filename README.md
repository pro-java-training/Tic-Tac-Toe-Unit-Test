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
>3. 如果棋子放在了已经有棋子的地方, 就会引发 RuntimeException 异常

第 1 个测试的代码

```java
public class TicTacToeSpec {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private TicTacToe ticTacToe;

    @Before
    public final void before() {
        ticTacToe = new TicTacToe();
    }
    
    /**
     * 如果棋子超过 x 轴的边界, 将引发 RuntimeException
     */
    @Test
    public void whenXOutsideBoardThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(5, 2);
    }
}
```
每个测试方法都会调用`@Before`注解的方法.

编写完测试代码后, 运行测试, 由于此时`play()`方法还不存在, 因此测试无法通过.
如果此时测试通过了, 就说明测试代码有问题.

实现`play`方法
```java
public class TicTacToe {
    
        public String play(int x, int y) {
            if (x < 1 || x > 3) {
                throw new RuntimeException("axis is outside board.");
            }
        }
}
```

此时再运行, 就能通过测试了.
---

**实现只能包含让测试通过的最少代码**

对于第 2 个需求, 需要重写测试方法
```
    /**
     * 如果棋子超过 y 轴的边界, 将引发 RuntimeException
     */
    @Test
    public void whenYOutsideBoardThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(2, 5);
    }
```

此时测试, 必须不能通过

实现该需求
```
    public String play(int x, int y) {
        if (x < 1 || x > 3) {
            throw new RuntimeException("x is outside board.");
        }
        if (y < 1 || y > 3) {
            throw new RuntimeException("y is outside board.");
        }
    }
```

现在测试, 就能通过了.

现在重构代码
```
    public String play(int x, int y) {
        checkAxis(x);
        checkAxis(y);
    }
    
    private void checkAxis(int axis) {
        if (axis < 1 || axis > 3) {
            throw new RuntimeException("axis is outside board.");
        }
    }
```

重构代码没有改变 `play()`的功能, 只是增强了可读性. 因此只要之前的测试全部通过, 就完全可以保证重构是安全的.

###代码覆盖率

    gradle clean test jacocoTestReport

测试结果在`/build/reports/jacoco/test/html` 中.