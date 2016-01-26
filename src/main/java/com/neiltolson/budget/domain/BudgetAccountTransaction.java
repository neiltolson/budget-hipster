package com.neiltolson.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.neiltolson.budget.domain.enumeration.TransactionType;

/**
 * A BudgetAccountTransaction.
 */
@Entity
@Table(name = "budget_account_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BudgetAccountTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "check_number")
    private String checkNumber;
    
    @Column(name = "transfer_transaction_id")
    private Long transferTransactionId;
    
    @Column(name = "reconciled_date")
    private LocalDate reconciledDate;
    
    @ManyToOne
    @JoinColumn(name = "budget_account_id")
    private BudgetAccount budgetAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCheckNumber() {
        return checkNumber;
    }
    
    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Long getTransferTransactionId() {
        return transferTransactionId;
    }
    
    public void setTransferTransactionId(Long transferTransactionId) {
        this.transferTransactionId = transferTransactionId;
    }

    public LocalDate getReconciledDate() {
        return reconciledDate;
    }
    
    public void setReconciledDate(LocalDate reconciledDate) {
        this.reconciledDate = reconciledDate;
    }

    public BudgetAccount getBudgetAccount() {
        return budgetAccount;
    }

    public void setBudgetAccount(BudgetAccount budgetAccount) {
        this.budgetAccount = budgetAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BudgetAccountTransaction budgetAccountTransaction = (BudgetAccountTransaction) o;
        if(budgetAccountTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, budgetAccountTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BudgetAccountTransaction{" +
            "id=" + id +
            ", transactionDate='" + transactionDate + "'" +
            ", transactionType='" + transactionType + "'" +
            ", description='" + description + "'" +
            ", amount='" + amount + "'" +
            ", notes='" + notes + "'" +
            ", checkNumber='" + checkNumber + "'" +
            ", transferTransactionId='" + transferTransactionId + "'" +
            ", reconciledDate='" + reconciledDate + "'" +
            '}';
    }
}
