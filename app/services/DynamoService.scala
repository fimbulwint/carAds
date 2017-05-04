package services

import javax.inject.Singleton
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import scala.util.Failure
import scala.util.Try
import com.amazonaws.services.dynamodbv2.document.Table
import scala.util.Success
import com.amazonaws.services.dynamodbv2.model.KeyType

@Singleton
class DynamoService {
  
  private final val dynamoClient: AmazonDynamoDBClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1)
  
  def getCarAdsTable(): Table = {
    Try(dynamoClient.describeTable(DynamoService.CAR_ADS_TABLE_NAME)) match {
      case Success(_) => new DynamoDB(dynamoClient).getTable(DynamoService.CAR_ADS_TABLE_NAME)
      case Failure(_) => createCarAdsTable
    }
  }
  
  private def createCarAdsTable(): Table = {
    val primaryKey = new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH)
    val attrsDef = new AttributeDefinition().withAttributeName("id").withAttributeType("N")
    val throughput = new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L)
    val request = new CreateTableRequest().withTableName(DynamoService.CAR_ADS_TABLE_NAME)
                                          .withKeySchema(primaryKey)
                                          .withAttributeDefinitions(attrsDef)
                                          .withProvisionedThroughput(throughput);
    val table = new DynamoDB(dynamoClient).createTable(request)
    table.waitForActive()
    table
  }
}

object DynamoService {
  private final val CAR_ADS_TABLE_NAME = "CarAds"
}