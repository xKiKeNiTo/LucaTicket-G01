package com.grupo01.spring.model;

import java.math.BigDecimal;

public class CompraResponse {
    @Override
	public String toString() {
		return "CompraResponse [message=" + message + ", success=" + success + ", transactionId=" + transactionId
				+ ", amount=" + amount + "]";
	}

	private String message;
    private boolean success;
    private String transactionId;
    private BigDecimal amount;

    // Constructor completo
    public CompraResponse(String message, boolean success, String transactionId, BigDecimal amount) {
        this.message = message;
        this.success = success;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
