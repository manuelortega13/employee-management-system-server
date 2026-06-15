package com.ethan.employee_system.urlshortener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Base62Test {

    @Test
    void encodesZeroToFirstAlphabetChar() {
        assertThat(Base62.encode(0)).isEqualTo("0");
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 9, 61, 62, 125, 1000, 123456789, Long.MAX_VALUE})
    void encodeThenDecodeRoundTrips(long id) {
        assertThat(Base62.decode(Base62.encode(id))).isEqualTo(id);
    }

    @Test
    void encodesAcrossBaseBoundary() {
        assertThat(Base62.encode(61)).isEqualTo("Z");
        assertThat(Base62.encode(62)).isEqualTo("10");
    }

    @Test
    void rejectsNegativeValue() {
        assertThatThrownBy(() -> Base62.encode(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsEmptyCode() {
        assertThatThrownBy(() -> Base62.decode(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsInvalidCharacter() {
        assertThatThrownBy(() -> Base62.decode("ab-cd"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
