/**
 * ClassName: SubFieldException
 *
 * Date: February 4, 2018
 *
 * [COPY]
 */

package com.example.jtbakker_subbook;

/**
 * Created by Jacob Bakker
 *
 * This class implements the exception type intended for illegal Subscription attributes.
 * Only basic methods for accessing/changing the error message are needed.
 */
public class SubFieldException extends Exception {
    private String message;

    public SubFieldException() {}

    public SubFieldException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
