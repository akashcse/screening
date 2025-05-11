package com.powerledger.screening.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * Common properties for all entity
 */
@Getter
@Setter
@NoArgsConstructor
public class EntityBase {
	@Id
	private Long id;

	@Column("is_deleted")
	private Boolean isDeleted = false;

	@CreatedDate
	@Column("created_dt")
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column("updated_dt")
	private LocalDateTime updatedDate;
}
