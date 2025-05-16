package cm.fastrelay.usermanager.domain.users;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    void testConstructor() {
        User objectUnderTest = User.builder().build();
        assertThat(objectUnderTest.getId()).isNotNull();
    }
}