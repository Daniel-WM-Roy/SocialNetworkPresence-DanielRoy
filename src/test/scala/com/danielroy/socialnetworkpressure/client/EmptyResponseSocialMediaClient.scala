package com.danielroy.socialnetworkpressure.client
import com.danielroy.socialnetworkpressure.SocialNetworkPressureError
import zio.{IO, ULayer, ZIO, ZLayer}

class EmptyResponseSocialMediaClient extends SocialMediaClient {
  override def connectionsGraph(socialMediaPlatform: String): IO[SocialNetworkPressureError, String] =
    ZIO.succeed("{}")
}

object EmptyResponseSocialMediaClient {
  val layer: ULayer[SocialMediaClient] =
    ZLayer.succeed(new EmptyResponseSocialMediaClient)
}
