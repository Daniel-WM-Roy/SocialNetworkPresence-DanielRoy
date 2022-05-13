package com.danielroy

package object socialnetworkpressure {

  sealed trait SocialNetworkPressureError
  final case class ClientError(exception: Throwable) extends SocialNetworkPressureError
  final case class DecodingError(errorMsg: String) extends SocialNetworkPressureError
  final case class ConsoleError(exception: Throwable) extends SocialNetworkPressureError
  final case class CacheError(exception: Throwable) extends SocialNetworkPressureError
}
