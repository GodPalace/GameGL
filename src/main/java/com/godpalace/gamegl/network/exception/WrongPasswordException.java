package com.godpalace.gamegl.network.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("Wrong password.");
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
