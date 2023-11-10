package br.com.doit.rxworld.service.doit;

@SuppressWarnings("serial")
public class DOitException extends RuntimeException {
    private final String message;

    public DOitException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
