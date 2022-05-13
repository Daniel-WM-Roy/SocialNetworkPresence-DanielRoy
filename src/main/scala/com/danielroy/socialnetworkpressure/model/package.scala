package com.danielroy.socialnetworkpressure

import zio.json.{DeriveJsonDecoder, JsonDecoder}

package object model {

  final case class Person(name: String)
  final case class Relationship(startNode: String, endNode: String)
  final case class SocialMediaConnections(sn: String, people: List[Person], relationships: List[Relationship])

  implicit val personDecoder: JsonDecoder[Person] = DeriveJsonDecoder.gen
  implicit val relationshipDecoder: JsonDecoder[Relationship] = DeriveJsonDecoder.gen
  implicit val socialMediaConnectionsDecoder: JsonDecoder[SocialMediaConnections] = DeriveJsonDecoder.gen
}
