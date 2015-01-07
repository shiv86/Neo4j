package com.packtpub.learningcypher.chapter1;

/**
 * A cost centre object
 * 
 * @author Onofrio Panzarino
 */
public class CostCenter {
    public static final String CODE = "code";
    
    private final long id;
    private final String code;

    public CostCenter(long id, String code) {
        this.code = code;
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public long getId() {
        return id;
    }
}
