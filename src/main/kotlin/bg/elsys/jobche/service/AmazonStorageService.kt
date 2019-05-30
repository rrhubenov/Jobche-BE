package bg.elsys.jobche.service

import bg.elsys.jobche.Constants
import com.amazonaws.services.s3.AmazonS3
import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream

@Service
class AmazonStorageService(private val client: AmazonS3) {
    fun save(file: InputStream, id: String): String {
        val tempFile = File.createTempFile(id, ".tmp")

        FileUtils.copyInputStreamToFile(file, tempFile);
        client.putObject(Constants.S3_BUCKET, id, tempFile)

        if (tempFile.exists()) {
            tempFile.delete()
        }

        return id
    }

    fun read(id: String): InputStream {
        return client.getObject(Constants.S3_BUCKET, id).objectContent
    }

    fun delete(id: String) {
        client.deleteObject(Constants.S3_BUCKET, id)
    }

    fun url(id: String): String {
//        if (id != null) {
            return client
                    .getUrl(Constants.S3_BUCKET, id)
                    .toExternalForm()
//        } else return null
    }
}