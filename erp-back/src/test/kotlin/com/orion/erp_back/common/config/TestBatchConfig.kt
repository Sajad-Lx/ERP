package com.orion.erp_back.common.config

import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.repository.UserRepository
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@TestConfiguration
@Import(QueryDslConfig::class)
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = [UserRepository::class])
@EntityScan(basePackageClasses = [User::class])
class TestBatchConfig