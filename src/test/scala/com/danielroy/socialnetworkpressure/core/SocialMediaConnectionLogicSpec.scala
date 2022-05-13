package com.danielroy.socialnetworkpressure.core

import com.danielroy.socialnetworkpressure.SocialNetworkPressureError
import com.danielroy.socialnetworkpressure.client.{EmptyResponseSocialMediaClient, FailingSocialMediaClient, TestSocialMediaClient}
import zio.Scope
import zio.test.Assertion._
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}

object SocialMediaConnectionLogicSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("SocialMediaConnectionLogic Specification")(
      test("Find all unconnected people in Facebook") {
        for {
          people <- SocialMediaConnectionLogic.findUnconnectedPeople("facebook")
        } yield {
          smartAssert(people)(hasSameElementsDistinct(List("Harry")))
        }
      }.provide(TestSocialMediaClient.layer),
      test("Find all unconnected people, empty response from client") {
        for {
          people <- SocialMediaConnectionLogic.findUnconnectedPeople("facebook")
        } yield {
          smartAssert(people)(isEmpty)
        }
      }.provide(EmptyResponseSocialMediaClient.layer),
      test("Find all unconnected people, client fails") {
        // Improvement: find how to test that it fails specifically with a ClientError
        for {
          people <- SocialMediaConnectionLogic.findUnconnectedPeople("facebook").exit
        } yield {
          smartAssert(people)(failsWithA[SocialNetworkPressureError])
        }
      }.provide(FailingSocialMediaClient.layer),

      test("Find 1st and 2nd degree connections, for John in Facebook") {
        for {
          connections <- SocialMediaConnectionLogic.find1and2DegreeConnections("John", List("facebook"))
        } yield {
          assertTrue(connections._1 == 2)
          assertTrue(connections._2 == 1)
        }
      }.provide(TestSocialMediaClient.layer),
      test("Find 1st and 2nd degree connections, for Peter in Facebook") {
        for {
          connections <- SocialMediaConnectionLogic.find1and2DegreeConnections("Peter", List("facebook"))
        } yield {
          assertTrue(connections._1 == 3)
          assertTrue(connections._2 == 0)
        }
      }.provide(TestSocialMediaClient.layer),
      test("Find 1st and 2nd degree connections, for Anna in Facebook") {
        for {
          connections <- SocialMediaConnectionLogic.find1and2DegreeConnections("Anna", List("facebook"))
        } yield {
          assertTrue(connections._1 == 1)
          assertTrue(connections._2 == 2)
        }
      }.provide(TestSocialMediaClient.layer),
      test("Find 1st and 2nd degree connections, empty response from client") {
        for {
          connections <- SocialMediaConnectionLogic.find1and2DegreeConnections("John", List("facebook"))
        } yield {
          assertTrue(connections._1 == 1)
          assertTrue(connections._2 == 2)
        }
      }.provide(EmptyResponseSocialMediaClient.layer),
      test("Find 1st and 2nd degree connections, client fails") {
        // Improvement: find how to test that it fails specifically with a ClientError
        for {
          connections <- SocialMediaConnectionLogic.find1and2DegreeConnections("John", List("facebook")).exit
        } yield {
          smartAssert(connections)(failsWithA[SocialNetworkPressureError])
        }
      }.provide(FailingSocialMediaClient.layer),
    )
}
