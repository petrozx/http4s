import cats.syntax.all._
import controllers.CompanyController.companyController
import controllers.FileServiceController.saveMultipartFiles
import controllers.OrderController.orderController
import controllers.UserControllers.userControllers
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import security.AuthMiddlewareC.authVerify


package object routes {
  val httpApp = Router(
    "/api/v1/" -> corsConfig
  ).orNotFound

  def apiRoutes = userControllers <+> authVerify(saveMultipartFiles <+> companyController <+> orderController)

  def corsConfig = CORS.policy
    .withAllowOriginHost(secureHost => true)
    .withAllowMethodsAll
    .withExposeHeadersAll
    .withAllowCredentials(false)
  .apply(apiRoutes)
}
