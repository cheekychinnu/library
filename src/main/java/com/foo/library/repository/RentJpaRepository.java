package com.foo.library.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foo.library.model.Rent;

@Repository
public interface RentJpaRepository extends JpaRepository<Rent, Long>{
	
	@Modifying
	@Query("update Rent set actualReturnDate = :actualReturnDate , isClosed = 1 where id = :rentId")
	int updateActualReturnDateAndMarkAsClosed(@Param("rentId") Long rentId, @Param("actualReturnDate") Date actualReturnDate);
	
	List<Rent> findByUserId(String userID);
	List<Rent> findByUserIdAndIsClosedFalse(String userID);
	List<Rent> findByUserIdAndIsClosedFalseAndDueDateBefore(String userID, Date currentDate);

	@Modifying
	@Query("update Rent set isClosed = 1 where id = :rentId")
	int markAsClosed(@Param("rentId") Long rentId);
}
