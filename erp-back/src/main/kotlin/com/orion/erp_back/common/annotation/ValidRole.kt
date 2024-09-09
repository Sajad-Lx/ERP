package com.orion.erp_back.common.annotation

/*
 * Scraped for role
import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.Role
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidRoleValidator::class])

annotation class ValidRole(
    val message: String = "Invalid role",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class ValidRoleValidator : ConstraintValidator<ValidRole, Set<Role>> {

    override fun isValid(value: Set<Role>?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true
        val validRoles = UserRole.entries.map { it.name }
        return value.all { it.roleName in validRoles }
    }
}
*/
