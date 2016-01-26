package com.neiltolson.budget.repository;

import com.neiltolson.budget.domain.UserPrefs;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserPrefs entity.
 */
public interface UserPrefsRepository extends JpaRepository<UserPrefs,Long> {

}
