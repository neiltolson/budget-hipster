package com.neiltolson.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.neiltolson.budget.domain.enumeration.BudgetLineItemType;

/**
 * A BudgetLineItem.
 */
@Entity
@Table(name = "budget_line_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BudgetLineItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "budget_amount", precision=10, scale=2)
    private BigDecimal budgetAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "budget_line_item_type")
    private BudgetLineItemType budgetLineItemType;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "sort_order")
    private Integer sortOrder;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }
    
    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BudgetLineItemType getBudgetLineItemType() {
        return budgetLineItemType;
    }
    
    public void setBudgetLineItemType(BudgetLineItemType budgetLineItemType) {
        this.budgetLineItemType = budgetLineItemType;
    }

    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BudgetLineItem budgetLineItem = (BudgetLineItem) o;
        if(budgetLineItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, budgetLineItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BudgetLineItem{" +
            "id=" + id +
            ", budgetAmount='" + budgetAmount + "'" +
            ", budgetLineItemType='" + budgetLineItemType + "'" +
            ", notes='" + notes + "'" +
            ", sortOrder='" + sortOrder + "'" +
            '}';
    }
}
