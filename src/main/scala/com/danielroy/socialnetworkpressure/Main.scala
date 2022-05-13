package com.danielroy.socialnetworkpressure

import com.danielroy.socialnetworkpressure.core.SocialMediaConnectionLogic._
import com.danielroy.socialnetworkpressure.client.SocialMediaClient
import zio.{Console, Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object Main extends ZIOAppDefault {

  // Printing to the Console is only for demonstration purposes

  /**
   * Print to console the given person's count of 1st and 2nd degree connections for the given social media platforms
   */
  private def printConnections(name: String, socialMedias: List[String]): ZIO[SocialMediaClient, SocialNetworkPressureError, Unit] =
    for {
      connections <- find1and2DegreeConnections(name, socialMedias)
      _ <- Console
        .printLine(s"$name, 1st Degree connections: ${connections._1}, 2nd Degree Connections: ${connections._2}")
        .mapError(ex => ConsoleError(ex))
    } yield ()

  /**
   * Print to console all people in the given social media platforms who have no connections
   */
  private def printUnconnectedPeople(socialMedias: List[String]): ZIO[SocialMediaClient, SocialNetworkPressureError, Unit] =
    ZIO.foreachPar(socialMedias) { sm =>
      for {
        unconnectedPeople <- findUnconnectedPeople(sm)
        _ <- Console
          .printLine(s"Number of unconnected people: ${unconnectedPeople.size}")
          .mapError(ex => ConsoleError(ex))
      } yield ()
    }.map(_.foldLeft(())((_, _) => ()))

  private def app: ZIO[SocialMediaClient, SocialNetworkPressureError, Unit] = {
    val people = List("John", "Peter", "George", "Harry", "Anna")
    val socialMedias = List("facebook")
    for {
      _ <- printUnconnectedPeople(socialMedias)
      _ <- ZIO.foreach(people)(person => printConnections(person, socialMedias))
    } yield ()
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ???
}
