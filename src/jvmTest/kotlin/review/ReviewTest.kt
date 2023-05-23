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
            .movieRatingShouldBeCorrect(5.0f)
            .finally()
                .stopServer()
    }
    @Test
    fun shouldNotCreateReviewWhenNotLogged(){
        ReviewTestBuilder
            .given()
                .thereIsConfiguredClient()
                .thereIsMovieInDatabase()
            .`when`()
                .postRequestOnReviewCreateIsSent("nice", 5)
            .then()
                .responseStatusCodeIsUnauthorized()
            .finally()
                .stopServer()
    }
    @Test
    fun shouldCalculateMovieRatingCorrectlyWhenReviewIsCreated(){
        ReviewTestBuilder
            .given()
                .thereIsConfiguredClient()
                .thereIsMovieInDatabase()
                .thereIsALoggedUser()
                .thereIsReviewInDatabase(7)
            .`when`()
                .postRequestOnReviewCreateIsSent("nice", 5)
            .then()
                .responseStatusCodeIsCreated()
                .movieRatingShouldBeCorrect(6.0f)
            .finally()
                .stopServer()
    }
   @Test
    fun shouldUpdateReview(){
        ReviewTestBuilder
            .given()
                .thereIsConfiguredClient()
                .thereIsMovieInDatabase()
                .thereIsALoggedUser()
                .thereIsReviewInDatabase(7)
            .`when`()
                .updateRequestOnReviewIsSent("GREAT", 10)
            .then()
                .responseStatusCodeIsOk()
                .movieRatingShouldBeCorrect(10.0f)
            .finally()
                .stopServer()
    }
    @Test
    fun shouldDeleteReview(){
        ReviewTestBuilder
            .given()
                .thereIsConfiguredClient()
                .thereIsMovieInDatabase()
                .thereIsALoggedUser()
                .thereIsReviewInDatabase(7)
            .`when`()
                .deleteRequestOnReviewIsSent()
            .then()
                .responseStatusCodeIsNoContent()
                .reviewWasDeletedFromDatabase()
                .movieRatingShouldBeCorrect(0.0f)
            .finally()
                .stopServer()
    }
}
