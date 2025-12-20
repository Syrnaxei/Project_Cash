package top.liewyoung.config.Exceptions;

/**
 * 空键错误
 *
 * @author LiewYoung
 * @since 2025/12/20
 */
public class KEYNULLException extends Exception {
    public KEYNULLException() {
        super("KEY_ERROR : When you got this which mean there is no apikey");
    }
}
