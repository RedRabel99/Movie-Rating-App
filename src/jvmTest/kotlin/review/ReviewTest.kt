package review

import org.junit.jupiter.api.Test

class ReviewTest {
    @Test
    fun shouldCreateReview(){
        ReviewTestBuilder
            .given()
                .thereIsConfiguredClient()
                .thereIsMovieInDatabase()
                .thereIsALoggedUser()
            .`when`()
                .postRequestOnReviewCreateIsSent("nice", 5)
            .then()
                .responseStatusCodeIsCreated()
                .reviewMovieIdShouldBeCorrect()
            .finally()
                .stopServer()
    }
}