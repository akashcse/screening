package com.powerledger.screening.entity;

import com.powerledger.screening.core.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "batteries", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Battery extends EntityBase {
    @Column("name")
    private String name;
    @Column("postcode")
    private String postcode;
    @Column("capacity")
    private Float capacity;
}
