package com.orion.erp_back.user.api

import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.annotation.CurrentUser
import com.orion.erp_back.user.dto.serve.request.CreateUserRequest
import com.orion.erp_back.user.dto.serve.request.UpdateUserRequest
import com.orion.erp_back.user.dto.serve.response.CreateUserResponse
import com.orion.erp_back.user.dto.serve.response.GetUserResponse
import com.orion.erp_back.user.dto.serve.response.UpdateMeResponse
import com.orion.erp_back.user.dto.serve.response.UpdateUserResponse
import com.orion.erp_back.user.service.ChangeUserService
import com.orion.erp_back.user.service.GetUserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val getUserService: GetUserService,
    private val changeUserService: ChangeUserService,
) {
    @GetMapping("/{userId}")
    fun getUserById(
        @PathVariable("userId", required = true) userId: Long
    ): ResponseEntity<GetUserResponse> = ResponseEntity.ok(getUserService.getUserById(userId))

    @GetMapping
    fun getUserList(pageable: Pageable): ResponseEntity<Page<GetUserResponse>> = ResponseEntity.ok(
        getUserService.getUserList(
            pageable
        )
    )

    @PostMapping("/register")
    fun createUser(
        @RequestBody @Valid createUserRequest: CreateUserRequest
    ): ResponseEntity<CreateUserResponse> = ResponseEntity.status(HttpStatus.CREATED).body(
        changeUserService.createUser(
            createUserRequest
        )
    )

    @PatchMapping("/{userId}")
    fun updateUser(
        @RequestBody @Valid updateUserRequest: UpdateUserRequest,
        @PathVariable("userId", required = true) userId: Long
    ): ResponseEntity<UpdateUserResponse> = ResponseEntity.ok(
        changeUserService.updateUser(
            userId, updateUserRequest
        )
    )

    @PatchMapping
    fun updateMe(
        @RequestBody @Valid updateUserRequest: UpdateUserRequest,
        @CurrentUser securityUserItem: SecurityUserItem
    ): ResponseEntity<UpdateMeResponse> = ResponseEntity.ok(
        changeUserService.updateMe(
            securityUserItem.userId, updateUserRequest
        )
    )

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable("userId", required = true) userId: Long): ResponseEntity<Void> {
        changeUserService.deleteUser(userId)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}