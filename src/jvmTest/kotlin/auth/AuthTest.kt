package auth

import TestDatabaseFactory
import org.junit.jupiter.api.Test

class AuthTest{
    @Test
    fun shouldRegisterUser() {
        AuthTestBuilder.given()
                .thereIsConfiguredClient()
            .`when`()
                .postRequestOnAuthRegisterIsSent("username", "mail@mail.com", "password")
            .then()
                .responseStatusCodeIsCreated()
                .responseUsernameShouldBe("username")
                .responseEmailShouldBe("mail@mail.com")
            .finally()
            .stopServer()
    }

    @Test
    fun shouldLoginUserWithValidCredentials(){
        AuthTestBuilder.given()
                .thereIsConfiguredClient()
            .`when`()
                .postRequestOnAuthLoginWithGivenCredentials(
                    TestDatabaseFactory.initialUser.email,
                    TestDatabaseFactory.initialUser.password)
            .then()
                .credentialsAreValid()
                .shouldReturnToken()
            .finally()
            .stopServer()
    }

    @Test
    fun shouldNotLoginUserWithInvalidCredentials(){
        AuthTestBuilder.given()
                .thereIsConfiguredClient()
            .`when`()
                .postRequestOnAuthLoginWithGivenCredentials(
                    TestDatabaseFactory.initialUser.email,
                    "invalidPassword")
            .then()
                .credentialsAreInvalid()
            .finally()
            .stopServer()
    }
}