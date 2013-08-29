package org.activiti.spring.test.components.config.java;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Cart implements Serializable {
    public long customerId;
    private static final long serialVersionUID = 2424207500000L ;

    @Override
    public String toString() {
     return new ToStringBuilder( this)
              .append("customerId",this.customerId)
             .append("amountDue", this.amountDue)
             .build();
    }

    public double amountDue = 0.0D;

    public Cart() {
    }

    public Cart(long customerId, double amountDue) {
        this.customerId = customerId;
        this.amountDue = amountDue;
    }

}