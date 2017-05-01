import com.google.inject.AbstractModule

import javax.inject.Singleton
import services.CarService

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[CarService]).asEagerSingleton
  }
}
