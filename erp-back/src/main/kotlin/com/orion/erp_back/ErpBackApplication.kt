package com.orion.erp_back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ErpBackApplication

fun main(args: Array<String>) {
	runApplication<ErpBackApplication>(*args)
}
