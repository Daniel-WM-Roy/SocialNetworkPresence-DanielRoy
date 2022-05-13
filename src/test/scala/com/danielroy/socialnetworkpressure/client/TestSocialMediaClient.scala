package com.danielroy.socialnetworkpressure.client

import com.danielroy.socialnetworkpressure.{CacheError, SocialNetworkPressureError}
import zio.{IO, ULayer, ZIO, ZLayer}

final class TestSocialMediaClient extends SocialMediaClient {

  // Improvement: some mechanism to limit the size of the cache to prevent it growing too big
  private val cache = scala.collection.mutable.Map[String, String]()

  override def connectionsGraph(socialMediaPlatform: String): IO[SocialNetworkPressureError, String] = {
    val callSignature = s"connectionsGraph:$socialMediaPlatform"
    if (cache.contains(callSignature)) ZIO.succeed(cache(callSignature))
    else {
      ZIO.succeed {
        """{
          |"sn": "facebook",
          |"people": [{"name":"John"},{"name":"Harry"},{"name":"Peter"}, {"name": "George"}, {"name": "Anna"}],
          |"relationships": [
          |    {"type": "HasConnection", "startNode": "John", "endNode": "Peter"},
          |    {"type": "HasConnection", "startNode": "John", "endNode": "George"},
          |    {"type": "HasConnection", "startNode": "Peter", "endNode": "George"},
          |    {"type": "HasConnection", "startNode": "Peter", "endNode": "Anna"}
          |]
          |}""".stripMargin
      }.flatMap { response =>
        ZIO.attempt {
          cache.put(callSignature, response)
          response
        }.mapError(ex => CacheError(ex))
      }
    }
  }
}

object TestSocialMediaClient {
  val layer: ULayer[SocialMediaClient] =
    ZLayer.succeed(new TestSocialMediaClient)
}