package com.neiltolson.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TransactionLineItem.
 */
@Entity
@Table(name = "transaction_line_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionLineItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;
    
    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;
    
    @ManyToOne
    @JoinColumn(name = "budget_line_item_id")
    private BudgetLineItem budgetLineItem;

    @ManyToOne
    @JoinColumn(name = "budget_account_transaction_id")
    private BudgetAccountTransaction budgetAccountTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BudgetLineItem getBudgetLineItem() {
        return budgetLineItem;
    }

    public void setBudgetLineItem(BudgetLineItem budgetLineItem) {
        this.budgetLineItem = budgetLineItem;
    }

    public BudgetAccountTransaction getBudgetAccountTransaction() {
        return budgetAccountTransaction;
    }

    public void setBudgetAccountTransaction(BudgetAccountTransaction budgetAccountTransaction) {
        this.budgetAccountTransaction = budgetAccountTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionLineItem transactionLineItem = (TransactionLineItem) o;
        if(transactionLineItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transactionLineItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TransactionLineItem{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
