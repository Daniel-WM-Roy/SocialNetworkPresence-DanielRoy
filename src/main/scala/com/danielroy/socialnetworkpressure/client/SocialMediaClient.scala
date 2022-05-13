package com.danielroy.socialnetworkpressure.client

import com.danielroy.socialnetworkpressure.SocialNetworkPressureError
import zio.IO

trait SocialMediaClient {
  def connectionsGraph(socialMediaPlatform: String): IO[SocialNetworkPressureError, String]
}