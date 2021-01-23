package scala

case class EnvironmentConfiguration(){

  private def getEnvironment() = {
    Option(System.getenv("test_environment"))
      .orElse(Option(System.getProperty("test_environment"))).get
  }

  def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def getBaseUrl(): String = {
    val env = getEnvironment().toLowerCase()
    val baseUrl = env match {
      case "qa" => "http://localhost:3000"
      case "prod" => "http://localhost:3000"
      case _ => throw new IllegalArgumentException(s"'${env}' is a invalid value!")
    }
    baseUrl
  }

  def getToken(): String = {
    val env = getEnvironment().toLowerCase()
    val token = env match {
      case "qa" => "Bearer b1c365a-0370-456f-bb47-1de8ad01eaf556f945464ba4903320bb374fa5b53998e7e6-a133-45f3-9a1c-94dc8dc920c2"
      case "prod" => "Bearer b00c365a-0370-456f-bb47-1de8ad01eaf556f945464ba4903320bb374fa5b53998e7e6-a133-45f3-9a1c-94dc8dc920c2"
      case _ => throw new IllegalArgumentException(s"'${env}' is a invalid value!")
    }
    token
  }
}
