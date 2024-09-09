package com.orion.erp_back.user.entity

/*
* Scrapping

@Entity
@Table(name = "app_role")
data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
    @SequenceGenerator(
        name = "role_sequence",
        sequenceName = "role_SEQ",
        initialValue = 100,
        allocationSize = 1,
    )
    var id: Int = 0,

    @Column(name = "role_name", updatable = false, nullable = false)
    var roleName: String,

    @Column(name = "description", updatable = false, nullable = false)
    var description: String = "",

    @ManyToMany(mappedBy = "roles")
    var users: MutableSet<User>? = null
) : GrantedAuthority {
    override fun getAuthority(): String {
        return roleName
    }
}
 */