/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.itmpindividualdetailsstub.repository

import org.mockito.Matchers.any
import org.mockito.Mockito.{doReturn, spy, when}
import org.scalatest.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import reactivemongo.api.commands.WriteResult
import uk.gov.hmrc.itmpindividualdetailsstub.domain.Individual
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext
import scala.concurrent.Future.successful

class IndividualsRepositorySpec extends UnitSpec with MockitoSugar with ShouldMatchers {

  private val individualsRepository = spy(new IndividualsRepository(mock[MongoConnectionProvider]))

  "Individuals repository create function" should {

    val individual = Individual("id", "name")

    def mockRepositoryInsertToReturn(writeResult: WriteResult, resultCount: Int) = {
      doReturn(successful(writeResult)).when(individualsRepository).insert(any[Individual])(any[ExecutionContext])
      when(writeResult.n).thenReturn(resultCount)
    }

    "return an individual when creation is successful" in {
      mockRepositoryInsertToReturn(mock[WriteResult], 1)
      await(individualsRepository.create(individual)) shouldBe individual
    }

    "propagate an exception when creation is unsuccessful" in {
      mockRepositoryInsertToReturn(mock[WriteResult], 0)
      intercept[RuntimeException] {
        await(individualsRepository.create(individual))
      }.getMessage.startsWith("failed to persist individual") shouldBe true
    }

  }

}
