package sk.letsdream.helperClasses

class LoginTableClass {

    var id: String? = null
    var username: String? = null
    var password: String? = null
    var name: String? = null
    var email: String? = null
    var privileges: String? = null
    var new_user: String? = null


    constructor() {}
    constructor(id: String?, username: String?, password: String?, name: String?, email: String?, privileges: String?, new_user: String?) {
        this.id = id
        this.username = username
        this.password = password
        this.name = name
        this.email = email
        this.privileges = privileges
        this.new_user = new_user
    }

    override fun toString(): String {
        return "logintable [id=" + id + ", username=" + username + ", password=" + password +
                ", name=" + name + ", email=" + email + ", privileges=" + privileges +
                ", new_user=" + new_user + "]"
    }

}