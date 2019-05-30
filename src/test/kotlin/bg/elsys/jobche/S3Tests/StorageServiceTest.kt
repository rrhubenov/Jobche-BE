package bg.elsys.jobche.S3Tests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.Constants
import bg.elsys.jobche.service.AmazonStorageService
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.AnonymousAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.util.IOUtils
import io.findify.s3mock.S3Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import java.io.ByteArrayInputStream

class StorageServiceTest : BaseUnitTest() {

    companion object {
        const val FILE_NAME = "MockImage"
    }


    private val testStream = ByteArrayInputStream("testStream".toByteArray())
    private var api: S3Mock
    private var endpoint: AwsClientBuilder.EndpointConfiguration
    private var client: AmazonS3
    private var storageService: AmazonStorageService

    init {
        api = S3Mock.Builder().withPort(8001).withInMemoryBackend().build()
        api.start()
        endpoint = AwsClientBuilder.EndpointConfiguration("http://localhost:8001", "eu-central-1")
        client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(AWSStaticCredentialsProvider(AnonymousAWSCredentials()))
                .build()
        storageService = AmazonStorageService(client)
        client.createBucket(Constants.S3_BUCKET)
    }

    @After
    fun after() {
        api.stop()
    }

    @Test
    fun testSave() {
        val fileName = storageService.save(testStream, FILE_NAME)
        assertThat(client.doesObjectExist(Constants.S3_BUCKET, fileName)).isTrue()
    }

    @Test
    fun testDelete() {
        val fileName = storageService.save(testStream, FILE_NAME)
        storageService.delete(fileName)
        assertThat(client.doesObjectExist(Constants.S3_BUCKET, fileName)).isFalse()
    }

    @Test
    fun testRead() {
        val byteArray = IOUtils.toByteArray(testStream)
        val fileName = storageService.save(ByteArrayInputStream(byteArray), FILE_NAME)
        assertArrayEquals(byteArray, IOUtils.toByteArray(storageService.read(fileName)))
    }
}