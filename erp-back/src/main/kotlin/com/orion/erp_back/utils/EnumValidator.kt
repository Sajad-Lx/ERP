package com.orion.erp_back.utils

import com.orion.erp_back.common.annotation.ValidEnum
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<ValidEnum?, Enum<*>> {
    private var annotation: ValidEnum? = null

    override fun initialize(constraintAnnotation: ValidEnum?) {
        this.annotation = constraintAnnotation
    }

    override fun isValid(value: Enum<*>, context: ConstraintValidatorContext): Boolean {
        var result = false
        val enumValues: Array<out Enum<*>>? = annotation!!.enumClass.java.enumConstants

        enumValues?.forEach {
            if (value === it) {
                result = true
                return@forEach
            }
        }

        return result
    }
}
