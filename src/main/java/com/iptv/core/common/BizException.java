package com.iptv.core.common;

import java.util.ArrayList;
import java.util.List;

public class BizException extends Exception {
    private final List<String> errorMessage = new ArrayList();

    public BizException(String message) {
        super(message);

        this.errorMessage.add(message);
    }

    public BizException(List<String> messages) {
        this.errorMessage.addAll(messages);
    }

    public List<String> getMessages() {
        return this.errorMessage;
    }
}
