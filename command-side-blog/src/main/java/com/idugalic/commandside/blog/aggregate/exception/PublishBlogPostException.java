package com.idugalic.commandside.blog.aggregate.exception;

public class PublishBlogPostException extends IllegalStateException {

    private static final long serialVersionUID = 2680537715575935263L;

    public PublishBlogPostException() {
    }

    public PublishBlogPostException(String s) {
        super(s);
    }

    public PublishBlogPostException(Throwable cause) {
        super(cause);
    }

    public PublishBlogPostException(String message, Throwable cause) {
        super(message, cause);
    }
}
