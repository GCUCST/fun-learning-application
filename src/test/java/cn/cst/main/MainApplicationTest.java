package cn.cst.main;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainApplicationTest {
    @Test
    void testMainFunction() {
        MainApplication mainApplication = new MainApplication();
        Assertions.assertDoesNotThrow(
                mainApplication::output
        );
    }


}
