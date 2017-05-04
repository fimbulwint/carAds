import com.google.inject.AbstractModule
import javax.inject.Singleton
import services.CarAdService
import services.DynamoService

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[CarAdService]).asEagerSingleton
    bind(classOf[DynamoService]).asEagerSingleton
  }
}
