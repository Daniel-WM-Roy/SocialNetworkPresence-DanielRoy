package com.danielroy.socialnetworkpressure.client
import com.danielroy.socialnetworkpressure.{ClientError, SocialNetworkPressureError}
import zio.{IO, ULayer, ZIO, ZLayer}

class FailingSocialMediaClient extends SocialMediaClient {
  override def connectionsGraph(socialMediaPlatform: String): IO[SocialNetworkPressureError, String] =
    ZIO.fail(ClientError(new Exception("The SocialMediaClient failed")))
}

object FailingSocialMediaClient {
  val layer: ULayer[SocialMediaClient] =
    ZLayer.succeed(new FailingSocialMediaClient)
}