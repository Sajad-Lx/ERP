package com.orion.erp_back.user.entity

import com.orion.erp_back.user.constant.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.jboss.aerogear.security.otp.api.Base32
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_SEQ",
        initialValue = 100,
        allocationSize = 1,
    )
    var id: Long = 0L,

    @NotEmpty @Column(name = "username", unique = true, nullable = false)
    var username: String,

    @Email @Column(name = "email", unique = true, nullable = false, updatable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    var isUsing2FA: Boolean = false,

    var secret: String = Base32.random(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.STANDARD_USER
) {
    fun update(username: String, firstName: String, lastName: String, role: UserRole): User {
        this.username = username
        this.firstName = firstName
        this.lastName = lastName
        this.role = role
        return this
    }

    fun encodePassword(bCryptPasswordEncoder: BCryptPasswordEncoder): User {
        this.password = bCryptPasswordEncoder.encode(this.password)
        return this
    }

    fun validatePassword(
        password: String?,
        bCryptPasswordEncoder: BCryptPasswordEncoder
    ): Boolean {
        return bCryptPasswordEncoder.matches(password, this.password)
    }
}