import com.google.inject.AbstractModule

import javax.inject.Singleton
import services.CarAdService

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[CarAdService]).asEagerSingleton
  }
}
