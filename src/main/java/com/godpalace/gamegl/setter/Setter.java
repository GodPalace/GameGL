package com.godpalace.gamegl.setter;

import java.io.IOException;

public interface Setter<T> {
    void set(String key, T value);
    T get(String key);

    void close() throws IOException;
    void reset();
    void finish() throws IOException;
}
