package com.neiltolson.budget.web.rest.dto;

import java.util.List;

public class CategoryListDTO {
	private List<CategoryGroup> categoryGroups;

	public List<CategoryGroup> getCategoryGroups() {
		return categoryGroups;
	}

	public void setCategoryGroups(List<CategoryGroup> categoryGroups) {
		this.categoryGroups = categoryGroups;
	}

	public static class CategoryGroup {
		private String categoryGroup;
		private List<CategoryLine> categoryLines;

		public String getCategoryGroup() {
			return categoryGroup;
		}

		public void setCategoryGroup(String categoryGroup) {
			this.categoryGroup = categoryGroup;
		}

		public List<CategoryLine> getCategoryLines() {
			return categoryLines;
		}

		public void setCategoryLines(List<CategoryLine> categoryLines) {
			this.categoryLines = categoryLines;
		}

	}

	public static class CategoryLine {
		private Long id;
		private String categoryGroup;
		private String categoryName;
		private Boolean active;
		private Integer sortOrder;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
		public String getCategoryGroup() {
			return categoryGroup;
		}
		
		public void setCategoryGroup(String categoryGroup) {
			this.categoryGroup = categoryGroup;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}

		public Integer getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(Integer sortOrder) {
			this.sortOrder = sortOrder;
		}
	}
}
