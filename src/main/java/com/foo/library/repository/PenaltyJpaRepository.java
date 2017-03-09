package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foo.library.model.Penalty;
import com.foo.library.model.PenaltyReason;
import com.foo.library.model.PenaltyStatus;
import com.foo.library.model.PenaltyType;

public interface PenaltyJpaRepository extends JpaRepository<Penalty, Long>{
	
	List<Penalty> findByRentUserIdAndStatus(String userId, PenaltyStatus status);
	
	@Modifying
	@Query("update Penalty p set p.status = :status where p.rentId = :rentId")
	int updateStatus(@Param("rentId") Long rentId, @Param("status") PenaltyStatus status);
	
	@Modifying
	@Query("update Penalty p set p.reason = :reason where p.rentId = :rentId")
	int updateReason(@Param("rentId") Long rentId, @Param("reason") PenaltyReason reason);
	
	@Modifying
	@Query("update Penalty p set p.type = :type where p.rentId = :rentId")
	int updateType(@Param("rentId") Long rentId, @Param("type") PenaltyType type);
	
	@Modifying
	@Query("update Penalty p set p.bookId = :bookId where p.rentId = :rentId")
	int updateContribution(@Param("rentId") Long rentId, @Param("bookId") Long bookId);
	
	@Modifying
	@Query("update Penalty p set p.amount = :amount where p.rentId = :rentId")
	int updateAmount(@Param("rentId") Long rentId, @Param("amount") Double amount);
	
}
