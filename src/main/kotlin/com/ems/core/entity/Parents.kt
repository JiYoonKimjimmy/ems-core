package com.ems.core.entity

import com.ems.core.common.enum.GenderEnum
import com.ems.core.common.enum.StatusEnum
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Table("PARENTS")
class Parents(
    @Id
    @Column("ID")
    var id: Long? = null,
    @Column("NAME")
    val name: String,
    @Column("MOBILE_NUMBER")
    val mobileNumber: String,
    @Column("GENDER")
    val gender: GenderEnum,
    @Column("STATUS")
    val status: StatusEnum,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    val student: Student?

) : BaseEntity()