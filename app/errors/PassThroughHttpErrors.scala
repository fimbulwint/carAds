package errors

import scala.concurrent.Future

import play.api.http.HttpErrorHandler
import play.api.mvc.RequestHeader
import play.api.mvc.Results.InternalServerError
import play.api.mvc.Results.Status

class PassThroughHttpErrors extends HttpErrorHandler {
  
  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      Status(statusCode)( message)
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError(exception.getMessage)
    )
  }
}