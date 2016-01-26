package com.neiltolson.budget.web.rest.dto;

import java.util.List;

import com.neiltolson.budget.domain.enumeration.BudgetLineItemType;

public class BudgetDTO {

	private LineItem carryOver;
	private LineItem income;
	private List<IncomeLine> incomeLines;
	private List<CategoryGroup> expenses;
	private LineItem total;
	private List<LabelValuePair> budgetLinks;

	public LineItem getCarryOver() {
		return carryOver;
	}

	public void setCarryOver(LineItem carryOver) {
		this.carryOver = carryOver;
	}

	public LineItem getIncome() {
		return income;
	}

	public void setIncome(LineItem income) {
		this.income = income;
	}

	public List<IncomeLine> getIncomeLines() {
		return incomeLines;
	}

	public void setIncomeLines(List<IncomeLine> incomeLines) {
		this.incomeLines = incomeLines;
	}

	public List<CategoryGroup> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<CategoryGroup> expenses) {
		this.expenses = expenses;
	}

	public LineItem getTotal() {
		return total;
	}

	public void setTotal(LineItem total) {
		this.total = total;
	}

	public List<LabelValuePair> getBudgetLinks() {
		return budgetLinks;
	}

	public void setBudgetLinks(List<LabelValuePair> budgetLinks) {
		this.budgetLinks = budgetLinks;
	}

	static class CategoryGroup {
		private String categoryGroup;
		private List<LineItem> lineItems;

		public String getCategoryGroup() {
			return categoryGroup;
		}

		public void setCategoryGroup(String categoryGroup) {
			this.categoryGroup = categoryGroup;
		}

		public List<LineItem> getLineItems() {
			return lineItems;
		}

		public void setLineItems(List<LineItem> lineItems) {
			this.lineItems = lineItems;
		}

	}

	static class IncomeLine {
		private Long id;
		private String description;
		private String amount;

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

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

	}

	static class LineItem {
		private Long id;
		private String categoryName;
		private BudgetLineItemType type;
		private boolean visible;
		private String notes;
		private String budgetAmt;
		private String actualAmt;
		private String prevBudgetAmt;
		private String prevActualAmt;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public BudgetLineItemType getType() {
			return type;
		}

		public void setType(BudgetLineItemType type) {
			this.type = type;
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}

		public String getNotes() {
			return notes;
		}

		public void setNotes(String notes) {
			this.notes = notes;
		}

		public String getBudgetAmt() {
			return budgetAmt;
		}

		public void setBudgetAmt(String budgetAmt) {
			this.budgetAmt = budgetAmt;
		}

		public String getActualAmt() {
			return actualAmt;
		}

		public void setActualAmt(String actualAmt) {
			this.actualAmt = actualAmt;
		}

		public String getPrevBudgetAmt() {
			return prevBudgetAmt;
		}

		public void setPrevBudgetAmt(String prevBudgetAmt) {
			this.prevBudgetAmt = prevBudgetAmt;
		}

		public String getPrevActualAmt() {
			return prevActualAmt;
		}

		public void setPrevActualAmt(String prevActualAmt) {
			this.prevActualAmt = prevActualAmt;
		}

	}
}
