package com.neiltolson.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.neiltolson.budget.domain.enumeration.BudgetAccountType;

import com.neiltolson.budget.domain.enumeration.BudgetAccountStatus;

/**
 * A BudgetAccount.
 */
@Entity
@Table(name = "budget_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BudgetAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private BudgetAccountType accountType;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "sub_account_name")
    private String subAccountName;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BudgetAccountStatus status;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "start_balance", precision=10, scale=2)
    private BigDecimal startBalance;
    
    @Column(name = "sort_order")
    private Integer sortOrder;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BudgetAccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(BudgetAccountType accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getSubAccountName() {
        return subAccountName;
    }
    
    public void setSubAccountName(String subAccountName) {
        this.subAccountName = subAccountName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BudgetAccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(BudgetAccountStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getStartBalance() {
        return startBalance;
    }
    
    public void setStartBalance(BigDecimal startBalance) {
        this.startBalance = startBalance;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BudgetAccount budgetAccount = (BudgetAccount) o;
        if(budgetAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, budgetAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BudgetAccount{" +
            "id=" + id +
            ", userId='" + userId + "'" +
            ", accountType='" + accountType + "'" +
            ", name='" + name + "'" +
            ", subAccountName='" + subAccountName + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", lastUpdated='" + lastUpdated + "'" +
            ", status='" + status + "'" +
            ", notes='" + notes + "'" +
            ", startBalance='" + startBalance + "'" +
            ", sortOrder='" + sortOrder + "'" +
            '}';
    }
}
