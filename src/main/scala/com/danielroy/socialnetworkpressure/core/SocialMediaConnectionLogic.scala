package com.danielroy.socialnetworkpressure.core

import com.danielroy.socialnetworkpressure.client.SocialMediaClient
import com.danielroy.socialnetworkpressure.model._
import com.danielroy.socialnetworkpressure.{DecodingError, SocialNetworkPressureError}
import zio.ZIO
import zio.json.DecoderOps

object SocialMediaConnectionLogic {

  /**
   * Get the social media connections for the given social media platform
   *
   * @param socialMediaPlatform
   * @return SocialMediaConnections
   */
  private def getSocialMediaConnections(socialMediaPlatform: String): ZIO[SocialMediaClient, SocialNetworkPressureError, SocialMediaConnections] =
    for {
      client <- ZIO.service[SocialMediaClient]
      strResponse <- client.connectionsGraph(socialMediaPlatform)
      smConnections <- ZIO.fromEither(strResponse.fromJson[SocialMediaConnections]).mapError(errMsg => DecodingError(errMsg))
    } yield smConnections

  /**
   * Scan the given social media platform for all people without relationship connections
   *
   * @param socialMedia : name of the social media platform to scan
   * @return a List of the names of the people without connections
   */
  def findUnconnectedPeople(socialMedia: String): ZIO[SocialMediaClient, SocialNetworkPressureError, List[String]] =
    for {
      smConnections <- getSocialMediaConnections(socialMedia)
      list <- ZIO.foreachPar(smConnections.relationships)(rel => ZIO.succeed(List(rel.startNode, rel.endNode)))
      allConnectedPeople = list.flatten.distinct
      unconnectedPeople = smConnections.people.map(_.name).diff(allConnectedPeople)
    } yield unconnectedPeople

  /**
   * Scan the given social media platforms and count all 1st and 2nd degree relationship connections for the given person
   *
   * @param person       : name of person
   * @param socialMedias : list of social media platforms
   * @return a tuple representing, in order, the 1st degree and the 2nd degree connection counts
   */
  def find1and2DegreeConnections(person: String, socialMedias: List[String]): ZIO[SocialMediaClient, SocialNetworkPressureError, (Int, Int)] = {
    def getConnections(person: String, relationship: List[Relationship]): ZIO[SocialMediaClient, Nothing, List[String]] = ZIO.succeed {
      relationship.flatMap { rel =>
        if (rel.startNode == person) Some(rel.endNode)
        else if (rel.endNode == person) Some(rel.startNode)
        else None
      }
    }

    ZIO.foreachPar(socialMedias) { sm =>
      for {
        smConnections <- getSocialMediaConnections(sm)
        sansPerson = smConnections.relationships.filterNot(rel => (rel.startNode == person || rel.endNode == person))
        firstDegree <- getConnections(person, smConnections.relationships)
        secondDegreeUnverified <- ZIO.foreachPar(firstDegree)(getConnections(_, sansPerson)).map(_.flatten)
        secondDegree = secondDegreeUnverified.diff(firstDegree) // Remove 1st degree connections
      } yield (firstDegree.size, secondDegree.size)
    }.map(_.foldLeft((0, 0)) { case (l, r) => (l._1 + r._1, l._2 + r._2) })
  }
}
